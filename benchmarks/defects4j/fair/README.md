# Fair comparison: TestGen, Randoop and EvoSuite under the same rules

`../mutation/` and `../COMPARISON.md` compare the tools **natively**: Randoop and EvoSuite test the Commons class directly and keep their own regression assertions, while TestGen tests the adapter and checks the contract. Those results stay as they are. This folder adds a second, **fair** set of numbers. Here every tool:

- uses the same API (the adapter);
- has its calls checked against the same contract;
- is judged by the same checks;
- faces the same mutants.

```bash
cd benchmarks/defects4j
./fair/fair.sh                               # everything (7 classes, 12 bugs, 3 generators x 10 seeds, report)
./fair/fair.sh --classes "treelist" --bugs none --tools "testgen randoop" --seeds "1 2"
./fair/fair.sh --report                      # rebuild REPORT.md from the rows
```

The run needs what `mutation.sh` needs: TestGen built, Defects4J set up (`setup.sh`, which also provides the Randoop and EvoSuite jars), the bugs checked out (`checkout.sh`), `run.sh` run once and `mutation.sh` run once. Scratch files go to `$MUT_WORK/fair` (default `~/.cache/testgen-d4j-mutation/fair`). A run that stops can be started again: generated suites and mutant rows are kept and resumed.

## The rules

1. **Same API.** Randoop and EvoSuite generate tests for the adapter (`libraries/<key>/Helper.java`, class `in.ac.iiitb.plproject.atc.generated.Helper`), on the fixed version, not for the Commons class. So they can call only the operations TestGen can.
   - Randoop gets `--omit-methods` for `reset()` and `--omit-field` for the adapter's state fields.
   - EvoSuite cannot be limited like that, because it also calls methods on the objects it gets, such as `Helper.M.put(...)`. So the monitor enforces the rule instead (see rule 4):
     - when a test writes a state field directly, the call after it is invalid;
     - when a test calls a library method directly, the call after it is invalid. Such calls are marked when the oracles are stripped.
2. **Same starting state.** Every test starts from `Helper.reset()`.
   - Randoop: `--junit-before-each` with a file that calls `Helper.reset()`.
   - EvoSuite: its static-field reset is on (`-Dreset_static_fields=true`). `fair.sh` checks that each suite says `resetStaticState = true` and that the scaffolding resets `Helper`.
   - The monitor also logs every reset. The run stops if any test of any tool makes its first adapter call on an adapter another test already used.
3. **Same information.** Each tool is run directly from the jar Defects4J ships, at the version used before (`$D4J_HOME/framework/lib/test_generation/generation/`). `gen_tests.pl` cannot target the adapter. All runs use a budget of 60 s per class, seeds 1–10, the fixed version of each bug, and the same classpath (the fixed Commons classes plus the adapter).
   - **randoop**: the options of Defects4J's `randoop.config`, as in the native run.
   - **evosuite-default**: EvoSuite's own default criteria (no `-criterion`).
   - **evosuite-blackbox**: `-criterion METHOD:METHODNOEXCEPTION:OUTPUT:EXCEPTION`, so no branch or line coverage guides it. Randoop is black-box already.
   - Both EvoSuite settings split the 60 s as Defects4J's `evosuite.sh` does: 30 s of search, then 30 s for assertions. They also use the rest of its `evosuite.config`.
4. **Same contract checks, through a contract monitor.** For each class, `MonitorGen` builds a monitored adapter.
   - It has the same fully-qualified name and the same public static methods and state fields as `Helper`. It forwards every call to `HelperImpl`, the real adapter renamed, and copies the state fields back after every call.
   - The PRE and POST checks are not written by hand. TestGen's own parser (`SpecToAstConverter`) and IR builder (`IrBuilder`, which uses `ClauseText`) turn the `.spec` into Java, exactly as for TestGen's own tests:
     - the clauses are split into conjuncts and checked in order;
     - `\old(v)` is saved before the call;
     - `\result` is the returned value;
     - clauses that only name the result are skipped.
   - `contracts.tsv`, next to each generated monitor, lists every check as it appears in the `.spec` and as Java.
   - **One deliberate difference:** when a clause compares two objects with `==` or `!=` (for example two Strings), the monitor uses `Objects.equals` instead. javac's types decide which comparisons are between objects. Other tools pass equal strings that are not the same object, and that must not count against them. TestGen's own tests keep `==`, so on a mutant a TestGen test can fail natively on a reference comparison that the fair run does not count.
   - The monitor writes a log for each test. A JUnit `RunListener` turns the log into the test's result:
     - **PRE false** on a call: the call is **invalid**. The test is "invalid from call k", and nothing from that call on counts.
     - **POST false** on a valid call: **contract failure**.
     - **The library throws** on a valid call: **contract failure** (the same rule as TestGen).
     - Evaluating a PRE or POST clause throws: also a contract failure, because the library threw while answering about a state reached through valid calls.
     - The log decides the outcome, so a `try/catch` in a generated test cannot hide a failure.
     - The log also counts the adapter calls in every test (the depth), and records what each call returned or threw and the plain state fields after it. That record is used only for the reasons in table 6.
   - How it runs: `MutantTestMain` is unchanged and runs one class, `fairmon.FairEntry`. That class's runner runs the suite's test classes with the listener and writes one line per test. The log sits in a JDK object in the system properties, so EvoSuite's separate class loader writes to the same log.
5. **Same oracle.** `StripOracles` removes Randoop's and EvoSuite's own assertions: every `assert*`, `fail(...)` and `verifyException(...)` statement, plus Java `assert`.
   - An adapter call inside an assertion is kept as a statement, so the call sequence does not change.
   - `try/catch` blocks stay, so the code compiles. The monitor log decides the outcome anyway.
   - TestGen's suites run unchanged, also under the monitor. Their own PRE/POST assertions are the same checks.
6. **Same mutants.** The scored set is exactly the one in `mutation/<key>.status.tsv`: the seed-1 sample of up to 300 per class, with the same INVALID/TCE exclusions.
   - The mutants are planted and compiled by `D4jMutationRun` itself, run with no suites so it only plants and compiles. `fair.sh` stops if its sample differs from the native one.
   - Only the Commons class is mutated, never the adapter or the monitor.
   - Each (mutant, suite) pair runs in its own JVM through `MutantTestMain`, with the same settings as `mutation.sh`: 30 s wall clock, `-Xmx512m`, headless, `TZ=America/Los_Angeles`.
7. **Suites per class.**
   - `testgen-long` (the same file `mutation.sh` generated) and `testgen-all` (`outputs/<bug>/…`);
   - `testgen-family`, only if `MUT_WORK/<key>/family-src` exists (it is not generated here);
   - `randoop-s1..s10`, `evosuite-default-s1..s10` and `evosuite-blackbox-s1..s10`: all adapter-targeted and stripped.
8. **Baseline check** on the fixed version, run twice. These tests are dropped:
   - tests **invalid from their first call** (they test nothing in-contract);
   - **false alarms**: a contract failure or a time-out on the fixed version (counted);
   - **flaky** tests, whose outcome, depth, invalid point or observations differ between the two runs (counted);
   - tests that make **no adapter call** (they cannot fail; counted separately).

   A suite left with no tests kills nothing.

   **TestGen check:** before any mutant runs, TestGen's suites must have no invalid call and no false alarm under the monitor. Otherwise the run stops and lists each test with the monitor's verdict and TestGen's own assertion. `--allow-native-false-alarms` continues only when every flagged test is also failed by TestGen's own assertions, i.e. a false alarm TestGen already has on that version, as on the 5f/15f/16f versions, which still contain bug 19.
9. **Fair kill.** A mutant is killed by a suite only if some kept test reaches a contract failure on a valid call (before any invalid call), or times out. That means JUnit's per-test limit, or the JVM's 30 s.
   - A JVM that ends early without a contract failure (a crash) is *not* a kill here. It is recorded as `SURVIVED`/`CRASH` in the rows.
   - Natively, a crash counted as a kill for every tool.

### The 12 real bugs

For each bug in `bugs.csv`:

1. The adapter-targeted Randoop and EvoSuite suites are generated on its fixed version, as above.
2. The tests that pass the baseline check are kept.
3. They run on the buggy version under the monitor, which is compiled against the buggy classes.

**Detected** = a contract failure on a valid call. TestGen's long-string suite and its `+ short strings` suite (`outputs/<bug>/`) go through the same steps.

## Files

| File | What it is |
| --- | --- |
| `fair.sh` | runs everything |
| `src/fairmon/` | the runtime: `FairLog` (the event log), `FairRunner` (listener, per-test outcome), `FairEntry` |
| `src/in/ac/iiitb/plproject/fair/MonitorGen.java` | builds the monitored adapter from the `.spec` with TestGen's parser and IR |
| `src/in/ac/iiitb/plproject/fair/StripOracles.java` | removes Randoop's and EvoSuite's assertions, marks direct library calls |
| `src/in/ac/iiitb/plproject/fair/FairDriver.java` | compiles the suites, runs the baseline check twice, runs every (mutant, suite) pair or the buggy version |
| `report.py` | builds `REPORT.md` and `lost-mutants.tsv` |
| `<key>.fair.rows.tsv` | one row per (mutant, suite): outcome, kind (CONTRACT / TIMEOUT), the killing test and call, and for a survivor how the mutant showed through the adapter |
| `validity.tsv` | per suite (per class version and per bug version): tests, invalid tests, false alarms, flaky, adapter calls per test, assertions removed, direct library calls marked |
| `bugs.fair.tsv` | one row per (bug, suite) |
| `REPORT.md` | the tables |

## Reasons in table 6

For a mutant some native suite of a tool kills but no fair suite of that tool kills, the fair run of that mutant is compared with the fixed version, call by call. Each call is compared on what it returned or threw, and on the plain state fields after it.

| Reason | What the comparison shows |
| --- | --- |
| **regression-only check** | A valid call behaves differently, but no clause of the contract checks that. The native kill came from a recorded-value assertion. |
| **invalid call** | Differences appear only in calls that break their precondition. The mutant makes a call invalid earlier, or changes calls that were already invalid. |
| **no adapter call** | No adapter call shows any difference. The native kill came from using the class in ways the adapter does not. |

## Limits

- **EvoSuite's suites are regenerated against the adapter.** They are not the native ones, so a native vs fair change mixes two effects: the target (adapter vs class) and the oracle (contract vs regression assertions). Table 6 helps to separate them.
- **"Contract-observable" (table 3) depends on the generated tests.** A mutant no suite of any tool kills may still be observable by some other in-contract sequence.
- **Marking direct library calls is static.** A call is marked by its declaring class. Calls on `String`, `Object`, boxed numbers, `Math`, JUnit, EvoSuite's runtime and `PrintStream` are allowed.
