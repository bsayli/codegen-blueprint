#!/usr/bin/env bash
set -euo pipefail

export LAYOUT="hexagonal"
export GUARDRAILS="strict"
export SAMPLE_CODE="none"
export DESCRIPTION="Verify generated project: hexagonal layout, strict guardrails, no sample"
export DEPENDENCIES=""

# shellcheck source=ci/common-verify-generated.sh
source "$(dirname "$0")/common-verify-generated.sh"