#!/usr/bin/env bash
set -euo pipefail

# ---------------------------------------------
# Executable Architecture Proof
# Generate -> Verify (GREEN) -> Violate -> Verify (RED) -> Fix -> Verify (GREEN)
# ---------------------------------------------

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DEMO_DIR="${ROOT_DIR}/demo"

CODEGEN_JAR="${CODEGEN_JAR:-${ROOT_DIR}/target/codegen-blueprint-1.0.0.jar}"

GROUP_ID="${GROUP_ID:-io.github.blueprintplatform}"
PACKAGE_NAME="${PACKAGE_NAME:-io.github.blueprintplatform.greeting}"
JAVA_BIN="${JAVA_BIN:-java}"

WORK_DIR="$(mktemp -d -t codegen-proof-XXXXXX)"
OUT_DIR="${WORK_DIR}/out"

log() { printf "\n\033[1m[%s]\033[0m %s\n" "$(date +%H:%M:%S)" "$*" >&2; }
die() { printf "\n\033[31mERROR:\033[0m %s\n" "$*" >&2; exit 1; }

require_file() { [[ -f "$1" ]] || die "Required file not found: $1"; }
require_cmd() { command -v "$1" >/dev/null 2>&1 || die "Required command not found on PATH: $1"; }

run_verify() {
  local project_dir="$1"
  local label="$2"

  log "${label}: clean verify"
  pushd "$project_dir" >/dev/null

  if [[ -x "./mvnw" ]]; then
    ./mvnw -q -ntp clean verify
  else
    require_cmd mvn
    mvn -q -ntp clean verify
  fi

  popd >/dev/null
}

run_verify_expect_fail() {
  local project_dir="$1"
  local label="$2"

  log "${label}: clean verify (expected FAIL)"
  set +e
  pushd "$project_dir" >/dev/null

  local rc
  if [[ -x "./mvnw" ]]; then
    ./mvnw -q -ntp clean verify
    rc=$?
  else
    require_cmd mvn
    mvn -q -ntp clean verify
    rc=$?
  fi

  popd >/dev/null
  set -e

  if [[ $rc -eq 0 ]]; then
    die "${label}: build PASSED but was expected to FAIL after violation. (Either violation didn't apply or rules differ.)"
  fi

  log "${label}: FAIL confirmed (rc=${rc})"
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

backup_file() {
  local file="$1"
  local backup="${file}.bak"

  if [[ -f "$backup" ]]; then
    return 0
  fi

  cp "$file" "$backup"
}

restore_file() {
  local file="$1"
  local backup="${file}.bak"

  [[ -f "$backup" ]] || die "Backup not found: $backup"
  log "Restore original file: ${file}"
  mv -f "$backup" "$file"

  if grep -q "__archViolation" "$file"; then
    die "Restore failed: __archViolation still present in $file"
  fi
}

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
      if (injected==0) {
        exit 42
      }
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

cleanup() {
  log "Cleanup temp dir: ${WORK_DIR}"
  rm -rf "$WORK_DIR"
}
trap cleanup EXIT

main() {
  require_file "$CODEGEN_JAR"
  require_cmd "$JAVA_BIN"

  log "Workspace: ${WORK_DIR}"
  log "Using CODEGEN_JAR: ${CODEGEN_JAR}"

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

  local std_controller
  std_controller="$(find_controller_file "$std_dir")"
  inject_standard_violation_controller_depends_on_repository "$std_dir" "$std_controller"
  run_verify_expect_fail "$std_dir" "STD violation"
  restore_file "$std_controller"
  run_verify "$std_dir" "STD fixed"

  log "DONE: Proof completed successfully (GREEN → RED → GREEN)"
}

main "$@"