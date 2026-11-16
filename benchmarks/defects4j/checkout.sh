#!/usr/bin/env bash
# Check out and compile the buggy (b) and fixed (f) version of every bug in bugs.csv.
#   ./checkout.sh                       # all bugs, into ./work
#   ./checkout.sh Lang-59 Collections-4 # only these bugs (a bare number means Collections-<n>)
#   ./checkout.sh --verify              # also run each bug's trigger tests: must fail on b and pass on f
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
D4J="$HERE/d4j.sh"
WORK="${WORK:-$HERE/work}"

verify=0; bugs=()
for a in "$@"; do
  if [[ "$a" == "--verify" ]]; then verify=1
  elif [[ "$a" =~ ^[0-9]+$ ]]; then bugs+=("Collections-$a")
  else bugs+=("$a"); fi
done
(( ${#bugs[@]} )) || mapfile -t bugs < <(tail -n +2 "$HERE/bugs.csv" | cut -d, -f1)

mkdir -p "$WORK"
status=0
for bug in "${bugs[@]}"; do
  project="${bug%-*}"; id="${bug##*-}"
  for v in b f; do
    dir="$WORK/$bug$v"
    if [[ ! -d "$dir" ]]; then
      "$D4J" checkout -p "$project" -v "$id$v" -w "$dir"
    fi
    "$D4J" compile -w "$dir"

    if (( verify )); then
      fails=0
      # Defects4J prints the list without a final newline, so read it with mapfile, not `while read`.
      mapfile -t triggers < <("$D4J" export -p tests.trigger -w "$dir" 2>/dev/null; echo)
      for t in "${triggers[@]}"; do
        [[ -z "$t" ]] && continue
        "$D4J" test -w "$dir" -t "$t" >/dev/null 2>&1 </dev/null
        n=$(grep -c '^---' "$dir/failing_tests" 2>/dev/null || true)
        fails=$(( fails + ${n:-0} ))
      done
      if [[ "$v" == b && $fails -gt 0 ]] || [[ "$v" == f && $fails -eq 0 ]]; then
        echo "OK   $bug$v  (failing trigger tests: $fails)"
      else
        echo "FAIL $bug$v  (failing trigger tests: $fails)"; status=1
      fi
    fi
  done
done
exit $status
