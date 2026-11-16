#!/usr/bin/env bash
# Run the baseline test generators (Randoop, EvoSuite) on the same Defects4J bugs as TestGen,
# with Defects4J's own protocol:
#   1. gen_tests.pl          generate tests for the class the bug touches, on the FIXED version
#   2. fix_test_suite.pl     remove tests that fail on the fixed version (false alarms)
#   3. run_bug_detection.pl  run the rest on the BUGGY version: Fail = bug detected
#
#   ./baselines.sh                                  # both tools, all bugs in bugs.csv, budget 60 s, seed 1
#   ./baselines.sh -t randoop -b 180 -s "1 2 3" Collections-15 Lang-59
# Options: -t randoop|evosuite|both   -b budget in seconds per class   -s "seed list"
# Results: baselines/results.csv (tool,bug,seed,result) and a summary at the end.
set -uo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
D4J_HOME="${D4J_HOME:-$HOME/defects4j}"
OUT="$HERE/baselines"

tools="randoop evosuite"; budget=60; seeds="1"
while getopts "t:b:s:" o; do
  case $o in
    t) [[ "$OPTARG" == both ]] && tools="randoop evosuite" || tools="$OPTARG" ;;
    b) budget="$OPTARG" ;;
    s) seeds="$OPTARG" ;;
    *) sed -n '2,12p' "$0"; exit 64 ;;
  esac
done
shift $((OPTIND - 1))

# Same environment as d4j.sh: Java 11 for this script only, Defects4J on PATH.
if [[ -z "${JAVA11_HOME:-}" ]]; then
  for d in /usr/lib/jvm/java-11-openjdk-*; do [[ -x "$d/bin/javac" ]] && { JAVA11_HOME="$d"; break; }; done
fi
[[ -x "${JAVA11_HOME:-}/bin/javac" ]] || { echo "baselines.sh: no Java 11 JDK found; set JAVA11_HOME" >&2; exit 1; }
export JAVA_HOME="$JAVA11_HOME" PATH="$D4J_HOME/framework/bin:$D4J_HOME/framework/util:$JAVA11_HOME/bin:$PATH"
export TZ="${TZ:-America/Los_Angeles}"
# EvoSuite's runtime touches AWT; the headless JDK has no desktop AWT library (libawt_xawt.so),
# so every EvoSuite suite would be "Broken". Headless mode avoids loading it. Every JVM that
# Defects4J starts (ant, the forked test runs) inherits this.
export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:+$JAVA_TOOL_OPTIONS }-Djava.awt.headless=true"

bugs=()
for a in "$@"; do if [[ "$a" =~ ^[0-9]+$ ]]; then bugs+=("Collections-$a"); else bugs+=("$a"); fi; done
(( ${#bugs[@]} )) || mapfile -t bugs < <(tail -n +2 "$HERE/bugs.csv" | cut -d, -f1)

mkdir -p "$OUT"
[[ -f "$OUT/results.csv" ]] || echo "tool,bug,seed,budget,result" > "$OUT/results.csv"

for tool in $tools; do
  for bug in "${bugs[@]}"; do
    project="${bug%-*}"; id="${bug##*-}"
    for seed in $seeds; do
      run="$OUT/runs/$tool/$bug/b$budget-s$seed"
      rm -rf "$run"; mkdir -p "$run/tmp"
      suites="$run/suites"
      echo "== $tool $bug seed $seed (budget ${budget}s) =="

      # 1. generate on the fixed version
      gen_tests.pl -g "$tool" -p "$project" -v "${id}f" -n "$seed" -s "$seed" -b "$budget" \
          -o "$suites" -t "$run/tmp" > "$run/gen.log" 2>&1
      suite_dir="$suites/$project/$tool/$seed"
      if ! ls "$suite_dir"/*.tar.bz2 >/dev/null 2>&1; then
        echo "   no test suite generated (see $run/gen.log)"
        echo "$tool,$bug,$seed,$budget,NO_SUITE" >> "$OUT/results.csv"; continue
      fi

      # 2. drop tests that fail on the fixed version
      fix_test_suite.pl -p "$project" -d "$suite_dir" -t "$run/tmp" > "$run/fix.log" 2>&1

      # 3. run what is left on the buggy version
      run_bug_detection.pl -p "$project" -d "$suite_dir" -o "$run/detect" -t "$run/tmp" > "$run/detect.log" 2>&1
      row=$(tail -n +2 "$run/detect/bug_detection" 2>/dev/null | head -1)
      # columns: project,version,suite_source,test_id,result(Pass|Fail|Broken),num_trigger_tests
      result=$(echo "$row" | awk -F, '{print $5}')
      [[ -n "$result" ]] || result="ERROR"
      echo "   $result   ($row)"
      echo "$tool,$bug,$seed,$budget,$result" >> "$OUT/results.csv"
      rm -rf "$run/tmp"
    done
  done
done

echo
echo "================ summary (budget ${budget}s; Fail = bug detected) ================"
awk -F, -v b="$budget" 'NR>1 && $4==b { k=$1" "$2; n[k]++; if ($5=="Fail") d[k]++ }
  END { for (k in n) printf "%-10s %-16s detected in %d/%d runs\n", substr(k,1,index(k," ")-1), substr(k,index(k," ")+1), d[k], n[k] }' \
  "$OUT/results.csv" | sort
