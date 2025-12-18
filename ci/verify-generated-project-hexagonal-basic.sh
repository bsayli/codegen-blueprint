#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(pwd)"
WORK_DIR="$(mktemp -d)"
TARGET_DIR="$WORK_DIR/out"

JAVA_VERSION="${JAVA_VERSION:-21}"
BOOT_VERSION="${BOOT_VERSION:-3.5}"
ARTIFACT_ID="${ARTIFACT_ID:-greeting}"
PACKAGE_NAME="${PACKAGE_NAME:-io.github.blueprintplatform.greeting}"

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

java -jar "$JAR_PATH" \
  --spring.main.web-application-type=none \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id "$ARTIFACT_ID" \
  --name Greeting \
  --description "Greeting sample built with hexagonal architecture" \
  --package-name "$PACKAGE_NAME" \
  --layout hexagonal \
  --enforcement basic \
  --sample-code basic \
  --java "$JAVA_VERSION" \
  --boot "$BOOT_VERSION" \
  --dependency web \
  --dependency data_jpa \
  --dependency actuator \
  --target-dir "$TARGET_DIR"

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

if [ -f "./mvnw" ]; then
  chmod +x ./mvnw
  ./mvnw -q -ntp verify
else
  mvn -q -ntp verify
fi