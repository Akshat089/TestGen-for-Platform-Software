#!/usr/bin/env bash
# Mothra mutation testing of TestGen, Randoop and EvoSuite on the 7 real Defects4J classes.
#   ./mutation.sh                     # all 7 classes
#   ./mutation.sh treelist flat3map   # only these (keys below)
#   ./mutation.sh --regen treelist    # regenerate the mutants first (otherwise mutation/<key>.mutants.tsv is reused)
# Environment: THREADS (default: cores - 4), TIMEOUT (wall-clock seconds per test JVM, default 30),
#              CAP (mutants per class, default 300), SEED (sample seed, default 1),
#              MUT_WORK (scratch, default ~/.cache/testgen-d4j-mutation; keep it on the Linux file system).
# Needs: TestGen built (mvn install), the bugs checked out and run.sh run once (outputs/<bug>/),
#        and for the baselines: baselines/runs/<tool>/<bug>/b60-s<seed>/ from baselines.sh.
# Results: mutation/<key>.* and, after all classes, mutation/REPORT.md (python3 mutation/report.py).
set -uo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TG="$(cd "$HERE/../.." && pwd)"
OUT="$HERE/mutation"
MUT_WORK="${MUT_WORK:-$HOME/.cache/testgen-d4j-mutation}"
THREADS="${THREADS:-$(( $(nproc) > 6 ? $(nproc) - 4 : 2 ))}"
TIMEOUT="${TIMEOUT:-30}"; CAP="${CAP:-300}"; SEED="${SEED:-1}"
D4J_HOME="${D4J_HOME:-$HOME/defects4j}"

if [[ -z "${JAVA11_HOME:-}" ]]; then
  for d in /usr/lib/jvm/java-11-openjdk-*; do [[ -x "$d/bin/javac" ]] && { JAVA11_HOME="$d"; break; }; done
fi
[[ -x "${JAVA11_HOME:-}/bin/javac" ]] || { echo "mutation.sh: no Java 11 JDK found; set JAVA11_HOME" >&2; exit 1; }
export JAVA_HOME="$JAVA11_HOME" PATH="$JAVA11_HOME/bin:$PATH"
SPF_HOME_DIR="${GENATC_SPF_HOME:-$HOME/.genatc-spf}"
export LD_LIBRARY_PATH="$SPF_HOME_DIR/jpf-symbc/lib${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}"

PL="$TG/pl-platform-testing/target/classes"; GM="$TG/genatc-mutation/target/classes"
[[ -f "$GM/in/ac/iiitb/plproject/mutation/MutantTestMain.class" ]] || {
  echo "mutation.sh: TestGen is not built. From $TG run: mvn -q install -DskipTests" >&2; exit 1; }
JUNIT=$(find "$HOME/.m2/repository/junit" -name 'junit-4.13.1.jar' 2>/dev/null | head -1)
HAMCREST=$(find "$HOME/.m2/repository/org/hamcrest" -name 'hamcrest-core-1.3.jar' 2>/dev/null | head -1)
EVORT="$D4J_HOME/framework/lib/test_generation/runtime/evosuite-standalone-runtime-1.1.0.jar"
[[ -f "$EVORT" ]] || { echo "mutation.sh: EvoSuite runtime not found at $EVORT" >&2; exit 1; }

# Build the two runner classes (in TestGen's mutation package, see runner/) and D4jMain.
mkdir -p "$HERE/build/mutation" "$HERE/build/runner"
javac -nowarn -d "$HERE/build/runner" -cp "$PL:$JUNIT:$HAMCREST" \
  "$HERE/runner/in/ac/iiitb/plproject/atc/D4jMain.java" || exit 1
javac -nowarn -d "$HERE/build/mutation" -cp "$PL:$GM:$JUNIT:$HAMCREST" \
  "$HERE"/runner/in/ac/iiitb/plproject/mutation/*.java || exit 1

# Copy what every test JVM loads onto the Linux file system once (thousands of JVMs start).
mkdir -p "$MUT_WORK/rt"
rsync -a --delete "$GM/" "$MUT_WORK/rt/mutation-classes/"
cp -u "$JUNIT" "$HAMCREST" "$EVORT" "$MUT_WORK/rt/"
RUNTIME_CP="$MUT_WORK/rt/$(basename "$JUNIT"):$MUT_WORK/rt/$(basename "$HAMCREST"):$MUT_WORK/rt/$(basename "$EVORT")"

# key -> bug whose fixed version is mutated, the class file, the adapter, the spec
classes=(
  "setuniquelist   Collections-19 list/SetUniqueList.java   SetUniqueList"
  "setuniquelist4  Collections-21 list/SetUniqueList.java   SetUniqueList4"
  "flat3map        Collections-6  map/Flat3Map.java         Flat3Map"
  "multivaluemap   Collections-4  map/MultiValueMap.java    MultiValueMap"
  "treelist        Collections-20 list/TreeList.java        TreeList"
  "listorderedmap4 Collections-22 map/ListOrderedMap.java   ListOrderedMap4"
  "strbuilder      Lang-59        text/StrBuilder.java      StrBuilder"
)

regen=0; want=()
for a in "$@"; do [[ "$a" == --regen ]] && regen=1 || want+=("$a"); done

for row in "${classes[@]}"; do
  read -r key bug cls spec <<< "$row"
  if (( ${#want[@]} )) && [[ ! " ${want[*]} " == *" $key "* ]]; then continue; fi
  echo "================ $key ($bug fixed) ================"
  srcs="$HERE/outputs/$bug/fixed-sources.txt"
  [[ -f "$srcs" ]] || { echo "  no $srcs: run ./run.sh $bug first" >&2; continue; }
  target=$(grep "/$cls\$" "$srcs" | head -1)
  adapter=$(head -1 "$srcs")
  W="$MUT_WORK/$key"; mkdir -p "$W"

  # 1. mutants (generated once; every tool is measured against the same file)
  if (( regen )) || [[ ! -f "$OUT/$key.mutants.tsv" ]]; then
    java -cp "$HERE/build/mutation:$PL:$GM" in.ac.iiitb.plproject.mutation.D4jMutantGen \
      "$key" "$target" "$srcs" "$OUT" | sed -n "2,6p" || exit 1
  fi

  # 2. TestGen suites: all strings (from run.sh), and the long string alone (generated here once)
  all="$HERE/outputs/$bug/fixed/in/ac/iiitb/plproject/atc/generated/GeneratedATCsTest.java"
  [[ -f "$all" ]] || { echo "  no TestGen test at $all: run ./run.sh $bug first" >&2; continue; }
  long="$W/testgen-long/fixed/in/ac/iiitb/plproject/atc/generated/GeneratedATCsTest.java"
  if [[ ! -f "$long" ]]; then
    awk 'f==0 && /^test /{f=1} f==1{print} f==1 && /^}/{exit}' "$HERE/specs/$spec.tests" > "$W/long.tests"
    echo "  generating TestGen's long-string-only test ($(grep -o 'test [A-Za-z0-9_]*' "$W/long.tests"))"
    ( cd "$HERE" && java -cp "$HERE/build/runner:$PL:$JUNIT:$HAMCREST" in.ac.iiitb.plproject.atc.D4jMain \
        "$bug" "specs/$spec.spec" "$W/long.tests" "$W/testgen-long" "$srcs" "$srcs" ) > "$W/testgen-long.log" 2>&1
    [[ -f "$long" ]] || { echo "  long-string generation failed, see $W/testgen-long.log" >&2; continue; }
  fi
  suites="$W/suites.tsv"
  printf 'testgen-long\ttestgen-long\t%s\n' "$long" > "$suites"
  printf 'testgen-all\ttestgen-all\t%s\n' "$all" >> "$suites"

  # 3. Randoop / EvoSuite suites from baselines.sh (generated on the same fixed version), one per seed
  project="${bug%-*}"
  for tool in randoop evosuite; do
    for run in "$HERE"/baselines/runs/$tool/$bug/b60-s*; do
      [[ -d "$run" ]] || continue
      seed="${run##*-s}"
      arch=$(ls "$run"/suites/"$project"/$tool/"$seed"/*.tar.bz2 2>/dev/null | head -1)
      [[ -n "$arch" ]] || { echo "  $tool seed $seed: no suite archive in $run" >&2; continue; }
      dest="$W/baseline-src/$tool-s$seed"
      rm -rf "$dest"; mkdir -p "$dest"; tar -xjf "$arch" -C "$dest"
      printf '%s\t%s-s%s\t%s\n' "$tool" "$tool" "$seed" "$dest" >> "$suites"
    done
  done
  echo "  suites: $(cut -f1 "$suites" | sort | uniq -c | tr '\n' ' ' | tr -s ' ')"

  # 4. the project's compiled classes, on the Linux file system
  pc=""
  for d in "$HERE/work/${bug}f/build/classes" "$HERE/work/${bug}f/target/classes"; do [[ -d "$d" ]] && { pc="$d"; break; }; done
  [[ -n "$pc" ]] || { echo "  no compiled classes in work/${bug}f: run ./checkout.sh $bug" >&2; continue; }
  rsync -a --delete "$pc/" "$W/project-classes/"

  cat > "$W/run.properties" <<EOF
key=$key
target=$target
adapter=$adapter
mutants=$OUT/$key.mutants.tsv
suites=$suites
projectClasses=$W/project-classes
runtimeCp=$RUNTIME_CP
mutationCp=$MUT_WORK/rt/mutation-classes
work=$W/run
out=$OUT
threads=$THREADS
timeoutSeconds=$TIMEOUT
cap=$CAP
seed=$SEED
EOF
  java -cp "$HERE/build/mutation:$PL:$GM:$JUNIT:$HAMCREST" in.ac.iiitb.plproject.mutation.D4jMutationRun "$W/run.properties"
done

echo
python3 "$OUT/report.py" "$OUT" && echo "report: $OUT/REPORT.md"
