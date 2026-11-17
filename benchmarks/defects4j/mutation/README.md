# Mothra mutation testing on the Defects4J classes

Hundreds of Mothra mutants are planted in each of the 7 real classes from the Defects4J benchmark. The same mutants are run against TestGen's, Randoop's and EvoSuite's test suites. The score is killed ÷ (killed + survived), per tool and per class. Results are in [REPORT.md](REPORT.md).

```bash
cd benchmarks/defects4j
./mutation.sh                    # all 7 classes (about 50 min on 16 cores)
./mutation.sh treelist flat3map  # some classes
python3 mutation/report.py       # rebuild REPORT.md from the rows
```

The run needs:
- TestGen built (`mvn install`);
- the bugs checked out, and `./run.sh` run once (for `outputs/<bug>/`);
- the Randoop/EvoSuite runs from `./baselines.sh` in `baselines/runs/`.

Scratch files go to `~/.cache/testgen-d4j-mutation` (set `MUT_WORK` to change it). They are kept on the Linux file system, because each class starts thousands of test JVMs. A run that stops can be started again: it resumes from the rows already written.

## Classes

| Key | Class | Version mutated |
| --- | --- | --- |
| `setuniquelist` | `collections.list.SetUniqueList` | Collections-19f |
| `setuniquelist4` | `collections4.list.SetUniqueList` | Collections-21f |
| `flat3map` | `collections.map.Flat3Map` | Collections-6f |
| `multivaluemap` | `collections.map.MultiValueMap` | Collections-4f |
| `treelist` | `collections.list.TreeList` | Collections-20f |
| `listorderedmap4` | `collections4.map.ListOrderedMap` | Collections-22f |
| `strbuilder` | `lang.text.StrBuilder` | Lang-59f |

## Method

1. **Mutants.** `runner/.../mutation/D4jMutantGen.java` runs `OperatorMutantGenerator` from `genatc-mutation/` unchanged, with no cap, on the real class only. The adapter and the rest of Commons are never mutated. Two things are added on top:
   - **The exact line of every mutant.** It is recovered by replaying the generator's own line filter, through reflection. Each mutant is planted at that line, not with `Mutant.applyTo`. `applyTo` finds a method by its name: on overloads (`StrBuilder.append`, `indexOf`, …) it takes the first match, and it misses headers with `throws`, `synchronized` or more than one line.
   - **Which method each line is in, and whether the adapter can reach it.** The library is parsed and type-checked with the JDK's own compiler (javac Tree API). Reachability is a call graph built from every adapter method, using rapid type analysis (only instantiated classes count for virtual calls). It covers inner and anonymous classes, so TreeList's `AVLNode` and Flat3Map's entry iterator are included. When a call lands in a JDK superclass (e.g. `TreeList.add(E)` is `AbstractList.add`), the class's overrides of JDK methods count as reachable, because the JDK code can call them back.
   
   Mutants in unreachable code are left out, since no tool's test of the adapter can reach them. So are mutants outside any method or initializer, and on `serialVersionUID`. Every mutant also records whether its method is one the adapter calls **directly**, and REPORT.md gives the score for that stricter scope too. Output: `<key>.mutants` (the usual format, `in:` = the exact method), `<key>.mutants.tsv` (with the line) and `<key>.scope.txt` (counts, and which methods are in and out).
2. **Compile check.** `runner/.../mutation/D4jMutationRun.java` compiles every mutant, but only the mutated file, against the project's compiled classes.
   - **INVALID:** the mutant does not compile.
   - **TCE (trivially equivalent):** its class files are byte-identical to the original's.
   
   Both are left out for every tool.
3. **Sample.** A seeded random sample (seed 1) of 300 valid mutants per class, or all of them if fewer. A random sample does not favour the top of the file.
4. **Suites.** Every suite was generated on the same fixed version that is mutated. None is regenerated per mutant.
   - **TestGen**, two suites:
     - *long string only*: generated here once, from a `.tests` file holding only the first test block;
     - *+ short strings*: the existing `outputs/<bug>/fixed/.../GeneratedATCsTest.java`.
   - **Randoop and EvoSuite**, one suite per seed (10 each): from the archives `baselines.sh` left behind, after Defects4J's cleanup. A Randoop suite is the `RegressionTest<n>` classes; an EvoSuite suite is the `*_ESTest` classes, run with their scaffolding and the EvoSuite runtime jar.
5. **Baseline.** Every suite is run twice on the unmutated build.
   - A test class that runs no test is left out of its suite.
   - A suite with any failure, or with no test class left, is dropped and listed in REPORT.md.
6. **Run.** Every (sampled mutant, kept suite) pair runs in its own JVM (`MutantTestMain`), with the mutant's classes first on the classpath and the same settings for every tool:
   - 30 s wall-clock limit;
   - `-Xmx512m`;
   - headless;
   - `TZ=America/Los_Angeles`.
   
   A mutant is **KILLED** by a suite if any test fails or throws, or the JVM times out or crashes. Otherwise it **SURVIVED**. There is one row per pair in `<key>.rows.tsv`.
7. **Generated family (step 7, `./mutation.sh --family`).** `runner/.../atc/D4jFamily.java` does for each class what phase 1 does for your own libraries:
   - `TestStringGenerator` draws 260 test strings from the spec, seeded with the class's long string only (never the bug-focused short ones), so the result stays blind;
   - `TestCache` generates each one's test once on the fixed version, with SPF as usual, and runs it there;
   - the passing tests become one more suite, `testgen-family`, run against every scored mutant under the same rules.
   
   The report adds "TestGen, long string + generated family (blind)" and "TestGen, all strings + generated family". `<key>.family.tsv` lists every test string with its status. Generation takes about 10 s per test string, 4 classes at a time.
8. **Report.**
   - Randoop and EvoSuite are reported as the median of 10 seeds with min–max, plus "any of 10 seeds" for comparison with the real-bug table.
   - The total over all classes adds seed *i* of every class.
   - REPORT.md also gives per-operator scores, which mutants only one tool kills, and how each kill happened.

## Changes from the plan (Nov 16)

| Plan | Here | Why |
| --- | --- | --- |
| Plant with `Mutant.applyTo` | Plant at the recorded line | `applyTo` picks the first method with that name |
| Keep mutants in methods the adapter calls | Keep everything reachable from them; also report the directly-called scope | Most of TreeList is its node class, which the adapter never calls by name |
| Cap = first 300 | Seeded random 300 of the valid mutants | The first 300 favour the top of the file; INVALID ones would use up the cap |
| `TestCache.runTests` | Own runner, still `MutantTestMain` | `TestCache` uses 128 MB, a time limit that depends on the number of classes, and no EvoSuite runtime |
| `TestCache.compileLibrary` per mutant | Compile only the mutated file, placed first on the classpath | Recompiling all of Commons for every mutant takes hours |
| (not stated) | Byte-identical mutants (TCE) are left out | They cannot be killed by anyone |

## Limits to keep in mind

- The generator is line-based, so some mutants on Commons code do not compile (INVALID, counted per class). A line that appears twice in one method gets no mutant. Both apply to every tool equally.
- The call graph over-approximates. Dispatch counts every instantiated subtype, and JDK callbacks count every override. "Direct" is the strict scope.
- A mutated `static final` constant is not seen by other classes that copied it at compile time. Such mutants just look equivalent, for every tool.
- Survivors include equivalent mutants (same behaviour, cannot be killed). Every equivalent mutant is among those no suite killed. `equivalent.tsv` holds a hand-checked random sample of those (up to 20 per class, with a reason for each verdict). REPORT.md gives an estimate for all classes, and the scores with the confirmed ones removed. The report reads `equivalent.tsv`; add rows to it to check more.
