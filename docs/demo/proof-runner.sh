#!/usr/bin/env bash
set -euo pipefail

# ---------------------------------------------
# Executable Architecture Proof
# Generate -> Verify (GREEN) -> Violate -> Verify (RED) -> Fix -> Verify (GREEN)
#
# Adds:
# - repo-root detection (run from anywhere)
# - env metadata capture
# - step tracking + deterministic proof summary
# - optional export of logs/excerpts/summary via PROOF_EXPORT_DIR
# - always prints a single PROOF RESULT line at the end (PASS/FAIL)
# ---------------------------------------------
# -------------------------
# Config (env-overridable)
# -------------------------
GROUP_ID="${GROUP_ID:-io.github.blueprintplatform}"
PACKAGE_NAME="${PACKAGE_NAME:-io.github.blueprintplatform.greeting}"
JAVA_BIN="${JAVA_BIN:-java}"

KEEP_WORK_DIR="${KEEP_WORK_DIR:-0}"

# If set, additionally copy proof artifacts there (optional)
PROOF_EXPORT_DIR="${PROOF_EXPORT_DIR:-}"

# -------------------------
# Globals (late-init)
# -------------------------
ROOT_DIR=""
CODEGEN_JAR=""

WORK_DIR="$(mktemp -d -t codegen-proof-XXXXXX)"
OUT_DIR="${WORK_DIR}/out"

# --- Proof output (in-repo, easy to find) ---
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEMO_DIR="${SCRIPT_DIR}"                       # script lives in docs/demo
PROOF_OUTPUT_ROOT="${PROOF_OUTPUT_ROOT:-${DEMO_DIR}/proof-output}"

RUN_TS="$(date +%Y%m%d-%H%M%S)"
RUN_DIR="${PROOF_OUTPUT_ROOT}/${RUN_TS}"
LATEST_DIR="${PROOF_OUTPUT_ROOT}/latest"

LOG_DIR="${RUN_DIR}/logs"
EXCERPT_DIR="${RUN_DIR}/excerpts"

mkdir -p "${LOG_DIR}" "${EXCERPT_DIR}"

ENV_FILE="${RUN_DIR}/env.txt"
SUMMARY_FILE="${RUN_DIR}/proof-summary.txt"

# backup store OUTSIDE generated project tree (temp)
BACKUP_DIR="${WORK_DIR}/.backup"
mkdir -p "$BACKUP_DIR"

# step tracking (label|status|log|excerpt)
declare -a STEPS=()

SCRIPT_EXIT_CODE=0

# -------------------------
# UX helpers
# -------------------------
log() { printf "\n\033[1m[%s]\033[0m %s\n" "$(date +%H:%M:%S)" "$*" >&2; }
die() { printf "\n\033[31mERROR:\033[0m %s\n" "$*" >&2; exit 1; }

require_file() { [[ -f "$1" ]] || die "Required file not found: $1"; }
require_cmd() { command -v "$1" >/dev/null 2>&1 || die "Required command not found on PATH: $1"; }

# -------------------------
# New: repo root detection
# -------------------------
ensure_repo_root() {
  # Prefer git if available and we are inside a repo
  if command -v git >/dev/null 2>&1; then
    local git_root
    git_root="$(git rev-parse --show-toplevel 2>/dev/null || true)"
    if [[ -n "$git_root" && -d "$git_root" ]]; then
      echo "$git_root"
      return 0
    fi
  fi

  # Fallback: assume script is under <repo>/docs/demo or similar and go up 2 levels
  local script_dir
  script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
  local guess
  guess="$(cd "${script_dir}/../.." && pwd)"

  [[ -d "$guess" ]] || die "Could not infer repository root. Run from inside the repo or install git."
  echo "$guess"
}

# -------------------------
# New: label normalization
# -------------------------
normalize_label() {
  local s="$1"
  # filesystem-safe, stable:
  # - trim
  # - spaces -> _
  # - anything not [A-Za-z0-9._-] -> _
  s="${s#"${s%%[![:space:]]*}"}"
  s="${s%"${s##*[![:space:]]}"}"
  s="${s// /_}"
  s="$(printf "%s" "$s" | sed 's/[^A-Za-z0-9._-]/_/g')"
  echo "$s"
}

normalize_label() {
  local s="$1"
  # filesystem-safe: spaces->_, trim weird chars, collapse repeats
  s="${s// /_}"
  s="$(printf '%s' "$s" | tr -cd '[:alnum:]_.-')"
  # avoid empty
  [[ -n "$s" ]] || s="step"
  echo "$s"
}

log_file_for_label() {
  local label="$1"
  echo "${LOG_DIR}/$(normalize_label "$label").log"
}

# excerpts artık ayrı klasöre
excerpt_file_for_label() {
  local label="$1"
  echo "${EXCERPT_DIR}/$(normalize_label "$label").excerpt.txt"
}

record_step() {
  local label="$1" status="$2" log_file="$3" excerpt_file="${4:-}"
  local rel_log rel_excerpt
  rel_log="${log_file#${RUN_DIR}/}"
  rel_excerpt="${excerpt_file#${RUN_DIR}/}"
  STEPS+=("${label}|${status}|${rel_log}|${rel_excerpt}")
}

# -------------------------
# Excerpt writer (slightly more deterministic for ArchUnit/Maven)
# -------------------------
write_excerpt() {
  local log_file="$1"
  local excerpt_file="$2"

  awk '
    /Architecture Violation|ArchUnit|guardrails|Violation|violat|Rules? were violated|STANDARD package schema integrity failure|FAILED|<<< FAILURE!/ {hit=NR}
    {lines[NR]=$0}
    END{
      if(NR==0){exit 0}
      if(hit==0){hit=NR}
      start=hit-50; if(start<1) start=1
      end=hit+120; if(end>NR) end=NR
      for(i=start;i<=end;i++) print lines[i]
    }
  ' "$log_file" > "$excerpt_file"
}

# -------------------------
# New: env metadata capture
# -------------------------
collect_env_metadata() {
  {
    echo "timestamp: $(date -Iseconds)"
    echo "pwd: $(pwd)"
    echo "root_dir: ${ROOT_DIR}"
    echo "codegen_jar: ${CODEGEN_JAR}"
    echo "java_bin: ${JAVA_BIN}"
    echo

    echo "java -version:"
    ( "$JAVA_BIN" -version 2>&1 || true )
    echo

    echo "maven:"
    if [[ -x "${ROOT_DIR}/mvnw" ]]; then
      ( "${ROOT_DIR}/mvnw" -v 2>&1 || true )
    elif command -v mvn >/dev/null 2>&1; then
      ( mvn -v 2>&1 || true )
    else
      echo "mvn not found on PATH"
    fi
    echo

    if command -v shasum >/dev/null 2>&1; then
      echo "codegen_jar_sha256:"
      ( shasum -a 256 "${CODEGEN_JAR}" 2>/dev/null || true )
      echo
    elif command -v sha256sum >/dev/null 2>&1; then
      echo "codegen_jar_sha256:"
      ( sha256sum "${CODEGEN_JAR}" 2>/dev/null || true )
      echo
    fi

    echo "git:"
    if command -v git >/dev/null 2>&1 && git -C "${ROOT_DIR}" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
      echo "  branch: $(git -C "${ROOT_DIR}" rev-parse --abbrev-ref HEAD 2>/dev/null || true)"
      echo "  commit: $(git -C "${ROOT_DIR}" rev-parse HEAD 2>/dev/null || true)"
      echo "  dirty:  $(git -C "${ROOT_DIR}" status --porcelain 2>/dev/null | wc -l | tr -d " ") file(s)"
    else
      echo "  not a git worktree"
    fi
  } > "$ENV_FILE"
}

# -------------------------
# Maven runner with mvnw chmod fix
# -------------------------
run_maven_verify() {
  # expects to run in project dir
  if [[ -f "./mvnw" && ! -x "./mvnw" ]]; then
    chmod +x ./mvnw 2>/dev/null || true
  fi

  if [[ -x "./mvnw" ]]; then
    ./mvnw -q -ntp clean verify
    return $?
  fi

  require_cmd mvn
  mvn -q -ntp clean verify
  return $?
}

run_verify() {
  local project_dir="$1"
  local label="$2"

  log "${label}: clean verify"
  pushd "$project_dir" >/dev/null

  local log_file
  log_file="$(log_file_for_label "$label")"

  set +e
  local rc
  run_maven_verify 2>&1 | tee "$log_file"
  rc=${PIPESTATUS[0]}
  set -e

  popd >/dev/null

  if [[ $rc -ne 0 ]]; then
    local excerpt_file
    excerpt_file="$(excerpt_file_for_label "$label")"
    write_excerpt "$log_file" "$excerpt_file"
    record_step "$label" "FAIL" "$log_file" "$excerpt_file"
    die "${label}: build FAILED (rc=${rc}). log: ${log_file} excerpt: ${excerpt_file}"
  fi

  record_step "$label" "PASS" "$log_file" ""
  log "${label}: PASS confirmed (log=${log_file})"
}

run_verify_expect_fail() {
  local project_dir="$1"
  local label="$2"

  log "${label}: clean verify (expected FAIL)"
  pushd "$project_dir" >/dev/null

  local log_file
  log_file="$(log_file_for_label "$label")"

  set +e
  local rc
  run_maven_verify 2>&1 | tee "$log_file"
  rc=${PIPESTATUS[0]}
  set -e

  popd >/dev/null

  if [[ $rc -eq 0 ]]; then
    record_step "$label" "UNEXPECTED_PASS" "$log_file" ""
    die "${label}: build PASSED but was expected to FAIL after violation. log: ${log_file}"
  fi

  local excerpt_file
  excerpt_file="$(excerpt_file_for_label "$label")"
  write_excerpt "$log_file" "$excerpt_file"
  record_step "$label" "EXPECTED_FAIL" "$log_file" "$excerpt_file"

  log "${label}: FAIL confirmed (rc=${rc}, log=${log_file})"
  log "${label}: excerpt written: ${excerpt_file}"
}

generate_project() {
  local artifact_id="$1"
  local name="$2"
  local description="$3"
  local layout="$4"
  local guardrails="$5"
  local sample="$6"

  log "Generate: ${artifact_id} (${layout}, guardrails=${guardrails}, sample=${sample})"
  mkdir -p "$OUT_DIR"

  "$JAVA_BIN" -jar "$CODEGEN_JAR" \
    --cli springboot \
    --group-id "$GROUP_ID" \
    --artifact-id "$artifact_id" \
    --name "$name" \
    --description "$description" \
    --package-name "$PACKAGE_NAME" \
    --layout "$layout" \
    --guardrails "$guardrails" \
    --sample-code "$sample" \
    --dependency web \
    --target-dir "$OUT_DIR" >/dev/null

  local project_dir="${OUT_DIR}/${artifact_id}"
  [[ -d "$project_dir" ]] || die "Generated project dir not found: $project_dir"
  echo "$project_dir"
}

# -------------------------
# Backup / Restore (file OR dir) - OUTSIDE project tree
# -------------------------
backup_key_for_path() {
  local path="$1"
  local rel="$path"
  rel="${rel#${WORK_DIR}/}"
  printf "%s" "$rel" | sed 's#/#__#g'
}

backup_path() {
  local path="$1"
  [[ -e "$path" ]] || die "Backup source not found: $path"

  local key backup
  key="$(backup_key_for_path "$path")"
  backup="${BACKUP_DIR}/${key}"

  if [[ -e "$backup" ]]; then
    return 0
  fi

  rm -rf "$backup"
  mkdir -p "$(dirname "$backup")"

  if [[ -d "$path" ]]; then
    cp -R "$path" "$backup"
  else
    cp "$path" "$backup"
  fi
}

restore_path() {
  local path="$1"

  local key backup
  key="$(backup_key_for_path "$path")"
  backup="${BACKUP_DIR}/${key}"

  [[ -e "$backup" ]] || die "Backup not found: $backup"
  log "Restore original path: ${path}"

  rm -rf "$path"
  mkdir -p "$(dirname "$path")"
  mv -f "$backup" "$path"

  if [[ -f "$path" ]] && grep -q "__archViolation" "$path"; then
    die "Restore failed: __archViolation still present in $path"
  fi
}

backup_file() { backup_path "$1"; }
restore_file() { restore_path "$1"; }

restore_files() {
  local f
  while IFS= read -r f; do
    [[ -n "$f" ]] || continue
    restore_path "$f"
  done
}

# -------------------------
# Violation injectors (unchanged logic)
# -------------------------
fqcn_from_java_path() {
  local project_dir="$1"
  local java_file="$2"

  local rel="${java_file#${project_dir}/src/main/java/}"
  local fqcn="${rel%.java}"
  fqcn="${fqcn//\//.}"
  echo "$fqcn"
}

simple_name_from_fqcn() {
  local fqcn="$1"
  echo "${fqcn##*.}"
}

insert_import_after_package_if_missing() {
  local file="$1"
  local fqcn="$2"

  if grep -q "^import ${fqcn};$" "$file"; then
    return 0
  fi

  awk -v imp="import ${fqcn};" '
    $0 ~ /^package / { print; print ""; print imp; next }
    { print }
  ' "$file" > "${file}.tmp" && mv "${file}.tmp" "$file"
}

insert_field_after_class_open_if_missing() {
  local file="$1"
  local type_simple="$2"

  if grep -q "__archViolation" "$file"; then
    return 0
  fi

  awk -v t="$type_simple" '
    BEGIN { injected=0 }
    {
      print
      if (injected==0 && $0 ~ /\{/) {
        print ""
        print "    private " t " __archViolation = null;"
        injected=1
      }
    }
    END {
      if (injected==0) exit 42
    }
  ' "$file" > "${file}.tmp"

  local rc=$?
  if [[ $rc -ne 0 ]]; then
    rm -f "${file}.tmp"
    die "Failed to inject __archViolation field into ${file} (no opening brace found)"
  fi

  mv "${file}.tmp" "$file"
  grep -q "__archViolation" "$file" || die "Failed to inject __archViolation field into ${file}"
}

find_controller_file() {
  local project_dir="$1"
  local base="${project_dir}/src/main/java"

  [[ -d "$base" ]] || die "No src/main/java found in ${project_dir}"

  local f
  f="$(grep -RIl --include='*Controller.java' -m 1 '@RestController' "$base" || true)"
  if [[ -z "$f" ]]; then
    f="$(find "$base" -type f -name '*Controller.java' | head -n 1 || true)"
  fi
  [[ -n "$f" ]] || die "Could not find a controller file in ${project_dir}. (Is sample-code basic enabled?)"
  echo "$f"
}

find_hex_application_impl_type_file() {
  local project_dir="$1"
  local src="${project_dir}/src/main/java"
  local f

  f="$(find "$src" -type f -name '*Handler.java' | grep '/application/' | head -n 1 || true)"

  if [[ -z "$f" ]]; then
    f="$(find "$src" -type f -name '*.java' \
      | grep '/application/' \
      | grep -v '/port/' \
      | head -n 1 || true)"
  fi

  [[ -n "$f" ]] || die "HEX: Could not find an application implementation class (under /application/ but not /port/). (Is sample-code basic enabled?)"
  echo "$f"
}

find_hex_adapter_type_file() {
  local project_dir="$1"
  local src="${project_dir}/src/main/java"
  local f

  f="$(grep -RIl --include='*Controller.java' -m 1 '@RestController' "$src" | head -n 1 || true)"
  if [[ -z "$f" ]]; then
    f="$(find "$src" -type f -name '*.java' | grep '/adapter/' | head -n 1 || true)"
  fi

  [[ -n "$f" ]] || die "HEX: Could not find an adapter class under /adapter/. (Is sample-code basic enabled?)"
  echo "$f"
}

inject_hex_violation_adapter_depends_on_application_impl() {
  local project_dir="$1"

  local adapter_file
  adapter_file="$(find_hex_adapter_type_file "$project_dir")"

  local impl_file
  impl_file="$(find_hex_application_impl_type_file "$project_dir")"

  local impl_fqcn
  impl_fqcn="$(fqcn_from_java_path "$project_dir" "$impl_file")"

  local impl_simple
  impl_simple="$(simple_name_from_fqcn "$impl_fqcn")"

  log "Inject HEX violation (adapter -> application impl): ${adapter_file}"
  backup_file "$adapter_file"

  insert_import_after_package_if_missing "$adapter_file" "$impl_fqcn"
  insert_field_after_class_open_if_missing "$adapter_file" "$impl_simple"

  grep -q "__archViolation" "$adapter_file" || die "HEX: Failed to inject violation into adapter."
  echo "$adapter_file"
}

find_standard_repository_type_file() {
  local project_dir="$1"
  local src="${project_dir}/src/main/java"
  local f

  f="$(find "$src" -type f -name '*Repository.java' | grep '/repository/' | head -n 1 || true)"
  if [[ -z "$f" ]]; then
    f="$(find "$src" -type f -name '*Repository.java' | head -n 1 || true)"
  fi
  if [[ -z "$f" ]]; then
    f="$(grep -RIl --include='*.java' -m 1 '@Repository' "$src" || true)"
  fi

  [[ -n "$f" ]] || die "STD: Could not find a repository type (*Repository.java or @Repository). (Is sample-code basic enabled?)"
  echo "$f"
}

inject_standard_violation_controller_depends_on_repository() {
  local project_dir="$1"
  local controller_file="$2"

  log "Inject STD violation (controller -> repository): ${controller_file}"
  backup_file "$controller_file"

  local repo_file
  repo_file="$(find_standard_repository_type_file "$project_dir")"

  local repo_fqcn
  repo_fqcn="$(fqcn_from_java_path "$project_dir" "$repo_file")"

  local repo_simple
  repo_simple="$(simple_name_from_fqcn "$repo_fqcn")"

  insert_import_after_package_if_missing "$controller_file" "$repo_fqcn"
  insert_field_after_class_open_if_missing "$controller_file" "$repo_simple"

  grep -q "__archViolation" "$controller_file" || die "STD: Failed to inject violation into controller."
}

replace_package_segment_once() {
  local file="$1"
  local from_segment="$2"
  local to_segment="$3"

  awk -v from="${from_segment}" -v to="${to_segment}" '
    BEGIN { done=0 }
    /^package[[:space:]]+/ && done==0 {
      if (index($0, "." from ";") > 0) {
        gsub("\\." from ";", "." to ";")
        done=1
      } else if (index($0, "." from ".") > 0) {
        gsub("\\." from "\\.", "." to ".")
        done=1
      } else {
        exit 43
      }
    }
    { print }
    END { if (done==0) exit 42 }
  ' "$file" > "${file}.tmp"

  local rc=$?
  if [[ $rc -ne 0 ]]; then
    rm -f "${file}.tmp"
    die "Package segment replace failed for ${file} (rc=${rc})"
  fi

  mv "${file}.tmp" "$file"
}

inject_standard_schema_violation_controller_package_rename() {
  local project_dir="$1"

  local base_path
  base_path="$(printf '%s' "$PACKAGE_NAME" | tr '.' '/')"

  local main_base="${project_dir}/src/main/java/${base_path}"
  local test_base="${project_dir}/src/test/java/${base_path}"

  local main_controller_dir="${main_base}/controller"
  local main_controllerx_dir="${main_base}/controllerx"

  [[ -d "$main_controller_dir" ]] || die "STD schema inject: root controller dir not found: ${main_controller_dir}"

  log "Inject STD schema violation (rename root controller dir -> controllerx) (STRUCTURAL + SEMANTIC)"
  log "  main dir: ${main_controller_dir}"

  backup_path "$main_controller_dir"
  mv "$main_controller_dir" "$main_controllerx_dir"

  find "$main_controllerx_dir" -type f -name '*.java' -print0 | while IFS= read -r -d '' f; do
    replace_package_segment_once "$f" "controller" "controllerx"
  done

  local test_controller_dir="${test_base}/controller"
  local test_controllerx_dir="${test_base}/controllerx"

  if [[ -d "$test_controller_dir" ]]; then
    log "  test dir: ${test_controller_dir}"
    backup_path "$test_controller_dir"
    mv "$test_controller_dir" "$test_controllerx_dir"
    find "$test_controllerx_dir" -type f -name '*.java' -print0 | while IFS= read -r -d '' f; do
      replace_package_segment_once "$f" "controller" "controllerx"
    done
  fi

  [[ ! -d "$main_base/controller" ]] || die "STD schema inject failed: controller dir still exists under ${main_base}"
  [[ -d "$main_base/controllerx" ]] || die "STD schema inject failed: controllerx dir not created under ${main_base}"

  printf "%s\n" "$main_controller_dir"
  if [[ -d "$test_controllerx_dir" ]]; then
    printf "%s\n" "$test_controller_dir"
  fi
}

# -------------------------
# New: summary + result line
# -------------------------
finalize_proof_status() {
  local exit_code="$1"

  {
    echo "Executable Architecture Proof — Summary"
    echo "timestamp: $(date -Iseconds)"
    echo "exit_code: ${exit_code}"
    echo

    echo "Environment:"
    echo "  root_dir: ${ROOT_DIR}"
    echo "  codegen_jar: ${CODEGEN_JAR}"
    echo "  logs_dir: ${LOG_DIR}"
    echo

    echo "Steps:"
    if [[ ${#STEPS[@]} -eq 0 ]]; then
      echo "  (no steps recorded)"
    else
      local s label status lf ef
      for s in "${STEPS[@]}"; do
        IFS='|' read -r label status lf ef <<< "$s"
        printf "  - %-20s : %-14s : %s\n" "$label" "$status" "$lf"
        if [[ -n "$ef" ]]; then
          printf "      excerpt: %s\n" "$ef"
        fi
      done
    fi
    echo

    if [[ $exit_code -eq 0 ]]; then
      echo "PROOF RESULT: PASS (GREEN → RED → GREEN)"
    else
      echo "PROOF RESULT: FAIL (exit_code=${exit_code})"
    fi
  } > "$SUMMARY_FILE"

  # Single deterministic console line (what a reader/CI needs)
  if [[ $exit_code -eq 0 ]]; then
    log "PROOF RESULT: PASS (GREEN → RED → GREEN)"
  else
    log "PROOF RESULT: FAIL (exit_code=${exit_code})"
  fi
}

# -------------------------
# New: export artifacts
# -------------------------
export_proof_artifacts() {
  [[ -n "$PROOF_EXPORT_DIR" ]] || return 0

  local export_dir="$PROOF_EXPORT_DIR"
  mkdir -p "$export_dir"

  # Copy logs + excerpts + env + summary
  cp -f "$ENV_FILE" "$export_dir/" 2>/dev/null || true
  cp -f "$SUMMARY_FILE" "$export_dir/" 2>/dev/null || true
  cp -f "${LOG_DIR}/"*.log "$export_dir/" 2>/dev/null || true
  cp -f "${LOG_DIR}/"*.excerpt.txt "$export_dir/" 2>/dev/null || true

  log "Exported proof artifacts to: ${export_dir}"
}
# -------------------------
# Cleanup / trap (export before cleanup)
# -------------------------
cleanup() {
  if [[ "$KEEP_WORK_DIR" == "1" ]]; then
    log "KEEP_WORK_DIR=1 -> workspace preserved: ${WORK_DIR}"
    return 0
  fi
  log "Cleanup temp dir: ${WORK_DIR}"
  rm -rf "$WORK_DIR"
}

on_exit() {
  local rc=$?
  SCRIPT_EXIT_CODE=$rc

  # Always write env + summary, then export (even on failure), then cleanup
  if [[ -n "${ROOT_DIR}" && -n "${CODEGEN_JAR}" ]]; then
    collect_env_metadata || true
  fi

  finalize_proof_status "$SCRIPT_EXIT_CODE" || true
  export_proof_artifacts || true
  cleanup || true

  exit "$SCRIPT_EXIT_CODE"
}
trap on_exit EXIT

# -------------------------
# Main
# -------------------------
main() {
  ROOT_DIR="$(ensure_repo_root)"
  CODEGEN_JAR="${CODEGEN_JAR:-${ROOT_DIR}/target/codegen-blueprint-1.0.0.jar}"

  require_file "$CODEGEN_JAR"
  require_cmd "$JAVA_BIN"

  # RUN_DIR/LOG_DIR/EXCERPT_DIR
  # RUN_DIR=docs/demo/proof-output/<timestamp>
  # LOG_DIR=${RUN_DIR}/logs
  # EXCERPT_DIR=${RUN_DIR}/excerpts
  # ENV_FILE=${RUN_DIR}/env.txt
  # SUMMARY_FILE=${RUN_DIR}/proof-summary.txt
  log "Workspace (temp): ${WORK_DIR}"
  log "Using CODEGEN_JAR: ${CODEGEN_JAR}"
  log "Proof output (run): ${RUN_DIR}"
  log "  logs:     ${LOG_DIR}"
  log "  excerpts: ${EXCERPT_DIR}"

  if [[ -n "$PROOF_EXPORT_DIR" ]]; then
    log "PROOF_EXPORT_DIR: ${PROOF_EXPORT_DIR}"
  fi

  # env first so even early failures have a baseline artifact
  collect_env_metadata

  local hex_dir
  local std_dir

  hex_dir="$(generate_project \
    "greeting-hex" \
    "Greeting (Hexagonal)" \
    "Greeting sample built with hexagonal architecture" \
    "hexagonal" \
    "strict" \
    "basic")"

  std_dir="$(generate_project \
    "greeting-standard" \
    "Greeting (Standard Layered)" \
    "Greeting sample built with standard layered architecture" \
    "standard" \
    "strict" \
    "basic")"

  log "HEX dir: ${hex_dir}"
  log "STD dir: ${std_dir}"

  run_verify "$hex_dir" "HEX baseline"
  run_verify "$std_dir" "STD baseline"

  local hex_adapter_touched
  hex_adapter_touched="$(inject_hex_violation_adapter_depends_on_application_impl "$hex_dir")"
  run_verify_expect_fail "$hex_dir" "HEX violation"
  restore_file "$hex_adapter_touched"
  run_verify "$hex_dir" "HEX fixed"

  local std_schema_touched
  std_schema_touched="$(inject_standard_schema_violation_controller_package_rename "$std_dir")"
  run_verify_expect_fail "$std_dir" "STD schema violation"
  restore_files <<< "$std_schema_touched"
  run_verify "$std_dir" "STD schema fixed"

  local std_controller
  std_controller="$(find_controller_file "$std_dir")"
  inject_standard_violation_controller_depends_on_repository "$std_dir" "$std_controller"
  run_verify_expect_fail "$std_dir" "STD violation"
  restore_file "$std_controller"
  run_verify "$std_dir" "STD fixed"

  log "DONE: Proof completed successfully (GREEN → RED → GREEN)"
  log "Proof output: ${RUN_DIR}"
  log "Latest pointer: ${LATEST_DIR}"

  if [[ -n "$PROOF_EXPORT_DIR" ]]; then
    log "Artifacts exported to: ${PROOF_EXPORT_DIR}"
  fi

  log "Tip: set KEEP_WORK_DIR=1 to inspect generated projects under ${WORK_DIR}"
}

main "$@"