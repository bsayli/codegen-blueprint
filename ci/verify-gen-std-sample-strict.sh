#!/usr/bin/env bash
set -euo pipefail

LAYOUT="standard"
SAMPLE_CODE="basic"
GUARDRAILS="strict"
DESCRIPTION="Verify generated project: standard (layered) layout, strict guardrails, basic sample"
DEPENDENCIES="web"

source "$(dirname "$0")/common-verify-generated.sh"