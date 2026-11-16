# Defects4J for the TestGen benchmark

Real-bug check for the Apache Commons Collections benchmark: the 7 Defects4J
bugs in `SetUniqueList` and `Flat3Map` (see `bugs.csv`).

Defects4J itself is **not** stored in this repo. It lives at `$D4J_HOME`
(default `~/defects4j`), pinned to revision `8c16da82` (v3.0.1 + 7 commits).
This folder only holds the scripts that set it up and use it. Nothing else in
TestGen depends on it.

## Setup (once, in WSL/Ubuntu)

```bash
cd benchmarks/defects4j
./setup.sh          # prints the one `sudo apt install ...` line if anything is missing
./setup.sh          # re-run after installing; downloads repos + tools (~1-2 GB, takes a while)
```

Defects4J v3 needs **Java 11**. `d4j.sh` runs Defects4J with Java 11 for that
command only, so your default `java` (used by TestGen/JPF) is not changed.
Override locations with `D4J_HOME=...` or `JAVA11_HOME=...`.

## Use

```bash
./checkout.sh --verify      # check out + compile 7 bugs x {buggy, fixed} into ./work,
                            # and confirm each trigger test fails on buggy, passes on fixed
./checkout.sh 15 19         # only some bugs
./d4j.sh info -p Collections -b 15    # any Defects4J command, with Java 11
```

`./work/Collections-<id>b` is the buggy version, `./work/Collections-<id>f` the
fixed one. `work/` is git-ignored.

Protocol (from the benchmark plan): generate tests on the **fixed** version,
drop any test that fails there (false alarm), then run the rest on the
**buggy** version. A bug is detected if at least one test fails.

`setup.sh` also downloads the Randoop and EvoSuite jars that Defects4J ships
(`$D4J_HOME/framework/lib/test_generation/generation/`), which are two of the
baselines in the plan.

## Running TestGen on the bugs

Laid out like the repo root, but nothing outside this folder is changed:

| Path | What it is |
| --- | --- |
| `libraries/setuniquelist/Helper.java` | adapter over the real 3.x `SetUniqueList` (bugs 5, 15, 16, 19) |
| `libraries/setuniquelist4/Helper.java` | adapter over collections4 `SetUniqueList` (bug 21) |
| `libraries/flat3map/Helper.java` | adapter over the real `Flat3Map` (bugs 1, 6) |
| `libraries/strbuilder/Helper.java` | adapter over Commons Lang `StrBuilder` (Lang-59, Lang-61) |
| `libraries/listorderedmap4/Helper.java` | adapter over collections4 `ListOrderedMap` (Collections-22) |
| `libraries/treelist/Helper.java` | adapter over `TreeList` (Collections-20) |
| `libraries/multivaluemap/Helper.java` | adapter over `MultiValueMap` (Collections-4) |
| `specs/*.spec`, `specs/*.tests` | contracts and test strings, same language as `../../specs` |
| `runner/.../D4jMain.java` | runs TestGen's `GenatcMain.runOne` on a library that is not in `LibraryRegistry` |
| `run.sh` | per bug: generate on the fixed version, drop tests failing there, run the rest on the buggy version |

Unlike `../../libraries`, each `Helper.java` here contains no data structure:
it forwards each call to the real Commons Collections class (compiled from the
Defects4J checkout next to it) and copies the state the contract reads into
static fields. All checks are in the `.spec`.

```bash
# once, from the repo root (Java 11):  ./scripts/setup-spf.sh && mvn -q install -DskipTests
./run.sh            # all 7 bugs;  ./run.sh 15 19  for some
```

`run.sh` sets `LD_LIBRARY_PATH` to `~/.genatc-spf/jpf-symbc/lib`, where SPF's Z3
solver library (`libz3java.so`) is. Without it every SPF round fails with
`UnsatisfiedLinkError: no libz3java` (this also affects `../../verify-generated.sh`).

Per-bug output: `outputs/Collections-<id>/result.txt`, `run.log`, and the usual
TestGen outputs under `fixed/` (ATC.txt, GeneratedATCsTest.java, spf-work/).

### Results (2026-11-16): 8 of 12 detected

| Bug | Class | Result | Failing check on the buggy version |
| --- | --- | --- | --- |
| Collections-4 | MultiValueMap | detected | `POST put: r == value` |
| Collections-5 | SetUniqueList | detected | `POST addAllAt: L.get(index) == a` |
| Collections-6 | Flat3Map | detected | `POST remove: removedVal == stored` |
| Collections-15 | SetUniqueList | detected | `POST set: L.contains(e)` |
| Collections-16 | SetUniqueList | detected | `POST subList: subTrackedSize == subSize` |
| Collections-19 | SetUniqueList | detected | `POST setExisting: L.contains(e)` |
| Collections-20 | TreeList | detected | `POST iterPrevious: elem == L.get(itNextIndex)` |
| Collections-21 | SetUniqueList (4.x) | detected | `POST subRemoveAt: changed == false` |
| Collections-1 | Flat3Map | not detected | value-dependent (below) |
| Collections-22 | ListOrderedMap (4.x) | not detected | value-dependent (below) |
| Lang-59 | StrBuilder | not detected | value-dependent (below) |
| Lang-61 | StrBuilder | not detected | value-dependent (below) |

**All 4 misses have the same cause:** the solver picks the simplest values
that satisfy the preconditions (0, or the first stored element), and each bug
only shows for a different value. The contracts can express every one of them.

- **Collections-1.** The buggy `setValue` falls through downward: setting
  entry 3 also overwrites entries 2 and 1, entry 2 overwrites entry 1, entry 1
  overwrites nothing. The solver picks the first stored key (entry 1).
- **Collections-22.** `putAll(index, ...)` over existing keys holding null goes
  wrong only when `index` is after those keys. The solver picks `index = 0`.
- **Lang-59.** `appendFixedWidthPadRight` overflows only when
  `0 < width < s.length()`. The solver picks `width = 0`, where the method does
  nothing.
- **Lang-61.** `indexOf` returns early on empty content, so stale text is only
  found when it sits after non-empty content, i.e. after deleting or truncating
  the *last* string appended. The solver picks the first one.- **Each "fixed" version still has the other bugs**, so some test strings fail
  on it and are dropped as false alarms (listed in `result.txt`). On 5f, 15f
  and 16f the 16-call dry run is even rejected (INCORRECT SEQUENCE): those
  versions contain bug 19, `setExisting` drops an element from the internal
  set, and the next `removeElement` precondition (`L.contains(e)`) can no
  longer be met.

## Notes on the bugs

- Bugs 1-19 are in Commons Collections 3.x (`org.apache.commons.collections`),
  bug 21 in collections4 (`org.apache.commons.collections4`).
- Collections-1 is in `Flat3Map`'s entry iterator (`setValue`), not in
  `put`/`remove`, so it needs an iterator adapter method to be reachable.
