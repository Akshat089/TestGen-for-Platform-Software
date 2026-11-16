# TestGen vs Randoop vs EvoSuite on Defects4J real bugs

Run on 2026-11-16. Raw data: `outputs/<bug>/result.txt` (TestGen) and
`baselines/results.csv` (Randoop, EvoSuite; one row per tool, bug and seed).

## Summary

| Tool | Bugs detected (of 12) | Detections per run, on average |
| --- | --- | --- |
| **TestGen**, long test string only | **4** | 4 (one run; TestGen is deterministic) |
| **TestGen**, long + bug-focused short strings | **8** | 8 (one run) |
| Randoop 4.3.4 | 4 (in at least 1 of 10 runs) | 1.6 |
| EvoSuite 1.1.0 | 4 (in at least 1 of 10 runs) | 1.9 |

- "Bugs detected" for Randoop and EvoSuite counts a bug if **any** of its 10
  runs caught it, which favours them. In a single run they catch 1.6 and 1.9
  bugs on average (= total detections / 10).
- TestGen with its general long string alone matches both baselines on bugs
  detected (4 each) and beats them per run (4 vs 1.6 / 1.9).
- **3 bugs were caught only by TestGen** (Collections-5, -6, -16); 1 only by the
  baselines (Lang-59); 3 by no tool (Collections-1, -22, Lang-61).

## Per bug

Randoop and EvoSuite: detected in *k* of 10 runs (seeds 1–10). TestGen: ✅ caught / ❌ missed.

| Bug | Class | TestGen, long string | TestGen, + short strings | Randoop | EvoSuite |
| --- | --- | --- | --- | --- | --- |
| Collections-4 | MultiValueMap | ✅ | ✅ | **10/10** | **10/10** |
| Collections-5 | SetUniqueList | ❌ | ✅ | 0/10 | 0/10 |
| Collections-6 | Flat3Map | ✅ | ✅ | 0/10 | 0/10 |
| Collections-15 | SetUniqueList | ❌ | ✅ | 0/10 | **6/10** |
| Collections-16 | SetUniqueList | ❌ | ✅ | 0/10 | 0/10 (10 broken, see below) |
| Collections-19 | SetUniqueList | ✅ | ✅ | 0/10 | 1/10 |
| Collections-20 | TreeList | ✅ | ✅ | 2/10 | 0/10 |
| Collections-21 | SetUniqueList (4.x) | ❌ | ✅ | 2/10 | 0/10 |
| Lang-59 | StrBuilder | ❌ | ❌ | 2/10 | 2/10 |
| Collections-1 | Flat3Map | ❌ | ❌ | 0/10 | 0/10 (1 broken) |
| Collections-22 | ListOrderedMap (4.x) | ❌ | ❌ | 0/10 | 0/10 |
| Lang-61 | StrBuilder | ❌ | ❌ | 0/10 | 0/10 |
| **Total** | | **4** | **8** | **16/120** | **19/120** |

The check that failed for each TestGen detection is listed in `README.md`.

## Setup

| | TestGen | Randoop | EvoSuite |
| --- | --- | --- | --- |
| Bugs | the 12 above | same | same |
| Protocol | generate on the fixed version, drop tests that fail there, run the rest on the buggy version; detected = a test fails | same (Defects4J `gen_tests.pl`, `fix_test_suite.pl`, `run_bug_detection.pl`) | same |
| Code under test | the real class, called through a thin adapter (`libraries/*/Helper.java`) | the real class | the real class |
| Input | contract (`.spec`) + test strings (`.tests`), written by hand | the class | the class |
| Time | 7–21 s per bug (measured; not limited) | 60 s per class | 60 s per class |
| Runs | 1 (deterministic) | 10 seeds | 10 seeds |
| Settings | as checked in | Defects4J defaults | Defects4J defaults |
| Machine, Java | same WSL machine (16 cores, 7 GB), Java 11 | same | same |

Defects4J at revision `8c16da82` (v3.0.1 + 7 commits). Randoop and EvoSuite ran
4 jobs in parallel (load about 13 of 16 cores), so each kept roughly a core of
its own during its 60 s budget.

## What to keep in mind when reading the numbers

1. **TestGen's input is written by a person; the baselines get only the class.**
   The time to write a contract and test strings is not in the 7–21 s.
2. **The short test strings were written with each bug's report in view**
   (e.g. `add -> addAllAt -> get -> count` for Collections-5). The 4 bugs they
   add (5, 15, 16, 21) are therefore not a blind result. The **long string per
   class** was written to exercise every operation, not any bug, so the
   *long string only* row is the fair head-to-head.
3. **The checks differ by design.** TestGen checks the contract; Randoop and
   EvoSuite record what the fixed version returns and check that the buggy
   version returns the same (regression assertions). This is the standard
   Defects4J setup.
4. **The 12 bugs are the ones TestGen can express.** Defects4J has 4 more in
   these classes (Lang-47, Lang-60, Collections-10, -27) that need inputs or
   behaviour TestGen does not support (null/char inputs, serialization). The
   baselines were not run on them.

## Broken EvoSuite suites (11 of 120), counted as not detected

A suite is *Broken* when it cannot be run on one of the versions.

- **Collections-16, all 10 seeds.** The fix for this bug added a new method,
  `SetUniqueList.createSetBasedOnList`. EvoSuite's tests call it directly
  (e.g. `SetUniqueList_ESTest.java`, lines 336 and 345), so they do not compile
  against the buggy version, where the method does not exist.
- **Collections-1, seed 8.** One generated test (`Flat3Map_ESTest::test83`)
  recurses through `Flat3Map`'s entry iterator `toString` until
  `StackOverflowError`, also on the fixed version; it fails only sometimes, so
  Defects4J's cleanup step did not remove it.

Randoop had no broken suites.

## Why TestGen missed 4 bugs

All four are reached by the right calls in the right order, but with values
for which the bug does not show. SPF's solver returns the simplest values that
satisfy the preconditions (0, or the first stored element):

| Bug | TestGen chose | The bug needs |
| --- | --- | --- |
| Collections-1 | `setValue` on the 1st entry | the 2nd or 3rd entry |
| Collections-22 | `putAll` at index 0 | an index after the existing keys |
| Lang-59 | pad width 0 (the method then does nothing) | `0 < width < s.length()` |
| Lang-61 | search for the first string appended | the last one, after it was deleted |

Lang-59 is the one bug the random tools caught and TestGen did not: they try
many widths, and 2 of 10 runs hit one in that range.

## Reproduce

```bash
cd benchmarks/defects4j
./run.sh                                          # TestGen, all 12 bugs
./baselines.sh -b 60 -s "1 2 3 4 5 6 7 8 9 10"    # Randoop + EvoSuite (about 3 h; split with -t randoop / -t evosuite to run in parallel)
```
