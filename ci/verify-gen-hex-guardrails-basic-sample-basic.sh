#!/usr/bin/env bash
set -euo pipefail

export LAYOUT="hexagonal"
export GUARDRAILS="basic"
export SAMPLE_CODE="basic"
export DESCRIPTION="Verify generated project: hexagonal layout, basic guardrails, basic sample"
export DEPENDENCIES="web"

# shellcheck source=ci/common-verify-generated.sh
source "$(dirname "$0")/common-verify-generated.sh"