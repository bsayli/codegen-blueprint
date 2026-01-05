#!/usr/bin/env bash
set -euo pipefail

# Entrypoint: flow only + trap/cleanup

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# shellcheck source=docs/demo/proof/lib/env.sh
source "${SCRIPT_DIR}/lib/env.sh"

# shellcheck source=docs/demo/proof/lib/io.sh
source "${SCRIPT_DIR}/lib/io.sh"

# shellcheck source=docs/demo/proof/lib/project_ops.sh
source "${SCRIPT_DIR}/lib/project_ops.sh"

cleanup() {
  if [[ "${KEEP_WORK_DIR}" == "1" ]]; then
    log "KEEP_WORK_DIR=1 -> workspace preserved: ${WORK_DIR}"
    return 0
  fi
  log "Cleanup temp dir: ${WORK_DIR}"
  rm -rf "$WORK_DIR"
}

on_exit() {
  local rc=$?

  # Best-effort artifacts even on failure
  if [[ -n "${ROOT_DIR:-}" && -n "${CODEGEN_JAR:-}" && -n "${ENV_FILE:-}" ]]; then
    collect_env_metadata || true
  fi

  if [[ -n "${SUMMARY_FILE:-}" ]]; then
    finalize_proof_status "$rc" || true
  fi

  export_proof_artifacts || true
  cleanup || true

  exit "$rc"
}
trap on_exit EXIT

main() {
  init_runtime_context

  require_file "$CODEGEN_JAR"
  require_cmd "$JAVA_BIN"

  log "Workspace (temp): ${WORK_DIR}"
  log "Using CODEGEN_JAR: ${CODEGEN_JAR}"
  log "Proof output (run): ${RUN_DIR}"
  log "  logs:     ${LOG_DIR}"
  log "  excerpts: ${EXCERPT_DIR}"

  if [[ -n "$PROOF_EXPORT_DIR" ]]; then
    log "PROOF_EXPORT_DIR: ${PROOF_EXPORT_DIR}"
  fi

  collect_env_metadata

  local hex_dir std_dir
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
  log "Tip: set KEEP_WORK_DIR=1 to inspect generated projects under ${WORK_DIR}"
}

main "$@"