# shellcheck shell=bash
# Logging, excerpts, env capture, summary/export (sourced)

log() { printf "\n\033[1m[%s]\033[0m %s\n" "$(date +%H:%M:%S)" "$*" >&2; }
die() { printf "\n\033[31mERROR:\033[0m %s\n" "$*" >&2; exit 1; }

require_file() { [[ -f "$1" ]] || die "Required file not found: $1"; }

require_cmd() {
  # If user passes JAVA_BIN as a path, accept it if executable
  if [[ -x "$1" ]]; then
    return 0
  fi
  command -v "$1" >/dev/null 2>&1 || die "Required command not found on PATH (or not executable): $1"
}

normalize_label() {
  local s="$1"
  # filesystem-safe: spaces->_, keep alnum/_.-
  s="${s// /_}"
  s="$(printf '%s' "$s" | tr -cd '[:alnum:]_.-')"
  [[ -n "$s" ]] || s="step"
  echo "$s"
}

log_file_for_label() {
  local label="$1"
  echo "${LOG_DIR}/$(normalize_label "$label").log"
}

excerpt_file_for_label() {
  local label="$1"
  echo "${EXCERPT_DIR}/$(normalize_label "$label").excerpt.txt"
}

record_step() {
  local label="$1" status="$2" log_file="$3" excerpt_file="${4:-}"
  local rel_log rel_excerpt
  rel_log="${log_file#"${RUN_DIR}"/}"
  rel_excerpt="${excerpt_file#"${RUN_DIR}"/}"
  STEPS+=("${label}|${status}|${rel_log}|${rel_excerpt}")
}

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
    echo "  excerpts_dir: ${EXCERPT_DIR}"
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

  if [[ $exit_code -eq 0 ]]; then
    log "PROOF RESULT: PASS (GREEN → RED → GREEN)"
  else
    log "PROOF RESULT: FAIL (exit_code=${exit_code})"
  fi
}

export_proof_artifacts() {
  [[ -n "$PROOF_EXPORT_DIR" ]] || return 0

  local export_dir="$PROOF_EXPORT_DIR"
  mkdir -p "$export_dir"

  cp -f "$ENV_FILE" "$export_dir/" 2>/dev/null || true
  cp -f "$SUMMARY_FILE" "$export_dir/" 2>/dev/null || true
  cp -f "${LOG_DIR}/"*.log "$export_dir/" 2>/dev/null || true
  cp -f "${EXCERPT_DIR}/"*.excerpt.txt "$export_dir/" 2>/dev/null || true

  log "Exported proof artifacts to: ${export_dir}"
}