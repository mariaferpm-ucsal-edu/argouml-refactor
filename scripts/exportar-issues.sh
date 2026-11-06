#!/usr/bin/env bash
set -euo pipefail
cd "$(git rev-parse --show-toplevel)"

if [ -z "${SONAR_TOKEN:-}" ]; then
  read -r -s -p "SONAR_TOKEN: " SONAR_TOKEN
  echo
fi
[ -n "$SONAR_TOKEN" ] || { echo "Token vazio. Abortando."; exit 1; }

KEY=mariaferpm-ucsal-edu_argouml-refactor
OUT=docs/baseline
TAG="${1:?Informe a fase: antes ou depois}"
TMP=$(mktemp -d)
mkdir -p "$OUT"

p=1
while true; do
  curl -sS -f -u "$SONAR_TOKEN:" \
    "https://sonarcloud.io/api/issues/search?componentKeys=$KEY&branch=master&resolved=false&ps=500&p=$p" \
    -o "$TMP/p$p.json"
  total=$(jq '.paging.total' "$TMP/p$p.json")
  echo "página $p: total=$total"
  [ $(( p * 500 )) -ge "$total" ] && break
  p=$(( p + 1 ))
done

jq -s '{total: .[0].paging.total, issues: [.[].issues[]]}' "$TMP"/p*.json \
  > "$OUT/issues-baseline-$TAG-completo.json"

jq -r '["key","regra","severidade","tipo","arquivo","linha","esforco","status"],
  (.issues[] | [.key,.rule,.severity,.type,.component,(.line//""),(.effort//""),.status]) | @csv' \
  "$OUT/issues-baseline-$TAG-completo.json" > "$OUT/issues-baseline-$TAG-completo.csv"

rm -rf "$TMP"
echo "Exportados: $(jq '.issues|length' "$OUT/issues-baseline-$TAG-completo.json") issues"