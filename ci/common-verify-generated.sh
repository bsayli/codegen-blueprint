#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(pwd)"
WORK_DIR="$(mktemp -d)"
TARGET_DIR="$WORK_DIR/out"

JAVA_VERSION="${JAVA_VERSION:-21}"
BOOT_VERSION="${BOOT_VERSION:-3.5}"
ARTIFACT_ID="${ARTIFACT_ID:-greeting}"
PACKAGE_NAME="${PACKAGE_NAME:-io.github.blueprintplatform.greeting}"

LAYOUT="${LAYOUT:?LAYOUT is required (hexagonal|standard)}"
GUARDRAILS="${GUARDRAILS:-strict}"
SAMPLE_CODE="${SAMPLE_CODE:?SAMPLE_CODE is required (none|basic)}"
DESCRIPTION="${DESCRIPTION:-Generated project verification}"
DEPENDENCIES="${DEPENDENCIES:-}"

cleanup() {
  rm -rf "$WORK_DIR" || true
}
trap cleanup EXIT

mkdir -p "$TARGET_DIR"

JAR_PATH="$(ls -1 "$ROOT_DIR"/target/codegen-blueprint-*.jar 2>/dev/null | head -n 1)"
if [ -z "${JAR_PATH:-}" ] || [ ! -f "$JAR_PATH" ]; then
  echo "Jar not found under: $ROOT_DIR/target"
  ls -la "$ROOT_DIR/target" || true
  exit 1
fi

CLI_ARGS=(
  --spring.main.web-application-type=none
  --cli springboot
  --group-id io.github.blueprintplatform
  --artifact-id "$ARTIFACT_ID"
  --name Greeting
  --description "$DESCRIPTION"
  --package-name "$PACKAGE_NAME"
  --layout "$LAYOUT"
  --guardrails "$GUARDRAILS"
  --sample-code "$SAMPLE_CODE"
  --java "$JAVA_VERSION"
  --boot "$BOOT_VERSION"
  --target-dir "$TARGET_DIR"
)

if [ -n "${DEPENDENCIES:-}" ]; then
  for dep in $DEPENDENCIES; do
    CLI_ARGS+=(--dependency "$dep")
  done
fi

java -jar "$JAR_PATH" "${CLI_ARGS[@]}"

ZIP="$TARGET_DIR/$ARTIFACT_ID.zip"
if [ ! -f "$ZIP" ]; then
  echo "Expected zip not found: $ZIP"
  find "$TARGET_DIR" -maxdepth 3 -type f -print || true
  exit 1
fi

unzip -q "$ZIP" -d "$TARGET_DIR/unzipped"

GEN_DIR="$TARGET_DIR/unzipped/$ARTIFACT_ID"
if [ ! -d "$GEN_DIR" ]; then
  echo "Expected generated project dir not found: $GEN_DIR"
  find "$TARGET_DIR/unzipped" -maxdepth 4 -type d -print || true
  exit 1
fi

cd "$GEN_DIR"

find_mockito_agent() {
  local repo="${HOME}/.m2/repository"
  if [ -d "$repo/org/mockito/mockito-core" ]; then
    ls -1 "$repo/org/mockito/mockito-core"/*/mockito-core-*.jar 2>/dev/null | sort -V | tail -n 1 || true
  fi
}

MOCKITO_AGENT="$(find_mockito_agent)"
MVN_ARGS=(-q -ntp verify)

if [ -n "${MOCKITO_AGENT:-}" ] && [ -f "${MOCKITO_AGENT:-}" ]; then
  MVN_ARGS=(-q -ntp -DargLine="-javaagent:${MOCKITO_AGENT}" verify)
fi

if [ -f "./mvnw" ]; then
  chmod +x ./mvnw
  ./mvnw "${MVN_ARGS[@]}"
else
  echo "[WARN] mvnw not found, falling back to system Maven (mvn)"
  mvn "${MVN_ARGS[@]}"
fi