#!/usr/bin/env bash
# Run the Defects4J CLI with Java 11, without changing the system's default Java.
#   ./d4j.sh info -p Collections
#   ./d4j.sh checkout -p Collections -v 15b -w /tmp/c15b
set -euo pipefail

D4J_HOME="${D4J_HOME:-$HOME/defects4j}"

# Defects4J v3 needs Java 11. Use JAVA11_HOME if set, else the first JDK 11 under /usr/lib/jvm.
if [[ -z "${JAVA11_HOME:-}" ]]; then
  for d in /usr/lib/jvm/java-11-openjdk-* /usr/lib/jvm/*-11-*; do
    [[ -x "$d/bin/javac" ]] && { JAVA11_HOME="$d"; break; }
  done
fi
if [[ -z "${JAVA11_HOME:-}" || ! -x "$JAVA11_HOME/bin/javac" ]]; then
  echo "d4j.sh: no Java 11 JDK found. Install it (sudo apt install openjdk-11-jdk-headless) or set JAVA11_HOME." >&2
  exit 1
fi
if [[ ! -x "$D4J_HOME/framework/bin/defects4j" ]]; then
  echo "d4j.sh: Defects4J not found at $D4J_HOME. Run setup.sh first, or set D4J_HOME." >&2
  exit 1
fi

export JAVA_HOME="$JAVA11_HOME"
export PATH="$D4J_HOME/framework/bin:$JAVA11_HOME/bin:$PATH"
export TZ="${TZ:-America/Los_Angeles}"   # some Defects4J tests assume this time zone

exec defects4j "$@"
