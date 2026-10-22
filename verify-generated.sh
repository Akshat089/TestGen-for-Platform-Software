#!/usr/bin/env bash
# Builds the generator, regenerates every registered library through the
# contract-only pipeline (checks -> IR -> symbolic driver built from the spec
# alone -> real SPF -> one solved input vector per sequence -> JUnit), runs
# each generated JUnit class against the unmodified library, compares the
# generated sources with the golden copies, and fails loudly on anything that
# is not the expected outcome:
#
#   - GENERATED with every JUnit test passing, for every supported library;
#   - SKIPPED for the three libraries that need a returned id threaded into a
#     later call (taskqueue, ticketservice, orderservice);
#   - the generated GeneratedATCs.java / GeneratedATCsTest.java identical to
#     pl-platform-testing/golden/<key>/ (regenerate those with --update-golden).
#
# Anything else — INCORRECT_SEQUENCE, UNSAT_POSTCONDITION, SOLVER_ERROR,
# TIMEOUT, COMPILE_ERROR, INPUT_ERROR, a failing generated test, or a golden
# difference — is a FAIL. It also checks that no generated driver mentions the
# library (Helper) and that no SPF run had the library on its classpath.
#
# Prerequisite: scripts/setup-spf.sh has been run at least once.
#
# Run from the repository root:  ./verify-generated.sh [--update-golden]
set -uo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
PROJECT="$ROOT/pl-platform-testing"
UPDATE_GOLDEN=0
[ "${1:-}" = "--update-golden" ] && UPDATE_GOLDEN=1

EXPECT_GENERATED="stack queue mathlib arraylib linkedlist heap set hashmap bst graph lrucache"
EXPECT_SKIPPED="taskqueue ticketservice orderservice"

cd "$PROJECT"
echo "== mvn -o install (pl-platform-testing) =="
mvn -q -o install -DskipTests || { echo "BUILD FAILED"; exit 1; }

JUNIT_JAR=$(find "$HOME/.m2/repository/junit" -name 'junit-4.13.1.jar' 2>/dev/null | head -1)
HAMCREST_JAR=$(find "$HOME/.m2/repository/org/hamcrest" -name 'hamcrest-core-1.3.jar' 2>/dev/null | head -1)
CP="target/classes:$JUNIT_JAR:$HAMCREST_JAR"

OUT="$(mktemp)"
echo "== running GenatcMain for every registered library =="
java -cp "$CP" in.ac.iiitb.plproject.atc.GenatcMain 2>&1 | grep -v '^\[WARN\]' | tee "$OUT"

status=0
GEN=in/ac/iiitb/plproject/atc/generated
for key in $EXPECT_GENERATED; do
    line="$(grep -E "^${key}[[:space:]]+[A-Z_]+[[:space:]]+tests=" "$OUT")"
    if [ -z "$line" ]; then
        echo "FAIL: $key did not appear in the summary at all"
        status=1
        continue
    elif ! echo "$line" | grep -q "GENERATED"; then
        echo "FAIL: $key expected GENERATED, got: $line"
        status=1
        continue
    elif echo "$line" | grep -q "JUnit failure"; then
        echo "FAIL: $key has failing generated tests: $line"
        status=1
    else
        echo "OK: $line"
    fi

    driver="outputs/$key/$GEN/GeneratedATCs.java"
    if grep -q "Helper" "$driver"; then
        echo "FAIL: $key's symbolic driver mentions the library (Helper)"
        status=1
    fi
    if grep -E '^classpath = ' "outputs/$key/spf-work/run.jpf" | grep -qE 'libraries|Helper'; then
        echo "FAIL: $key's SPF classpath contains the library"
        status=1
    fi

    for f in GeneratedATCs.java GeneratedATCsTest.java; do
        if [ "$UPDATE_GOLDEN" -eq 1 ]; then
            mkdir -p "golden/$key" && cp "outputs/$key/$GEN/$f" "golden/$key/$f"
        elif ! diff -q "golden/$key/$f" "outputs/$key/$GEN/$f" > /dev/null 2>&1; then
            echo "FAIL: $key's $f differs from golden/$key/$f"
            status=1
        fi
    done
done

for key in $EXPECT_SKIPPED; do
    line="$(grep -E "^${key}[[:space:]]+[A-Z_]+[[:space:]]+tests=" "$OUT")"
    if echo "$line" | grep -q "SKIPPED"; then
        echo "OK (unsupported by design): $key"
    else
        echo "FAIL: $key expected SKIPPED, got: $line"
        status=1
    fi
done

rm -f "$OUT"
[ "$UPDATE_GOLDEN" -eq 1 ] && echo "golden files updated under pl-platform-testing/golden/"
if [ "$status" -ne 0 ]; then
    echo
    echo "VERIFY FAILED — see FAIL lines above."
else
    echo
    echo "VERIFY OK — every library matches its expected status and its golden files."
fi
exit $status
