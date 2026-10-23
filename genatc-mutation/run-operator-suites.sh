#!/usr/bin/env bash
# Builds and runs the generated test suites, and measures them with the
# generated mutation suites. Three phases per library:
#
#   1. generate 260 test strings from the spec, generate each one's test ONCE
#      on the correct library (genCTC on real SPF, ready calls on the real
#      library), run it on the correct library, and record whether it passed,
#      genCTC rejected it (INCORRECT SEQUENCE), or a generated assertion failed.
#   2. generate the operator mutants — every operator in MutationOperator applied
#      to every line of the library source that admits one — and run the cached
#      test for the library's own checked-in sequence against each. This is the
#      per-operator score.
#   3. run every mutant that survived phase 2 against the passing family tests
#      from phase 1, stopping at the first one that kills it. This measures how
#      much of the survival was the specification and how much was the single
#      sequence.
#
# This is the mechanical counterpart of ./run-mutations.sh, which runs the
# hand-written sets in mutations/ and checks their declared expectations. This
# one declares nothing: it measures.
#
# What it writes, all under operator-suites/ (relative to this directory):
#   <key>.mutants                    the generated mutation suite
#   teststrings/<key>/*.tests        the generated test strings, one file each
#   results/<key>.mutants.tsv        one row per mutant
#   results/<key>.teststrings.tsv    one row per test string
#   SUMMARY.txt                      the three tables
#
# It is SLOW — thousands of real SPF runs (one per genCTC round). Budget well over an hour for the
# whole set; pass keys for a subset.
#
# Prerequisite: ../scripts/setup-spf.sh has been run once.
#
# Run from the repository root:  ./genatc-mutation/run-operator-suites.sh [stack queue ...]
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
mvn -q -o install -pl pl-platform-testing -am -DskipTests
cd "$ROOT/genatc-mutation"
ARGS="$*"
mvn -q -o exec:java -Dexec.mainClass=in.ac.iiitb.plproject.mutation.GeneratedSuiteRunner \
    ${ARGS:+-Dexec.args="$ARGS"}
