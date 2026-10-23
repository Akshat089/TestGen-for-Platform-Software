#!/usr/bin/env bash
# Runs every mutation set through the whole pipeline and reports the score.
#
# For each library, the test for its checked-in sequence is generated ONCE on
# the correct library (checks -> IR -> ATC -> genCTC, each round's ready calls
# run on the real library inside SPF -> GeneratedATCsTest.java) and compiled
# once. Then, for each mutant in mutations/*.mutants:
#   1. seed the fault into a private copy of the library and compile it
#   2. run that same compiled test, unchanged, against the mutated library
#   3. killed if a PRE or POST assertion fails, the library throws, or the
#      test times out
#
# Exits non-zero when a mutant contradicts what its mutation set declared —
# either it could not be seeded, or `expect:` disagrees with what happened.
#
# Prerequisite: ../scripts/setup-spf.sh has been run once.
#
# Run from the repository root:  ./genatc-mutation/run-mutations.sh [stack|hashmap ...]
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
mvn -q -o install -pl pl-platform-testing -am -DskipTests
cd "$ROOT/genatc-mutation"
ARGS="$*"
mvn -q -o exec:java -Dexec.mainClass=in.ac.iiitb.plproject.mutation.MutationRunner \
    ${ARGS:+-Dexec.args="$ARGS"}
