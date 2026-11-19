#!/usr/bin/env bash
# FAIR comparison of TestGen, Randoop and EvoSuite on the 7 Defects4J classes and the 12 real bugs.
# Every tool tests the ADAPTER, under the same contract monitor, with the same oracle and the same mutants.
# See fair/README.md. Nothing outside fair/ is written (scratch goes to $MUT_WORK/fair).
#
#   fair/fair.sh                                  # everything: generate, mutants (7 classes), bugs (12), report
#   fair/fair.sh --classes "treelist flat3map"    # only these classes (keys as in mutation.sh); "none" = skip
#   fair/fair.sh --bugs "Collections-6 Lang-59"   # only these bugs; "none" = skip the bug runs
#   fair/fair.sh --tools "testgen randoop"        # testgen randoop evosuite-default evosuite-blackbox (default: all)
#   fair/fair.sh --tools "randpre randpre-pool"   # the tools added later (run them apart from the four above):
#       randpre, randpre-pool                     #   RandPre, 1000 draws per call (suites randpre[-pool]-{long,all}-s<seed>)
#       randpre-timed, randpre-pool-timed         #   RandPre with TestGen's time per string (needs fair/timing.tsv)
#       randoop-b180, evosuite-default-b180, evosuite-blackbox-b180   # the three generators with a 180 s budget
#   fair/fair.sh --timing [--timing-runs "1 2"]   # TestGen's time per test string on each bug's fixed version -> fair/timing.tsv
#   fair/fair.sh --depth-timing [--libs "stack"] [--timing-runs "1"]  # the 14 libraries, prefixes 4..24 -> ../../depth_timing.tsv
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
report_only=0; allow_native=0; redo_bugs=0; timing=0; depth_timing=0; timing_runs="1"; libs_opt=""
while (( $# )); do
  case "$1" in
    --classes) classes_opt="$2"; shift ;;
    --bugs) bugs_opt="$2"; shift ;;
    --tools) tools="$2"; shift ;;
    --seeds) seeds="$2"; shift ;;
    --report) report_only=1 ;;
    --allow-native-false-alarms) allow_native=1 ;;
    --redo-bugs) redo_bugs=1 ;;
    --timing) timing=1 ;;
    --depth-timing) depth_timing=1 ;;
    --timing-runs) timing_runs="$2"; shift ;;
    --libs) libs_opt="$2"; shift ;;
    -h|--help) sed -n '2,40p' "$0"; exit 0 ;;
    *) echo "fair.sh: unknown option $1" >&2; exit 64 ;;
  esac
  shift
done
has_tool() { [[ " $tools " == *" $1 "* ]]; }
# Tools added after the first fair run (RandPre, budget 180 s). A run of them must not include an original tool,
# so that its validity and bug rows go to their own files (validity.<tag>.tsv, bug.rows.<tag>.tsv) and the
# original ones are never rewritten.
ORIG_TOOLS="testgen randoop evosuite-default evosuite-blackbox"
NEW_TOOLS="randpre randpre-pool randpre-timed randpre-pool-timed randoop-b180 evosuite-default-b180 evosuite-blackbox-b180"
run_tag=""
for t in $tools; do
  if [[ " $NEW_TOOLS " == *" $t "* ]]; then run_tag+="${run_tag:+_}$t"
  elif [[ " $ORIG_TOOLS " != *" $t "* ]]; then echo "fair.sh: unknown tool $t" >&2; exit 64; fi
done
if [[ -n "$run_tag" ]]; then
  for t in $tools; do [[ " $ORIG_TOOLS " == *" $t "* ]] && { echo "fair.sh: do not mix $t with the new tools in one run" >&2; exit 64; }; done
fi
VAL_NAME="validity${run_tag:+.$run_tag}.tsv"; BUGROWS_NAME="bug.rows${run_tag:+.$run_tag}.tsv"
budget_of() { [[ "$1" == *-b180 ]] && echo 180 || echo "$BUDGET"; }   # generation budget of a Randoop/EvoSuite tool

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
if has_tool randoop || has_tool randoop-b180; then [[ -f "$RANDOOP" ]] || missing+=("$RANDOOP"); [[ -f "$REPLACECALL" ]] || missing+=("$REPLACECALL"); fi
if [[ " $tools " == *" evosuite-"* ]]; then [[ -f "$EVOSUITE" ]] || missing+=("$EVOSUITE"); fi
if (( ${#missing[@]} )); then
  printf 'fair.sh: the Randoop/EvoSuite jars Defects4J ships are missing (run %s/setup.sh):\n' "$D4J_DIR" >&2
  printf '  %s\n' "${missing[@]}" >&2; exit 1
fi

# ── build: the monitor runtime, the fair tools, and the existing runner classes (called, not changed) ──
B="$FW/build"; mkdir -p "$B/fairmon" "$B/tools" "$B/runner"
javac -nowarn -d "$B/fairmon" -cp "$JUNIT" "$HERE"/src/fairmon/*.java || exit 1
javac -nowarn -d "$B/runner" -cp "$PL:$GM:$JUNIT:$HAMCREST" \
  "$D4J_DIR"/runner/in/ac/iiitb/plproject/mutation/*.java "$D4J_DIR/runner/in/ac/iiitb/plproject/atc/D4jMain.java" || exit 1
javac -nowarn -d "$B/tools" -cp "$PL:$GM:$B/fairmon:$JUNIT" "$HERE"/src/in/ac/iiitb/plproject/fair/*.java \
  "$HERE"/src/in/ac/iiitb/plproject/atc/*.java || exit 1
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
  local bud; bud=$(budget_of "$tool"); local BUDGET="$bud"; tool="${tool%-b180}"   # randoop-b180 = randoop with 180 s
  [[ -f "$G/.done" ]] && return 0
  rm -rf "$G"; mkdir -p "$G/raw" "$G/run"
  cp="$BW/fixed-classes:$BW/adapter"
  if [[ "$tool" == randpre* ]]; then gen_randpre "$bug" "$2" "$seed" "$G"; return $?; fi
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

# ── 2b. RandPre (fair/README.md): TestGen's strings, random values kept only when the monitor's PRE holds ──
pre_strip() {   # $1 a JUnit file in TestGen's form, $2 dest dir -> a copy without its PRE assertions (as testgen_copy)
  local dst="$2/${PKG//.//}/$(basename "$1")"
  mkdir -p "$(dirname "$dst")"
  grep -vE '^[[:space:]]*assertTrue\("PRE ' "$1" > "$dst"
  echo "files 1 assertions_removed $(grep -cE '^[[:space:]]*assertTrue\("PRE ' "$1") offapi_markers 0 from $1" > "$(dirname "$dst")/strip.txt"
  echo "$dst"
}
gen_randpre() {   # $1 bug, $2 tool (randpre[-pool][-timed]), $3 seed, $4 dir -> $4/{long,all}/{raw,src}
  local bug="$1" tool="$2" seed="$3" G="$4" BW="$FW/bugs/$1" lib spec pool=false budget which f
  read -r lib spec <<< "$(library_of "$bug")"
  [[ "$tool" == *-pool* ]] && pool=true
  if [[ "$tool" == *-timed ]]; then budget=(timing="$HERE/timing.tsv" scope="$bug"); else budget=(draws=1000); fi
  for which in long all; do
    mkdir -p "$G/$which/src"
    ( cd "$D4J_DIR" && timeout 3600 java -Xmx512m -cp "$BW/monitor-f:$BW/fixed-classes:$FAIRMON_CP:$TOOLS_CP" \
        in.ac.iiitb.plproject.fair.RandPre spec="specs/$spec.spec" tests="specs/$spec.tests" which="$which" \
        seed="$seed" pool="$pool" "${budget[@]}" contracts="$BW/monitor-src/contracts.tsv" \
        monitorHelper="$BW/monitor-src/${PKG//.//}/Helper.java" out="$G/$which" ) > "$G/$which/gen.log" 2>&1 || {
      echo "GENFAIL $tool $bug s$seed ($which): see $G/$which/gen.log"; return 1; }
    f="$G/$which/raw/${PKG//.//}/GeneratedATCsTest.java"
    [[ -f "$f" ]] && pre_strip "$f" "$G/$which/src" > /dev/null
  done
  touch "$G/.done"
}

gen_tools=()
for t in randoop evosuite-default evosuite-blackbox randoop-b180 evosuite-default-b180 evosuite-blackbox-b180 \
         randpre randpre-pool randpre-timed randpre-pool-timed; do has_tool "$t" && gen_tools+=("$t"); done

# ── TestGen's running time (--timing, --depth-timing): FairTiming calls GenatcMain.generate per string ──
if (( timing )); then
  # every bug's fixed version (the 7 classes are among them), each test string generated alone
  tbugs=("${sel_bugs[@]}"); (( ${#tbugs[@]} )) || { echo "fair.sh: --timing needs bugs" >&2; exit 64; }
  out="$HERE/timing.tsv"
  [[ -f "$out" ]] || printf 'scope\tclass\tspec\tstring\tcalls\trun\tstatus\trounds\tspf_runs\tspf_ms\ttotal_ms\twall_ms\tdetail\n' > "$out"
  for run in $timing_runs; do
    for bug in "${tbugs[@]}"; do
      read -r lib spec <<< "$(library_of "$bug")"
      key=$(key_of_bug "$bug" || true)
      grep -qP "^\Q$bug\E\t[^\t]*\t[^\t]*\t[^\t]*\t[^\t]*\t$run\t" "$out" && { echo "== timing $bug run $run: already in timing.tsv"; continue; }
      echo "== timing $bug ($spec) run $run"
      ( cd "$D4J_DIR" && java -cp "$TOOLS_CP" in.ac.iiitb.plproject.atc.FairTiming d4j "$bug" "specs/$spec.spec" \
          "specs/$spec.tests" "outputs/$bug/fixed-sources.txt" "$FW/timing" "$run" ) > "$FW/timing-$bug-$run.log" 2>&1
      grep '^ROW' "$FW/timing-$bug-$run.log" | while IFS=$'\t' read -r _ sc name calls r st rounds runs spf tot wall det; do
        printf '%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' "$sc" "${key:--}" "$spec" "$name" "$calls" "$r" "$st" "$rounds" "$runs" "$spf" "$tot" "$wall" "$det"
      done | tee -a "$out"
    done
  done
  exit 0
fi
if (( depth_timing )); then
  # the 14 libraries: prefixes of the one test string in specs/<Lib>.tests, run from scratch .tests files
  out="$TG/depth_timing.tsv"
  [[ -f "$out" ]] || printf 'library\tstring\tprefix\trun\tstatus\trounds\tspf_runs\tspf_ms\ttotal_ms\twall_ms\tdetail\n' > "$out"
  all_libs="stack queue mathlib arraylib linkedlist heap set hashmap bst graph lrucache taskqueue ticketservice orderservice"
  for run in $timing_runs; do
    for libk in ${libs_opt:-$all_libs}; do
      grep -qP "^$libk\t[^\t]*\t[^\t]*\t$run\t" "$out" && { echo "== depth $libk run $run: already in depth_timing.tsv"; continue; }
      lens="4 8 12 16 20 24"
      echo "== depth timing $libk run $run"
      ( cd "$TG/pl-platform-testing" && java -cp "$TOOLS_CP" in.ac.iiitb.plproject.atc.FairTiming lib "$libk" "$FW/depth" "$run" $lens ) \
          > "$FW/depth-$libk-$run.log" 2>&1
      grep -q '^ROW' "$FW/depth-$libk-$run.log" || { echo "fair.sh: depth timing failed for $libk, see $FW/depth-$libk-$run.log" >&2; tail -5 "$FW/depth-$libk-$run.log" >&2; }
      grep '^ROW' "$FW/depth-$libk-$run.log" | while IFS=$'\t' read -r _ sc name calls r st rounds runs spf tot wall det; do
        printf '%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' "$sc" "${name%@*}" "$calls" "$r" "$st" "$rounds" "$runs" "$spf" "$tot" "$wall" "$det"
      done | tee -a "$out"
    done
  done
  exit 0
fi

for bug in "${need_bugs[@]}"; do
  echo "== prepare $bug"
  prepare_bug "$bug" || exit 1
done
if (( ${#gen_tools[@]} )); then
  [[ " ${gen_tools[*]} " == *-timed* && ! -f "$HERE/timing.tsv" ]] && { echo "fair.sh: run --timing first (fair/timing.tsv)" >&2; exit 1; }
  echo "== generating ${gen_tools[*]} for ${#need_bugs[@]} version(s), seeds $seeds, budget ${BUDGET}s (-b180: 180 s), $GEN_JOBS at a time"
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
    if [[ "$tool" == randpre* ]]; then   # RandPre: the long string and all strings are two suites, as for TestGen
      for which in long all; do
        printf '%s\t%s-s%s\t%s\n' "$tool-$which" "$tool-$which" "$seed" "$FW/gen/$1/$tool-s$seed/$which/src"
      done
    else
      printf '%s\t%s-s%s\t%s\n' "$tool" "$tool" "$seed" "$FW/gen/$1/$tool-s$seed/src"
    fi
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
  # read by FairDriver (next to the sources): the removed PRE assertions count as assertions_removed
  pre_strip "$3" "$FW/testgen/$1/$2"
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
    echo "projectClasses=$BW/fixed-classes"; echo "validity=$K/$VAL_NAME"; echo "rows=$HERE/$key.fair.rows.tsv"
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
  [[ -f "$BW/$BUGROWS_NAME" ]] && (( ! redo_bugs )) && { echo "== $bug: already run (--redo-bugs to run again)"; continue; }
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
    echo "validity=$BW/$VAL_NAME"; echo "rows=$BW/$BUGROWS_NAME"; common_props "$BW/run"; } > "$BW/fair.properties"
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
  # then the runs of the tools added later, one file per run (the rows above stay as they were)
  # (oldest file first; a suite in several runs keeps its row from the latest run)
  ls -tr "$FW"/classes/*/validity.*.tsv "$FW"/bugs/*/validity.*.tsv 2>/dev/null | xargs -r cat \
    | awk -F'\t' '{k=$1 FS $3; if (!(k in row)) ord[++n]=k; row[k]=$0} END {for (i=1;i<=n;i++) print row[ord[i]]}' 
} > "$HERE/validity.tsv"
{
  printf 'bug\ttool\tsuite\ttests\tkept\tresult\ttest\tcall\tmessage\n'
  for f in "$FW"/bugs/*/bug.rows.tsv; do [[ -f "$f" ]] && tail -n +2 "$f"; done
  ls -tr "$FW"/bugs/*/bug.rows.*.tsv 2>/dev/null | while read -r f; do tail -n +2 "$f"; done \
    | awk -F'\t' '{k=$1 FS $3; if (!(k in row)) ord[++n]=k; row[k]=$0} END {for (i=1;i<=n;i++) print row[ord[i]]}' 
} > "$HERE/bugs.fair.tsv"
# RandPre's generation log: one row per (version, tool, seed, long/all, string)
{
  printf 'bug\ttool\tseed_dir\twhich\tstring\tcalls\tstatus\tat_call\tdraws_total\tdraws_per_call\tbudget\tms\tseed\tpool\n'
  for f in "$FW"/gen/*/randpre*-s*/*/randpre.tsv; do
    [[ -f "$f" ]] || continue
    w=$(basename "$(dirname "$f")"); t=$(basename "$(dirname "$(dirname "$f")")"); b=$(basename "$(dirname "$(dirname "$(dirname "$f")")")")
    tail -n +2 "$f" | sed "s|^|$b\t${t%-s*}\t$t\t$w\t|"
  done
} > "$HERE/randpre.tsv"
report
