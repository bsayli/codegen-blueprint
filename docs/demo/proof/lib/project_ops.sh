# shellcheck shell=bash
# Generation, maven verify, backup/restore, injectors (sourced)

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

  [[ -n "${OUT_DIR:-}" ]] || die "OUT_DIR is not set (did init_runtime_context run?)"
  [[ -n "${CODEGEN_JAR:-}" ]] || die "CODEGEN_JAR is not set"
  [[ -n "${JAVA_BIN:-}" ]] || die "JAVA_BIN is not set"

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
  rel="${rel#"${WORK_DIR}"/}"
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

  local rel="${java_file#"${project_dir}"/src/main/java/}"
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