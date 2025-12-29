#!/usr/bin/env bash
set -euo pipefail

LAYOUT="standard"
SAMPLE_CODE="none"
GUARDRAILS="strict"
DESCRIPTION="Verify generated project: standard (layered) layout, strict guardrails, no sample"
DEPENDENCIES=""

source "$(dirname "$0")/common-verify-generated.sh"