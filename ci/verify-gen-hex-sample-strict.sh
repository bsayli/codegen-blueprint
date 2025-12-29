#!/usr/bin/env bash
set -euo pipefail

LAYOUT="hexagonal"
SAMPLE_CODE="basic"
GUARDRAILS="strict"
DESCRIPTION="Verify generated project: hexagonal layout, strict guardrails, basic sample"
DEPENDENCIES="web"

source "$(dirname "$0")/common-verify-generated.sh"