#!/usr/bin/env bash
# One-time setup of Defects4J for the TestGen benchmark.
# Installs nothing inside the TestGen repo: Defects4J lives at $D4J_HOME (default ~/defects4j).
# Safe to re-run.
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
D4J_HOME="${D4J_HOME:-$HOME/defects4j}"
D4J_REPO="https://github.com/rjust/defects4j.git"
D4J_REV="${D4J_REV:-8c16da82}"   # v3.0.1 + 7 commits; pinned so results are reproducible

APT_PKGS="openjdk-11-jdk-headless subversion git curl wget unzip cpanminus build-essential \
libdbi-perl libdbd-csv-perl liburi-perl libjson-perl libjson-parse-perl libstring-interpolate-perl"

# 1. System prerequisites (needs sudo, so we only check and tell you what to run).
missing=()
for t in svn git curl wget unzip cpanm make; do command -v "$t" >/dev/null || missing+=("$t"); done
for m in DBI DBD::CSV URI JSON JSON::Parse String::Interpolate; do
  perl -M"$m" -e1 2>/dev/null || missing+=("perl:$m")
done
ls -d /usr/lib/jvm/java-11-openjdk-* >/dev/null 2>&1 || [[ -n "${JAVA11_HOME:-}" ]] || missing+=("java11")
if (( ${#missing[@]} )); then
  echo "Missing: ${missing[*]}"
  echo "Run this once, then re-run setup.sh:"
  echo "  sudo apt update && sudo apt install -y $APT_PKGS"
  echo "(Installing Java 11 does not change your default 'java'.)"
  exit 1
fi

# 2. Get Defects4J at the pinned revision.
if [[ ! -d "$D4J_HOME/.git" ]]; then
  git clone "$D4J_REPO" "$D4J_HOME"
  git -C "$D4J_HOME" checkout --quiet "$D4J_REV"
else
  have="$(git -C "$D4J_HOME" rev-parse --short=8 HEAD)"
  [[ "$have" == "$D4J_REV"* ]] || echo "Note: $D4J_HOME is at $have, pinned revision is $D4J_REV. Leaving it as is."
fi

# 3. Any Perl modules apt did not cover.
( cd "$D4J_HOME" && cpanm --quiet --notest --installdeps --skip-satisfied . ) || \
  echo "Note: cpanm reported a problem; Defects4J usually still works if the step-1 modules are present."

# 4. Download project repos and tools (Major, Randoop, EvoSuite, Gradle). Skips work already done.
if [[ ! -f "$D4J_HOME/major/bin/major" || ! -d "$D4J_HOME/project_repos/commons-collections.git" ]]; then
  J11="${JAVA11_HOME:-$(ls -d /usr/lib/jvm/java-11-openjdk-* | head -1)}"
  ( cd "$D4J_HOME" && JAVA_HOME="$J11" PATH="$J11/bin:$PATH" ./init.sh )
fi

# 5. Sanity check.
"$HERE/d4j.sh" info -p Collections | head -20
echo
echo "Defects4J is ready at $D4J_HOME. Next: $HERE/checkout.sh"
