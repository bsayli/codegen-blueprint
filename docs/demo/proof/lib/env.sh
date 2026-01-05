# shellcheck shell=bash
# Environment + path initialization (sourced)
# -------------------------
# Config (env-overridable)
# -------------------------
GROUP_ID="${GROUP_ID:-io.github.blueprintplatform}"
PACKAGE_NAME="${PACKAGE_NAME:-io.github.blueprintplatform.greeting}"
JAVA_BIN="${JAVA_BIN:-java}"

KEEP_WORK_DIR="${KEEP_WORK_DIR:-0}"
PROOF_EXPORT_DIR="${PROOF_EXPORT_DIR:-}"

# Proof output root (in-repo by default)
# Can be overridden to write elsewhere (e.g., CI workspace)
PROOF_OUTPUT_ROOT="${PROOF_OUTPUT_ROOT:-}"

# -------------------------
# Globals (late-init)
# -------------------------
ROOT_DIR="${ROOT_DIR:-}"
CODEGEN_JAR="${CODEGEN_JAR:-}"

LIB_DIR="${LIB_DIR:-}"       # docs/demo/proof/lib
PROOF_DIR="${PROOF_DIR:-}"   # docs/demo/proof
DEMO_DIR="${DEMO_DIR:-}"     # docs/demo

WORK_DIR="${WORK_DIR:-}"
OUT_DIR="${OUT_DIR:-}"
BACKUP_DIR="${BACKUP_DIR:-}"

RUN_TS="${RUN_TS:-}"
RUN_DIR="${RUN_DIR:-}"

LOG_DIR="${LOG_DIR:-}"
EXCERPT_DIR="${EXCERPT_DIR:-}"

ENV_FILE="${ENV_FILE:-}"
SUMMARY_FILE="${SUMMARY_FILE:-}"


# -------------------------
# Repo root detection
# -------------------------
ensure_repo_root() {
  # 1) Prefer git if available and we are inside a repo
  if command -v git >/dev/null 2>&1; then
    local git_root
    git_root="$(git rev-parse --show-toplevel 2>/dev/null || true)"
    if [[ -n "$git_root" && -d "$git_root" ]]; then
      echo "$git_root"
      return 0
    fi
  fi

  # 2) Fallback: walk up from this lib dir and find a plausible repo root
  # Heuristics: .git directory OR pom.xml at root
  local dir
  dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

  for _ in 1 2 3 4 5 6 7 8; do
    if [[ -d "${dir}/.git" || -f "${dir}/pom.xml" ]]; then
      echo "$dir"
      return 0
    fi
    local parent
    parent="$(cd "${dir}/.." && pwd)"
    [[ "$parent" == "$dir" ]] && break
    dir="$parent"
  done

  die "Could not infer repository root. Run from inside the repo or install git."
}

# -------------------------
# Context init
# -------------------------
init_runtime_context() {
  # This file lives under: docs/demo/proof/lib
  LIB_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
  PROOF_DIR="$(cd "${LIB_DIR}/.." && pwd)"   # docs/demo/proof
  DEMO_DIR="$(cd "${PROOF_DIR}/.." && pwd)"  # docs/demo

  ROOT_DIR="$(ensure_repo_root)"

  # Default JAR location if not provided via env
  CODEGEN_JAR="${CODEGEN_JAR:-${ROOT_DIR}/target/codegen-blueprint-1.0.0.jar}"

  # Default proof-output root (keep stable under docs/demo/)
  PROOF_OUTPUT_ROOT="${PROOF_OUTPUT_ROOT:-${DEMO_DIR}/proof-output}"

  # Workspace (temp)
  WORK_DIR="$(mktemp -d -t codegen-proof-XXXXXX)"
  OUT_DIR="${WORK_DIR}/out"

  # Backup store OUTSIDE generated project tree (temp)
  BACKUP_DIR="${WORK_DIR}/.backup"
  mkdir -p "${BACKUP_DIR}"

  # Run output dirs (in-repo by default)
  RUN_TS="$(date +%Y%m%d-%H%M%S)"
  RUN_DIR="${PROOF_OUTPUT_ROOT}/${RUN_TS}"

  LOG_DIR="${RUN_DIR}/logs"
  EXCERPT_DIR="${RUN_DIR}/excerpts"
  mkdir -p "${LOG_DIR}" "${EXCERPT_DIR}"

  ENV_FILE="${RUN_DIR}/env.txt"
  SUMMARY_FILE="${RUN_DIR}/proof-summary.txt"
}