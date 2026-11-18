#!/usr/bin/env bash
# FAIR comparison of TestGen, Randoop and EvoSuite on the 7 Defects4J classes and the 12 real bugs.
# Every tool tests the ADAPTER, under the same contract monitor, with the same oracle and the same mutants.
# See fair/README.md. Nothing outside fair/ is written (scratch goes to $MUT_WORK/fair).
#
#   fair/fair.sh                                  # everything: generate, mutants (7 classes), bugs (12), report
#   fair/fair.sh --classes "treelist flat3map"    # only these classes (keys as in mutation.sh); "none" = skip
#   fair/fair.sh --bugs "Collections-6 Lang-59"   # only these bugs; "none" = skip the bug runs
#   fair/fair.sh --tools "testgen randoop"        # testgen randoop evosuite-default evosuite-blackbox (default: all)
#   fair/fair.sh --seeds "1 2 3"                  # Randoop/EvoSuite seeds (default 1..10)
#   fair/fair.sh --report                         # only rebuild REPORT.md / validity.tsv from the rows
#   fair/fair.sh --allow-native-false-alarms      # see "TestGen check" below
# A run that stops can be started again: generated suites (.done markers) and mutant rows are kept and resumed.
# --redo-bugs re-runs bugs that already have results.
#
# Environment (as mutation.sh): THREADS (default cores - 4), TIMEOUT (30), MUT_WORK (~/.cache/testgen-d4j-mutation),
#   D4J_HOME (~/defects4j), JAVA11_HOME, BUDGET (60 s per class and run), GEN_JOBS (generators at once, default 4).
# Needs: TestGen built (mvn install); Defects4J set up (setup.sh: it provides the Randoop/EvoSuite jars);
#   the bugs checked out (checkout.sh) and run.sh run once (outputs/<bug>/: TestGen's suites and source lists);
#   mutation.sh run once (mutation/<key>.status.tsv: the scored mutants; MUT_WORK/<key>/testgen-long).
#
# TestGen check: before any mutant runs, the monitor must find no INVALID call and no false alarm in TestGen's
# suites on the fixed version. Otherwise the run stops and lists each test, the monitor's verdict and TestGen's own
# assertion. --allow-native-false-alarms continues only if every flagged test is also failed by TestGen's own
# assertions (a false alarm TestGen itself already has on that version) and no call is INVALID.
set -uo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"          # .../benchmarks/defects4j/fair
D4J_DIR="$(cd "$HERE/.." && pwd)"                               # .../benchmarks/defects4j
TG="$(cd "$D4J_DIR/../.." && pwd)"
MUT_WORK="${MUT_WORK:-$HOME/.cache/testgen-d4j-mutation}"
FW="$MUT_WORK/fair"
THREADS="${THREADS:-$(( $(nproc) > 6 ? $(nproc) - 4 : 2 ))}"
TIMEOUT="${TIMEOUT:-30}"; BUDGET="${BUDGET:-60}"; GEN_JOBS="${GEN_JOBS:-4}"
D4J_HOME="${D4J_HOME:-$HOME/defects4j}"
GENLIB="$D4J_HOME/framework/lib/test_generation/generation"
ADAPTER_FQN="in.ac.iiitb.plproject.atc.generated.Helper"
PKG="in.ac.iiitb.plproject.atc.generated"

classes_opt=""; bugs_opt=""; tools="testgen randoop evosuite-default evosuite-blackbox"; seeds="1 2 3 4 5 6 7 8 9 10"
report_only=0; allow_native=0; redo_bugs=0
while (( $# )); do
  case "$1" in
    --classes) classes_opt="$2"; shift ;;
    --bugs) bugs_opt="$2"; shift ;;
    --tools) tools="$2"; shift ;;
    --seeds) seeds="$2"; shift ;;
    --report) report_only=1 ;;
    --allow-native-false-alarms) allow_native=1 ;;
    --redo-bugs) redo_bugs=1 ;;
    -h|--help) sed -n '2,27p' "$0"; exit 0 ;;
    *) echo "fair.sh: unknown option $1" >&2; exit 64 ;;
  esac
  shift
done
has_tool() { [[ " $tools " == *" $1 "* ]]; }

report() {
  python3 "$HERE/report.py" "$HERE" && echo "report: $HERE/REPORT.md"
}
if (( report_only )); then report; exit $?; fi

# ── environment (as mutation.sh) ──
if [[ -z "${JAVA11_HOME:-}" ]]; then
  for d in /usr/lib/jvm/java-11-openjdk-*; do [[ -x "$d/bin/javac" ]] && { JAVA11_HOME="$d"; break; }; done
fi
[[ -x "${JAVA11_HOME:-}/bin/javac" ]] || { echo "fair.sh: no Java 11 JDK found; set JAVA11_HOME" >&2; exit 1; }
export JAVA_HOME="$JAVA11_HOME" PATH="$JAVA11_HOME/bin:$PATH"
export TZ="America/Los_Angeles"
SPF_HOME_DIR="${GENATC_SPF_HOME:-$HOME/.genatc-spf}"
export LD_LIBRARY_PATH="$SPF_HOME_DIR/jpf-symbc/lib${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}"

PL="$TG/pl-platform-testing/target/classes"; GM="$TG/genatc-mutation/target/classes"
[[ -f "$GM/in/ac/iiitb/plproject/mutation/MutantTestMain.class" ]] || {
  echo "fair.sh: TestGen is not built. From $TG run: mvn -q install -DskipTests" >&2; exit 1; }
JUNIT=$(find "$HOME/.m2/repository/junit" -name 'junit-4.13.1.jar' 2>/dev/null | head -1)
HAMCREST=$(find "$HOME/.m2/repository/org/hamcrest" -name 'hamcrest-core-1.3.jar' 2>/dev/null | head -1)
EVORT="$D4J_HOME/framework/lib/test_generation/runtime/evosuite-standalone-runtime-1.1.0.jar"
RANDOOP="$GENLIB/randoop-current.jar"; REPLACECALL="$GENLIB/replacecall-current.jar"; EVOSUITE="$GENLIB/evosuite-current.jar"
missing=()
[[ -f "$EVORT" ]] || missing+=("$EVORT")
if has_tool randoop; then [[ -f "$RANDOOP" ]] || missing+=("$RANDOOP"); [[ -f "$REPLACECALL" ]] || missing+=("$REPLACECALL"); fi
if has_tool evosuite-default || has_tool evosuite-blackbox; then [[ -f "$EVOSUITE" ]] || missing+=("$EVOSUITE"); fi
if (( ${#missing[@]} )); then
  printf 'fair.sh: the Randoop/EvoSuite jars Defects4J ships are missing (run %s/setup.sh):\n' "$D4J_DIR" >&2
  printf '  %s\n' "${missing[@]}" >&2; exit 1
fi

# ── build: the monitor runtime, the fair tools, and the existing runner classes (called, not changed) ──
B="$FW/build"; mkdir -p "$B/fairmon" "$B/tools" "$B/runner"
javac -nowarn -d "$B/fairmon" -cp "$JUNIT" "$HERE"/src/fairmon/*.java || exit 1
javac -nowarn -d "$B/runner" -cp "$PL:$GM:$JUNIT:$HAMCREST" \
  "$D4J_DIR"/runner/in/ac/iiitb/plproject/mutation/*.java "$D4J_DIR/runner/in/ac/iiitb/plproject/atc/D4jMain.java" || exit 1
javac -nowarn -d "$B/tools" -cp "$PL:$GM:$B/fairmon:$JUNIT" "$HERE"/src/in/ac/iiitb/plproject/fair/*.java || exit 1
TOOLS_CP="$B/tools:$B/fairmon:$B/runner:$PL:$GM:$JUNIT:$HAMCREST"
mkdir -p "$MUT_WORK/rt"
rsync -a --delete "$GM/" "$MUT_WORK/rt/mutation-classes/"
cp -u "$JUNIT" "$HAMCREST" "$EVORT" "$MUT_WORK/rt/"
RUNTIME_CP="$MUT_WORK/rt/$(basename "$JUNIT"):$MUT_WORK/rt/$(basename "$HAMCREST"):$MUT_WORK/rt/$(basename "$EVORT")"
rsync -a --delete "$B/fairmon/" "$MUT_WORK/rt/fairmon/"
FAIRMON_CP="$MUT_WORK/rt/fairmon"

# key -> bug whose fixed version is mutated, class file, adapter folder, spec (as mutation.sh)
classes=(
  "setuniquelist   Collections-19 list/SetUniqueList.java   setuniquelist   SetUniqueList"
  "setuniquelist4  Collections-21 list/SetUniqueList.java   setuniquelist4  SetUniqueList4"
  "flat3map        Collections-6  map/Flat3Map.java         flat3map        Flat3Map"
  "multivaluemap   Collections-4  map/MultiValueMap.java    multivaluemap   MultiValueMap"
  "treelist        Collections-20 list/TreeList.java        treelist        TreeList"
  "listorderedmap4 Collections-22 map/ListOrderedMap.java   listorderedmap4 ListOrderedMap4"
  "strbuilder      Lang-59        text/StrBuilder.java      strbuilder      StrBuilder"
)
library_of() {   # bug -> adapter folder and spec (as run.sh)
  case "$1" in
    Collections-1|Collections-6) echo "flat3map Flat3Map" ;;
    Collections-5|Collections-15|Collections-16|Collections-19) echo "setuniquelist SetUniqueList" ;;
    Collections-21) echo "setuniquelist4 SetUniqueList4" ;;
    Collections-22) echo "listorderedmap4 ListOrderedMap4" ;;
    Collections-20) echo "treelist TreeList" ;;
    Collections-4)  echo "multivaluemap MultiValueMap" ;;
    Lang-59|Lang-61) echo "strbuilder StrBuilder" ;;
    *) return 1 ;;
  esac
}
key_of_bug() { for row in "${classes[@]}"; do read -r k b _ <<< "$row"; [[ "$b" == "$1" ]] && { echo "$k"; return 0; }; done; return 1; }

sel_classes=(); sel_bugs=()
for row in "${classes[@]}"; do
  read -r k _ <<< "$row"
  [[ "$classes_opt" == none ]] && break
  [[ -z "$classes_opt" || " $classes_opt " == *" $k "* ]] && sel_classes+=("$k")
done
if [[ "$bugs_opt" != none ]]; then
  while IFS= read -r b; do
    [[ -z "$bugs_opt" || " $bugs_opt " == *" $b "* ]] && sel_bugs+=("$b")
  done < <(tail -n +2 "$D4J_DIR/bugs.csv" | cut -d, -f1)
fi
need_bugs=("${sel_bugs[@]}")
for k in "${sel_classes[@]}"; do
  for row in "${classes[@]}"; do read -r kk b _ <<< "$row"; [[ "$kk" == "$k" && " ${need_bugs[*]} " != *" $b "* ]] && need_bugs+=("$b"); done
done
echo "fair: classes: ${sel_classes[*]:-none}; bugs: ${sel_bugs[*]:-none}; tools: $tools; seeds: $seeds"

project_classes() {   # $1 checkout dir -> its compiled classes
  for d in "$1/build/classes" "$1/target/classes"; do [[ -d "$d" ]] && { echo "$d"; return 0; }; done
  return 1
}

# ── 1. per bug version: fixed/buggy classes, the real adapter, the monitor ──
prepare_bug() {   # $1 bug
  local bug="$1" lib spec BW pcf pcb
  read -r lib spec <<< "$(library_of "$bug")"
  BW="$FW/bugs/$bug"; mkdir -p "$BW"
  pcf=$(project_classes "$D4J_DIR/work/${bug}f") || { echo "fair.sh: $bug not checked out/compiled: run ./checkout.sh $bug" >&2; return 1; }
  pcb=$(project_classes "$D4J_DIR/work/${bug}b") || { echo "fair.sh: $bug not checked out/compiled: run ./checkout.sh $bug" >&2; return 1; }
  rsync -a --delete "$pcf/" "$BW/fixed-classes/"
  rsync -a --delete "$pcb/" "$BW/buggy-classes/"
  rm -rf "$BW/adapter" "$BW/monitor-src" "$BW/monitor-f" "$BW/monitor-b"; mkdir -p "$BW/adapter" "$BW/monitor-f" "$BW/monitor-b"
  javac -nowarn -d "$BW/adapter" -cp "$BW/fixed-classes" "$D4J_DIR/libraries/$lib/Helper.java" || return 1
  java -cp "$TOOLS_CP" in.ac.iiitb.plproject.fair.MonitorGen "$D4J_DIR/specs/$spec.spec" \
    "$D4J_DIR/libraries/$lib/Helper.java" "$BW/fixed-classes" "$BW/monitor-src" || return 1
  javac -nowarn -d "$BW/monitor-f" -cp "$BW/fixed-classes:$FAIRMON_CP" "$BW"/monitor-src/${PKG//.//}/*.java || return 1
  javac -nowarn -d "$BW/monitor-b" -cp "$BW/buggy-classes:$FAIRMON_CP" "$BW"/monitor-src/${PKG//.//}/*.java || {
    echo "fair.sh: the monitored adapter does not compile against $bug buggy" >&2; return 1; }
}

# ── 2. Randoop / EvoSuite on the ADAPTER (fixed version), then strip their oracles ──
gen_one() {   # $1 bug, $2 tool, $3 seed  -> $FW/gen/<bug>/<tool>-s<seed>/{raw,src}
  local bug="$1" tool="$2" seed="$3" BW="$FW/bugs/$1" G="$FW/gen/$1/$2-s$3" cp
  [[ -f "$G/.done" ]] && return 0
  rm -rf "$G"; mkdir -p "$G/raw" "$G/run"
  cp="$BW/fixed-classes:$BW/adapter"
  if [[ "$tool" == randoop ]]; then
    echo "$ADAPTER_FQN.reset();" > "$G/before-each.txt"
    omit_fields=()
    while read -r f; do omit_fields+=("--omit-field=$ADAPTER_FQN.$f"); done < <(
      grep -oE 'public static [^=;(]*[ ]([A-Za-z_][A-Za-z0-9_]*)[ ]*(=|;)' "$D4J_DIR/libraries/$(library_of "$bug" | cut -d' ' -f1)/Helper.java" \
        | sed -E 's/[ ]*(=|;)$//' | awk '{print $NF}')
    ( cd "$G/run" && timeout $(( BUDGET * 4 + 120 )) java -ea -classpath "$cp:$RANDOOP" \
        "-Xbootclasspath/a:$REPLACECALL" "-javaagent:$REPLACECALL" \
        randoop.main.Main gentests \
        --testclass="$ADAPTER_FQN" --junit-package-name="$PKG" --junit-output-dir="$G/raw" \
        --randomseed="$seed" --time-limit="$BUDGET" \
        --regression-test-basename=RegressionTest --error-test-basename=ErrorTest --no-error-revealing-tests=true \
        --junit-before-each="$G/before-each.txt" \
        --omit-methods="^in\.ac\.iiitb\.plproject\.atc\.generated\.Helper\.reset\(\)$" "${omit_fields[@]}" \
        --flaky-test-behavior=output --usethreads --clear=10000 --string-maxlen=5000 --forbid-null=false \
        --null-ratio=0.1 --only-test-public-members=false ) > "$G/gen.log" 2>&1
    rm -f "$G/raw/${PKG//.//}/RegressionTest.java"
    ls "$G/raw/${PKG//.//}"/RegressionTest*.java > /dev/null 2>&1 || { echo "GENFAIL randoop $bug s$seed: no test written, see $G/gen.log"; return 1; }
  else
    crit=()
    [[ "$tool" == evosuite-blackbox ]] && crit=(-criterion METHOD:METHODNOEXCEPTION:OUTPUT:EXCEPTION -Dalgorithm=MOSA)
    # Defects4J's evosuite.sh: half the budget for the search, half for assertions; its evosuite.config, minus -criterion
    ( cd "$G/run" && timeout $(( BUDGET * 4 + 300 )) java -Djava.awt.headless=true -cp "$EVOSUITE" org.evosuite.EvoSuite \
        -class "$ADAPTER_FQN" -projectCP "$cp" -seed "$seed" "${crit[@]}" \
        -Dsearch_budget=$(( BUDGET / 2 )) -Dassertion_timeout=$(( BUDGET / 2 )) -Dtest_dir="$G/raw" \
        -Dstopping_condition=MaxTime -Dshow_progress=false -Djunit_check=false -Dfilter_assertions=false \
        -Dtest_comments=false -Dreset_static_fields=true -mem 1500 ) > "$G/gen.log" 2>&1
    if ! ls "$G/raw/${PKG//.//}"/*_ESTest.java > /dev/null 2>&1; then
      echo "GENFAIL $tool $bug s$seed: EvoSuite wrote no test for the adapter. Last lines of $G/gen.log:"
      tail -25 "$G/gen.log" | sed 's/^/    /'
      return 2
    fi
    scaf=$(ls "$G/raw/${PKG//.//}"/*_ESTest_scaffolding.java | head -1)
    grep -q 'resetStaticState = true' "$G/raw/${PKG//.//}"/*_ESTest.java && grep -q "\"$ADAPTER_FQN\"" "$scaf" || {
      echo "GENFAIL $tool $bug s$seed: static-field reset is not on for Helper (resetStaticState / scaffolding), see $G/raw"; return 2; }
  fi
  java -cp "$TOOLS_CP" in.ac.iiitb.plproject.fair.StripOracles "$G/raw" "$G/src" \
    "$BW/monitor-f:$BW/fixed-classes:$JUNIT:$HAMCREST:$EVORT:$FAIRMON_CP" "$ADAPTER_FQN" > "$G/strip.txt" 2>&1 || {
    echo "GENFAIL $tool $bug s$seed: stripping failed, see $G/strip.txt"; return 1; }
  touch "$G/.done"
}

gen_tools=()
for t in randoop evosuite-default evosuite-blackbox; do has_tool "$t" && gen_tools+=("$t"); done

for bug in "${need_bugs[@]}"; do
  echo "== prepare $bug"
  prepare_bug "$bug" || exit 1
done
if (( ${#gen_tools[@]} )); then
  echo "== generating ${gen_tools[*]} for ${#need_bugs[@]} version(s), seeds $seeds, budget ${BUDGET}s, $GEN_JOBS at a time"
  genlog="$FW/gen-status.txt"; : > "$genlog"
  for bug in "${need_bugs[@]}"; do
    for tool in "${gen_tools[@]}"; do
      for seed in $seeds; do
        while (( $(jobs -rp | wc -l) >= GEN_JOBS )); do wait -n; done
        ( gen_one "$bug" "$tool" "$seed" >> "$genlog" 2>&1 ) &
      done
    done
  done
  wait
  if grep -q '^GENFAIL' "$genlog"; then
    echo "fair.sh: generation failed (EvoSuite is never pointed at the Commons class instead):" >&2
    cat "$genlog" >&2; exit 2
  fi
fi

suite_lines() {   # $1 bug -> tool TAB suite TAB stripped sources, for the selected tools/seeds
  for tool in "${gen_tools[@]}"; do for seed in $seeds; do
    printf '%s\t%s-s%s\t%s\n' "$tool" "$tool" "$seed" "$FW/gen/$1/$tool-s$seed/src"
  done; done
}

testgen_long() {   # $1 bug, $2 spec, $3 key or "" -> the long-string-only TestGen test (mutation.sh's when it exists)
  local bug="$1" spec="$2" key="$3" f W
  if [[ -n "$key" ]]; then
    f="$MUT_WORK/$key/testgen-long/fixed/${PKG//.//}/GeneratedATCsTest.java"
    [[ -f "$f" ]] && { echo "$f"; return 0; }
  fi
  W="$FW/bugs/$bug/testgen-long"; f="$W/out/fixed/${PKG//.//}/GeneratedATCsTest.java"
  if [[ ! -f "$f" ]]; then
    mkdir -p "$W"
    awk 'f==0 && /^test /{f=1} f==1{print} f==1 && /^}/{exit}' "$D4J_DIR/specs/$spec.tests" > "$W/long.tests"
    ( cd "$D4J_DIR" && java -cp "$B/runner:$PL:$JUNIT:$HAMCREST" in.ac.iiitb.plproject.atc.D4jMain \
        "$bug" "specs/$spec.spec" "$W/long.tests" "$W/out" "outputs/$bug/fixed-sources.txt" "outputs/$bug/buggy-sources.txt" ) \
        > "$W/gen.log" 2>&1
  fi
  [[ -f "$f" ]] && echo "$f"
}

testgen_copy() {   # $1 scope (class key or bug), $2 suite, $3 TestGen's test file -> a COPY without its PRE assertions
  # TestGen checks each precondition with assertTrue("PRE ...") BEFORE the call, so on a mutant that assertion would end
  # the test before the call reaches the monitor. The monitor checks the same PRE clauses, so the copy drops them (one
  # line each) and every call is judged by the monitor, as for Randoop/EvoSuite. The original file is never changed.
  local dst="$FW/testgen/$1/$2/${PKG//.//}/$(basename "$3")"
  mkdir -p "$(dirname "$dst")"
  grep -vE '^[[:space:]]*assertTrue\("PRE ' "$3" > "$dst"
  # read by FairDriver (next to the sources): the removed PRE assertions count as assertions_removed
  echo "files 1 assertions_removed $(grep -cE '^[[:space:]]*assertTrue\("PRE ' "$3") offapi_markers 0 from $3" > "$(dirname "$dst")/strip.txt"
  echo "$dst"
}

common_props() {   # $1 work dir
  cat <<EOF
runtimeCp=$RUNTIME_CP
mutationCp=$MUT_WORK/rt/mutation-classes
fairmonCp=$FAIRMON_CP
threads=$THREADS
timeoutSeconds=$TIMEOUT
work=$1
checkTools=testgen-long,testgen-all,testgen-family
allowNativeFalseAlarms=$( (( allow_native )) && echo true || echo false )
EOF
}

# ── 3. mutants, per class ──
for key in "${sel_classes[@]}"; do
  for row in "${classes[@]}"; do read -r k bug cls lib spec <<< "$row"; [[ "$k" == "$key" ]] && break; done
  echo "================ $key ($bug fixed) ================"
  srcs="$D4J_DIR/outputs/$bug/fixed-sources.txt"
  [[ -f "$srcs" ]] || { echo "fair.sh: no $srcs: run ./run.sh $bug first" >&2; exit 1; }
  [[ -f "$D4J_DIR/mutation/$key.status.tsv" ]] || { echo "fair.sh: no mutation/$key.status.tsv: run ./mutation.sh $key first" >&2; exit 1; }
  target=$(grep "/$cls\$" "$srcs" | head -1)
  K="$FW/classes/$key"; BW="$FW/bugs/$bug"; mkdir -p "$K"; SCOPE="$key"

  # the same mutants, planted and compiled by D4jMutationRun itself (no suite: it only plants and compiles)
  if [[ ! -f "$K/plant-out/$key.status.tsv" ]]; then
    : > "$K/no-suites.tsv"
    cat > "$K/plant.properties" <<EOF
key=$key
target=$target
adapter=$(head -1 "$srcs")
mutants=$D4J_DIR/mutation/$key.mutants.tsv
suites=$K/no-suites.tsv
projectClasses=$BW/fixed-classes
runtimeCp=$RUNTIME_CP
mutationCp=$MUT_WORK/rt/mutation-classes
work=$K/plant
out=$K/plant-out
threads=$THREADS
timeoutSeconds=$TIMEOUT
cap=${CAP:-300}
seed=${SEED:-1}
EOF
    java -cp "$TOOLS_CP" in.ac.iiitb.plproject.mutation.D4jMutationRun "$K/plant.properties" > "$K/plant.log" 2>&1 || {
      echo "fair.sh: planting failed, see $K/plant.log" >&2; exit 1; }
  fi
  a=$(awk -F'\t' '$3=="yes"{print $1"\t"$2}' "$D4J_DIR/mutation/$key.status.tsv" | sort)
  b=$(awk -F'\t' '$3=="yes"{print $1"\t"$2}' "$K/plant-out/$key.status.tsv" | sort)
  [[ "$a" == "$b" ]] || { echo "fair.sh: $key: the planted sample differs from mutation/$key.status.tsv; stopping" >&2; exit 1; }

  # the monitor, compiled against the unmutated class build (mutants replace only the Commons class)
  rm -rf "$K/monitor"; mkdir -p "$K/monitor"
  javac -nowarn -d "$K/monitor" -cp "$K/plant/orig:$BW/fixed-classes:$FAIRMON_CP" "$BW"/monitor-src/${PKG//.//}/*.java || exit 1

  suites="$K/suites.tsv"; : > "$suites"
  if has_tool testgen; then
    long=$(testgen_long "$bug" "$spec" "$key") || true
    [[ -n "$long" ]] && printf 'testgen-long\ttestgen-long\t%s\n' "$(testgen_copy "$SCOPE" testgen-long "$long")" >> "$suites"
    all="$D4J_DIR/outputs/$bug/fixed/${PKG//.//}/GeneratedATCsTest.java"
    [[ -f "$all" ]] && printf 'testgen-all\ttestgen-all\t%s\n' "$(testgen_copy "$SCOPE" testgen-all "$all")" >> "$suites"
    if ls "$MUT_WORK/$key/family-src"/*.java > /dev/null 2>&1; then
      printf 'testgen-family\ttestgen-family\t%s\n' "$MUT_WORK/$key/family-src" >> "$suites"
    fi
  fi
  suite_lines "$bug" >> "$suites"

  { echo "mode=mutants"; echo "scope=$key"; echo "suites=$suites"; echo "monitor=$K/monitor"
    echo "orig=$K/plant/orig"; echo "mutantsDir=$K/plant/mutants"; echo "sample=$D4J_DIR/mutation/$key.status.tsv"
    echo "projectClasses=$BW/fixed-classes"; echo "validity=$K/validity.tsv"; echo "rows=$HERE/$key.fair.rows.tsv"
    common_props "$K/run"; } > "$K/fair.properties"
  java -cp "$TOOLS_CP" in.ac.iiitb.plproject.fair.FairDriver "$K/fair.properties"
  rc=$?
  (( rc == 3 )) && { echo "fair.sh: stopped by the TestGen check (see above)" >&2; exit 3; }
  (( rc == 0 )) || exit $rc
done

# ── 4. the 12 real bugs ──
for bug in "${sel_bugs[@]}"; do
  read -r lib spec <<< "$(library_of "$bug")"
  BW="$FW/bugs/$bug"; SCOPE="$bug"
  [[ -f "$BW/bug.rows.tsv" ]] && (( ! redo_bugs )) && { echo "== $bug: already run (--redo-bugs to run again)"; continue; }
  echo "================ bug $bug ================"
  suites="$BW/suites.tsv"; : > "$suites"
  if has_tool testgen; then
    long=$(testgen_long "$bug" "$spec" "$(key_of_bug "$bug" || true)") || true
    [[ -n "$long" ]] && printf 'testgen-long\ttestgen-long\t%s\n' "$(testgen_copy "$SCOPE" testgen-long "$long")" >> "$suites"
    all="$D4J_DIR/outputs/$bug/fixed/${PKG//.//}/GeneratedATCsTest.java"
    [[ -f "$all" ]] && printf 'testgen-all\ttestgen-all\t%s\n' "$(testgen_copy "$SCOPE" testgen-all "$all")" >> "$suites"
  fi
  suite_lines "$bug" >> "$suites"
  { echo "mode=bug"; echo "scope=$bug"; echo "suites=$suites"; echo "monitor=$BW/monitor-f"
    echo "projectClasses=$BW/fixed-classes"; echo "buggyMonitor=$BW/monitor-b"; echo "buggyClasses=$BW/buggy-classes"
    echo "validity=$BW/validity.tsv"; echo "rows=$BW/bug.rows.tsv"; common_props "$BW/run"; } > "$BW/fair.properties"
  java -cp "$TOOLS_CP" in.ac.iiitb.plproject.fair.FairDriver "$BW/fair.properties"
  rc=$?
  (( rc == 3 )) && { echo "fair.sh: stopped by the TestGen check (see above)" >&2; exit 3; }
  (( rc == 0 )) || exit $rc
done

# ── 5. collect validity and bug rows into fair/, build the report ──
{
  printf 'scope\ttool\tsuite\tstatus\ttests\tinvalid_first_call\tpct_invalid\tinvalid_any_call\tno_call\tfalse_alarms\tflaky\tnot_run\tnot_fresh\tkept\tcalls_median\tcalls_max\tassertions_removed\toffapi_markers\tcalls_by_op\treason\n'
  for k in "$FW"/classes/*/validity.tsv; do [[ -f "$k" ]] && cat "$k"; done
  for k in "$FW"/bugs/*/validity.tsv; do [[ -f "$k" ]] && cat "$k"; done
} > "$HERE/validity.tsv"
{
  printf 'bug\ttool\tsuite\ttests\tkept\tresult\ttest\tcall\tmessage\n'
  for f in "$FW"/bugs/*/bug.rows.tsv; do [[ -f "$f" ]] && tail -n +2 "$f"; done
} > "$HERE/bugs.fair.tsv"
report
