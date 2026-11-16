#!/usr/bin/env bash
# Run TestGen on Defects4J bugs: generate on the fixed version, drop tests that fail
# there, run the rest on the buggy version.
#   ./run.sh            # all 7 bugs in bugs.csv
#   ./run.sh 15 19      # only these bugs
# Results: outputs/Collections-<id>/result.txt, plus a summary at the end.
# Needs: TestGen built (./scripts/setup-spf.sh, then mvn install) and the bugs checked out.
set -uo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TG="$(cd "$HERE/../.." && pwd)"
WORK="${WORK:-$HERE/work}"

# Java 11, as TestGen's README requires; does not touch the system default.
if [[ -z "${JAVA11_HOME:-}" ]]; then
  for d in /usr/lib/jvm/java-11-openjdk-*; do [[ -x "$d/bin/javac" ]] && { JAVA11_HOME="$d"; break; }; done
fi
[[ -x "${JAVA11_HOME:-}/bin/javac" ]] || { echo "run.sh: no Java 11 JDK found; set JAVA11_HOME" >&2; exit 1; }
export JAVA_HOME="$JAVA11_HOME" PATH="$JAVA11_HOME/bin:$PATH"
# SPF loads the Z3 solver's native library (libz3java.so) from jpf-symbc/lib; the SPF process
# TestGen starts inherits this variable.
SPF_HOME_DIR="${GENATC_SPF_HOME:-$HOME/.genatc-spf}"
export LD_LIBRARY_PATH="$SPF_HOME_DIR/jpf-symbc/lib${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}"

CLASSES="$TG/pl-platform-testing/target/classes"
[[ -f "$CLASSES/in/ac/iiitb/plproject/atc/GenatcMain.class" ]] || {
  echo "run.sh: TestGen is not built. From $TG run: ./scripts/setup-spf.sh && mvn -q install -DskipTests" >&2; exit 1; }
JUNIT=$(find "$HOME/.m2/repository/junit" -name 'junit-4.13.1.jar' 2>/dev/null | head -1)
HAMCREST=$(find "$HOME/.m2/repository/org/hamcrest" -name 'hamcrest-core-1.3.jar' 2>/dev/null | head -1)
CP="$CLASSES:$JUNIT:$HAMCREST"

# Build the runner (one class, in TestGen's atc package; see runner/).
mkdir -p "$HERE/build/runner"
javac -nowarn -d "$HERE/build/runner" -cp "$CP" "$HERE/runner/in/ac/iiitb/plproject/atc/D4jMain.java" || exit 1

# bug -> adapter folder and spec name
library_of() {
  case "$1" in
    Collections-1|Collections-6)    echo "flat3map Flat3Map" ;;
    Collections-5|Collections-15|Collections-16|Collections-19)
                                    echo "setuniquelist SetUniqueList" ;;
    Collections-21)                 echo "setuniquelist4 SetUniqueList4" ;;
    Collections-22)                 echo "listorderedmap4 ListOrderedMap4" ;;
    Collections-20)                 echo "treelist TreeList" ;;
    Collections-4)                  echo "multivaluemap MultiValueMap" ;;
    Lang-59|Lang-61)                echo "strbuilder StrBuilder" ;;
    *)                              return 1 ;;
  esac
}

# The Helper first, then every library source file it needs (found by compiling it).
sources_for() {   # $1 checkout dir, $2 Helper.java
  local dir="$1" helper="$2" srcdir tmp
  srcdir="$dir/$(grep '^d4j.dir.src.classes=' "$dir/defects4j.build.properties" | cut -d= -f2)"
  tmp="$(mktemp -d)"
  if ! javac -nowarn -implicit:class -sourcepath "$srcdir" -d "$tmp" "$helper" > "$tmp/javac.log" 2>&1; then
    echo "run.sh: the adapter does not compile against $dir:" >&2; cat "$tmp/javac.log" >&2; rm -rf "$tmp"; return 1
  fi
  echo "$helper"
  ( cd "$tmp" && find org -name '*.class' ! -name '*$*' | sort ) | sed 's/\.class$/.java/' | while read -r f; do
    [[ -f "$srcdir/$f" ]] && echo "$srcdir/$f"
  done
  rm -rf "$tmp"
}

bugs=()
for a in "$@"; do if [[ "$a" =~ ^[0-9]+$ ]]; then bugs+=("Collections-$a"); else bugs+=("$a"); fi; done
(( ${#bugs[@]} )) || mapfile -t bugs < <(tail -n +2 "$HERE/bugs.csv" | cut -d, -f1)

summary=()
for name in "${bugs[@]}"; do
  if ! lib="$(library_of "$name")"; then
    summary+=("$(printf '%-16s %-15s %s' "$name" "-" "NO ADAPTER")")
    continue
  fi
  read -r libdir spec <<< "$lib"
  [[ -d "$WORK/${name}b" && -d "$WORK/${name}f" ]] || "$HERE/checkout.sh" "$name" || exit 1

  out="$HERE/outputs/$name"
  mkdir -p "$out"
  helper="$HERE/libraries/$libdir/Helper.java"
  sources_for "$WORK/${name}f" "$helper" > "$out/fixed-sources.txt" || exit 1
  sources_for "$WORK/${name}b" "$helper" > "$out/buggy-sources.txt" || exit 1
  dups=$(xargs -n1 basename < "$out/fixed-sources.txt" | sort | uniq -d)
  [[ -z "$dups" ]] || { echo "run.sh: two source files share a name ($dups); TestGen copies them into one folder" >&2; exit 1; }

  echo "== $name ($spec, $(($(wc -l < "$out/fixed-sources.txt") - 1)) library files) =="
  ( cd "$HERE" && java -cp "$HERE/build/runner:$CP" in.ac.iiitb.plproject.atc.D4jMain \
      "$name" "specs/$spec.spec" "specs/$spec.tests" "outputs/$name" \
      "$out/fixed-sources.txt" "$out/buggy-sources.txt" ) > "$out/run.log" 2>&1
  rc=$?
  sed -n '/^---------------- Defects4J result/,$p' "$out/run.log" | tail -n +2
  case $rc in 0) r="DETECTED" ;; 1) r="NOT DETECTED" ;; *) r="NO TEST / ERROR (see outputs/$name/run.log)" ;; esac
  summary+=("$(printf '%-16s %-15s %s' "$name" "$spec" "$r")")
done

echo
echo "================ summary ================"
printf '%s\n' "${summary[@]}"
