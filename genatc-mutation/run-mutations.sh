#!/usr/bin/env bash
# Runs every mutation set through the whole pipeline and reports the score.
#
# For each mutant in mutations/*.mutants:
#   1. seed the fault into a private copy of the library
#   2. re-run the real pipeline from the spec file: checks -> IR -> real SPF
#      -> GeneratedATCs.java (symbolic driver) -> GeneratedATCsTest.java (JUnit)
#   3. compile and run the generated JUnit class against the mutated library
#   4. see whether any generated assertion rejects the fault
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
