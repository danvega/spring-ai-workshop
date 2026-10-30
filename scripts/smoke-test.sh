#!/usr/bin/env bash
# Hits every demo endpoint once and reports pass or fail.
# Usage: start the app, then ./scripts/smoke-test.sh
set -u
BASE="http://localhost:8080"
pass=0; fail=0; failed=()

check() {
  local name="$1"; local path="$2"; local timeout="${3:-90}"
  local start=$(date +%s)
  local code
  code=$(curl -s -o /tmp/smoke-body -w "%{http_code}" --max-time "$timeout" "$BASE$path")
  local secs=$(( $(date +%s) - start ))
  local size=$(wc -c < /tmp/smoke-body | tr -d ' ')
  if [ "$code" = "200" ] && [ "$size" -gt 0 ]; then
    printf "PASS  %-28s %ss  %s bytes\n" "$name" "$secs" "$size"
    pass=$((pass+1))
  else
    printf "FAIL  %-28s HTTP %s after %ss\n" "$name" "$code" "$secs"
    failed+=("$name")
    fail=$((fail+1))
  fi
}

check "chat /"                  "/?message=Tell%20me%20a%20dad%20joke%20about%20dogs"
check "chat /chat"              "/chat"
check "chat /chat-response"     "/chat-response"
check "chat /jokes-by-topic"    "/jokes-by-topic?topic=coffee"
check "chat /jokes-with-response" "/jokes-with-response"
check "chat-model /fact"        "/chat-model/fact"
check "chat-model /taglines"    "/chat-model/taglines"
check "prompt /posts/new"       "/posts/new"
check "output /vacation/structured"   "/vacation/structured"
check "output /vacation/unstructured" "/vacation/unstructured"
check "options /options"        "/options"
check "memory /memory"          "/memory?message=My%20name%20is%20Dan%20and%20I%20live%20in%20Cleveland"
check "advisor /advisor"        "/advisor"
check "advisor /pii-masking"    "/pii-masking"
check "advisor /support on"     "/support"
check "advisor /support off"    "/support?message=write%20me%20a%20python%20script"
check "async /stream"           "/stream" 120
check "byod /models"            "/models"
check "byod /stuff-the-prompt"  "/models/stuff-the-prompt"
check "rag /rag/models"         "/rag/models"
check "guards /input-validation" "/input-validation"
check "guards /fact-checking"   "/fact-checking"
check "tools /tools"            "/tools"
check "tools /tasks"            "/tasks?message=Create%20a%20task%20to%20buy%20milk"
check "tools /weather/alerts"   "/weather/alerts?message=Any%20weather%20alerts%20in%20Ohio%3F"
check "router /router"          "/router?message=What%20is%20the%20capital%20of%20Ohio"
check "multimodal /image-to-text"  "/image-to-text" 120
check "multimodal /speak"       "/speak" 120
check "multimodal /generate-image" "/generate-image" 180

echo
echo "$pass passed, $fail failed"
if [ "$fail" -gt 0 ]; then printf 'FAILED: %s\n' "${failed[@]}"; exit 1; fi
