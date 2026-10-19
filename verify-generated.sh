#!/usr/bin/env bash
# Builds the generator, regenerates every registered library through the real
# pipeline (checks -> IR -> real SPF -> solved input vectors -> JUnit), and
# fails loudly on any failure that is not an already-known, documented
# outcome.
#
#   - GENERATED : the library produced a JUnit class; prints pass/fail counts.
#       A JUnit FAILURE here is not itself a script failure — arraylib is
#       EXPECTED to show failures (a real contract bug, see
#       IMPLEMENTATION_NOTES.md), so this script reports every failure loudly
#       but only turns the exit code non-zero for a library that was not
#       already known to fail when this script was last updated.
#   - SKIPPED        : an unsupported library (reports its reason) — fine.
#   - INFEASIBLE      : SPF found no feasible path — fine, as long as the
#       library is not one of the ones this script expects to generate.
#   - EXECUTION_ERROR : SPF could not execute a call at all (mathlib/gcd's
#       confirmed jpf-symbc limitation) — fine for mathlib specifically.
#   - COMPILE_ERROR / INPUT_ERROR / an unexpected status for any other
#       library, or a library that was expected to generate but didn't : FAIL.
#
# Prerequisite: scripts/setup-spf.sh has been run at least once.
#
# Run from the repository root:  ./verify-generated.sh
set -uo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
PROJECT="$ROOT/pl-platform-testing"

# Libraries this script expects to produce a GENERATED JUnit class, and the
# ones whose status is something else for a documented reason (see
# IMPLEMENTATION_NOTES.md for why).
EXPECT_GENERATED="stack queue arraylib linkedlist heap set hashmap bst graph lrucache"
EXPECT_EXECUTION_ERROR="mathlib"
EXPECT_SKIPPED="taskqueue ticketservice orderservice"
# A JUnit FAILURE is expected (a found contract bug, not a generator bug) for:
EXPECT_JUNIT_FAILURES="arraylib"

cd "$PROJECT"
echo "== mvn -o install (pl-platform-testing) =="
mvn -q -o install -DskipTests || { echo "BUILD FAILED"; exit 1; }

CP="target/classes"
for jar in $(find "$HOME/.m2/repository/gov/nasa/jpf" -name '*.jar' 2>/dev/null); do CP="$CP:$jar"; done
JUNIT_JAR=$(find "$HOME/.m2/repository/junit" -name 'junit-4.13.1.jar' 2>/dev/null | head -1)
HAMCREST_JAR=$(find "$HOME/.m2/repository/org/hamcrest" -name 'hamcrest-core-1.3.jar' 2>/dev/null | head -1)
CP="$CP:$JUNIT_JAR:$HAMCREST_JAR"

OUT="$(mktemp)"
echo "== running GenatcMain for every registered library (real SPF) =="
java -cp "$CP" in.ac.iiitb.plproject.atc.GenatcMain 2>&1 | tee "$OUT"

status=0
for key in $EXPECT_GENERATED; do
    line="$(grep -E "^${key}[[:space:]]" "$OUT")"
    if [ -z "$line" ]; then
        echo "FAIL: $key did not appear in the summary at all"
        status=1
        continue
    fi
    if ! echo "$line" | grep -q "GENERATED"; then
        echo "FAIL: $key expected GENERATED, got: $line"
        status=1
        continue
    fi
    if echo "$line" | grep -q "JUnit failure"; then
        case " $EXPECT_JUNIT_FAILURES " in
            *" $key "*)
                echo "OK (expected JUnit failures, a documented contract bug): $line" ;;
            *)
                echo "FAIL: $key has UNEXPECTED JUnit failures: $line"
                status=1 ;;
        esac
    else
        echo "OK: $line"
    fi
done

for key in $EXPECT_EXECUTION_ERROR; do
    line="$(grep -E "^${key}[[:space:]]" "$OUT")"
    if echo "$line" | grep -q "EXECUTION_ERROR"; then
        echo "OK (documented jpf-symbc limitation): $line"
    else
        echo "FAIL: $key expected EXECUTION_ERROR, got: $line"
        status=1
    fi
done

for key in $EXPECT_SKIPPED; do
    line="$(grep -E "^${key}[[:space:]]" "$OUT")"
    if echo "$line" | grep -q "SKIPPED"; then
        echo "OK (unsupported by design): $line"
    else
        echo "FAIL: $key expected SKIPPED, got: $line"
        status=1
    fi
done

rm -f "$OUT"
if [ "$status" -ne 0 ]; then
    echo
    echo "VERIFY FAILED — see FAIL lines above."
else
    echo
    echo "VERIFY OK — every library matches its documented, previously-observed status."
fi
exit $status
