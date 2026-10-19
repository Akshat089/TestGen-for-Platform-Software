#!/usr/bin/env bash
# Builds Java PathFinder (jpf-core) and Symbolic PathFinder (jpf-symbc) from
# source and installs the jars GENATC needs into the local Maven repository.
#
# Neither project is on Maven Central, so this is the practical alternative:
# clone the two repos at pinned commits, build them with Gradle/Ant, then
# `mvn install:install-file` the resulting jars under fixed coordinates that
# pl-platform-testing/pom.xml depends on (scope `provided`, since generated
# JUnit classes must never import gov.nasa.jpf — rule 11).
#
# Requires: JDK 11 (both projects build and run cleanly under 11; jpf-core's
# own `java-8` branch exists but jpf-symbc's prebuilt class files are 52.0 and
# choke when *read* by a JDK 8 javac sitting behind a JDK 11-built jpf-core —
# simplest fixed point is JDK 11 everywhere, confirmed by actually building
# both projects clean with it during this change), git, ant, and a network
# connection for the one-time clone. Already-built checkouts are reused.
#
# Usage:
#   JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 ./scripts/setup-spf.sh
#   ./scripts/setup-spf.sh --spf-home ~/spf-src     # custom checkout location
#
# After this script succeeds, `mvn -o install` from the repo root can build
# GENATC's symbolic driver + listener against real SPF.
set -euo pipefail

JPF_CORE_COMMIT="f70927ce05b165addb02e9c232af74e9737e09bf"   # jpf-core, master, 2025-12-16
JPF_SYMBC_COMMIT="3bedf0b168b3c85dd0f05434dd61c4c011cc447a"  # jpf-symbc, master, 2022-06-08

JPF_CORE_REPO="https://github.com/javapathfinder/jpf-core.git"
JPF_SYMBC_REPO="https://github.com/javapathfinder/jpf-symbc.git"

MVN_JPF_CORE_VERSION="8.0-f70927c"
MVN_JPF_SYMBC_VERSION="1.0-3bedf0b"

SPF_HOME="${SPF_HOME:-$HOME/.genatc-spf}"
while [ $# -gt 0 ]; do
    case "$1" in
        --spf-home) SPF_HOME="$2"; shift 2 ;;
        *) echo "unknown argument: $1" >&2; exit 1 ;;
    esac
done

JAVA_VER="$(java -version 2>&1 | head -1)"
case "$JAVA_VER" in
    *'"11'*|*'"12'*|*'"13'*|*'"14'*|*'"15'*|*'"16'*|*'"17'*|*'"18'*|*'"19'*|*'"20'*|*'"21'*) ;;
    *) echo "WARNING: expected JDK 11+ on PATH, found: $JAVA_VER" >&2 ;;
esac

mkdir -p "$SPF_HOME"
CORE_DIR="$SPF_HOME/jpf-core"
SYMBC_DIR="$SPF_HOME/jpf-symbc"

echo "== jpf-core -> $CORE_DIR (commit $JPF_CORE_COMMIT) =="
if [ ! -d "$CORE_DIR/.git" ]; then
    git clone "$JPF_CORE_REPO" "$CORE_DIR"
fi
git -C "$CORE_DIR" checkout "$JPF_CORE_COMMIT"

echo "== building jpf-core (gradlew buildJars) =="
( cd "$CORE_DIR" && ./gradlew --no-daemon buildJars )

echo "== jpf-symbc -> $SYMBC_DIR (commit $JPF_SYMBC_COMMIT) =="
if [ ! -d "$SYMBC_DIR/.git" ]; then
    git clone "$JPF_SYMBC_REPO" "$SYMBC_DIR"
fi
git -C "$SYMBC_DIR" checkout "$JPF_SYMBC_COMMIT"

# jpf-symbc's build.xml reads ~/.jpf/site.properties for the jpf-core location
# if present, and otherwise defaults to a `../jpf-core` peer directory. Point
# it explicitly at the checkout above so this script does not depend on (or
# clobber) any pre-existing JPF installation on the machine.
mkdir -p "$HOME/.jpf"
SITE_PROPS="$HOME/.jpf/site.properties"
if [ -f "$SITE_PROPS" ] && ! grep -q "genatc-managed" "$SITE_PROPS" 2>/dev/null; then
    echo "NOTE: leaving existing $SITE_PROPS alone (not genatc-managed);"
    echo "      building jpf-symbc via an explicit -Djpf-core instead."
    JPF_CORE_PROP="-Djpf-core=$CORE_DIR"
else
    cat > "$SITE_PROPS" <<EOF
# genatc-managed — written by scripts/setup-spf.sh
jpf-core = $CORE_DIR
jpf-symbc = $SYMBC_DIR
extensions = \${jpf-core},\${jpf-symbc}
EOF
    JPF_CORE_PROP=""
fi

echo "== building jpf-symbc (ant build) =="
( cd "$SYMBC_DIR" && ant -q ${JPF_CORE_PROP:+-Djpf-core="$CORE_DIR"} build )

echo "== installing jars into the local Maven repository =="
# jpf-core's build produces two jars with disjoint class sets: build/jpf-classes.jar
# holds only the small set of annotation/model classes visible to a MODELED (target)
# program, while build/jpf.jar is the actual host-side engine — Config, JPF,
# PropertyListenerAdapter, Search, VM, ChoiceGenerator, all of which the generator's
# own JPF listener (compiled against this `provided` dependency) needs. The
# generated DRIVER's own compile step (javac, run by SpfRunner, not Maven) uses
# jpf-classes.jar directly from this checkout instead — see SpfHome.java.
mvn -q install:install-file \
    -Dfile="$CORE_DIR/build/jpf.jar" \
    -DgroupId=gov.nasa.jpf -DartifactId=jpf-core -Dversion="$MVN_JPF_CORE_VERSION" \
    -Dpackaging=jar
mvn -q install:install-file \
    -Dfile="$SYMBC_DIR/build/jpf-symbc.jar" \
    -DgroupId=gov.nasa.jpf -DartifactId=jpf-symbc -Dversion="$MVN_JPF_SYMBC_VERSION" \
    -Dpackaging=jar

cat <<EOF

== done ==
jpf-core  : $CORE_DIR  (engine jar at build/jpf.jar, runner at build/RunJPF.jar,
            target-side classes at build/jpf-classes.jar)
jpf-symbc : $SYMBC_DIR (engine jar at build/jpf-symbc.jar,
            target-side Debug class at build/jpf-symbc-classes.jar)

Installed into ~/.m2 as:
  gov.nasa.jpf:jpf-core:$MVN_JPF_CORE_VERSION
  gov.nasa.jpf:jpf-symbc:$MVN_JPF_SYMBC_VERSION

pl-platform-testing/pom.xml depends on exactly these coordinates (scope
provided) so the generator can compile its symbolic driver and JPF listener.

GENATC's own SPF *runner* launches SPF as a separate process using:
  $CORE_DIR/build/RunJPF.jar
  $CORE_DIR/build/jpf-classes.jar
  $SYMBC_DIR/build/jpf-symbc.jar
Set GENATC_SPF_HOME=$SPF_HOME if this is not the default (\$HOME/.genatc-spf)
the runner looks for.
EOF
