#!/usr/bin/env bash
set -euo pipefail

export LAYOUT="standard"
export GUARDRAILS="strict"
export SAMPLE_CODE="basic"
export DESCRIPTION="Verify generated project: standard (layered) layout, strict guardrails, basic sample"
export DEPENDENCIES="web"

# shellcheck source=ci/common-verify-generated.sh
source "$(dirname "$0")/common-verify-generated.sh"