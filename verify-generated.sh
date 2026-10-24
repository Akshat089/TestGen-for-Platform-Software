#!/usr/bin/env bash
# Builds the generator and regenerates every registered library through the
# TestGen pipeline (checks -> IR -> abstract test case -> genCTC: each round
# rewriteATC, then symex on real SPF with every ready call running on the real
# library -> the concrete test case -> JUnit), runs each generated JUnit class
# against the unmodified library, compares the outputs with the golden copies,
# and fails loudly on anything that is not the expected outcome:
#
#   - GENERATED with every JUnit test passing, for every library;
#   - the generation trace ATC.txt (ATC, t' after every genCTC round, concrete
#     test) and GeneratedATCsTest.java identical to
#     pl-platform-testing/golden/<key>/, for every library that has a golden
#     directory (regenerate those with --update-golden);
#   - the black-box guard never fired in any SPF run (no symbolic value
#     reached the library), and no generated JUnit class mentions SPF.
#
# Anything else — INCORRECT_SEQUENCE, SOLVER_ERROR, TIMEOUT, LIBRARY_EXCEPTION,
# BLACK_BOX_VIOLATION, NO_PROGRESS, COMPILE_ERROR, INPUT_ERROR, a failing
# generated test, or a golden difference — is a FAIL.
#
# Prerequisite: scripts/setup-spf.sh has been run at least once.
#
# Run from the repository root:  ./verify-generated.sh [--update-golden]
set -uo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
PROJECT="$ROOT/pl-platform-testing"
UPDATE_GOLDEN=0
[ "${1:-}" = "--update-golden" ] && UPDATE_GOLDEN=1

EXPECT_GENERATED="stack queue mathlib arraylib linkedlist heap set hashmap bst graph lrucache taskqueue ticketservice orderservice"

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

    if find "outputs/$key/spf-work" -name blackbox.txt -size +0 2>/dev/null | grep -q .; then
        echo "FAIL: $key: the black-box guard fired (a symbolic value reached the library)"
        status=1
    fi
    if grep -q "gov.nasa.jpf" "outputs/$key/$GEN/GeneratedATCsTest.java"; then
        echo "FAIL: $key's JUnit class mentions SPF"
        status=1
    fi

    if [ ! -d "golden/$key" ]; then
        echo "   (no golden directory for $key; outputs not compared)"
        continue
    fi
    for f in ATC.txt GeneratedATCsTest.java; do
        if [ "$f" = ATC.txt ]; then out="outputs/$key/$f"; else out="outputs/$key/$GEN/$f"; fi
        if [ "$UPDATE_GOLDEN" -eq 1 ]; then
            cp "$out" "golden/$key/$f"
        elif ! diff -q "golden/$key/$f" "$out" > /dev/null 2>&1; then
            echo "FAIL: $key's $f differs from golden/$key/$f"
            status=1
        fi
    done
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
