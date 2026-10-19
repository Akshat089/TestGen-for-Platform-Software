# GENATC: no-propagation IR, real SPF, decoupled mutation module

This replaces the old pipeline (`NewGenATC` + `PropagationScan` →
`GeneratedATCs.java` / `GeneratedATCs_JUnit.java` / `SingularCase.java`, SPF
never actually run) with the design described in the task: contract + sequence
→ checks → one IR → a symbolic driver → **real** Symbolic PathFinder → solved
input vectors → a JUnit class, verified against the unmodified library. All
numbers below come from runs actually executed on this machine on 2026-10-19;
none are invented.

## What changed, file by file

**Deleted** (propagation era): `atc/PropagationScan.java`,
`atc/SingularCaseGenerator.java`, `atc/NewGenATC.java`, `atc/ConcreteInput.java`,
`atc/{GenATC,TestGen,LibTestGenATC,IncrementalTestExample,JavaFile}.java`, all of
old `atc/ir/*` (`AtcClass`, `AtcTestMethod`, `AtcAssumeStmt`,
`AtcReturnCaptureStmt`, `AtcPropagatedInputStmt`, …), `symex/SpfWrapper.java`,
`symex/AtcIrToSymbolicIrTransformer.java`, `symex/TypeMapper.java`,
`parser/TestStringValidator.java`, `atc/LibraryDryRunExamples.java`, `Main.java`,
`verify-stubs/` (real SPF now, no stub needed), old
`golden/*/{GeneratedATCs_JUnit,SingularCase}.java`, old
`src/test/.../{LibraryDryRunInvariantsTest,MutationScoreTest,GeneratedTestCasesTest,GeneratedSuitesTest}.java`.
`Variable`'s `VariableOrigin` (CLIENT_INPUT/SERVER_OUTPUT) enum is gone.

**Added**, new packages under `pl-platform-testing/src/main/java/in/ac/iiitb/plproject/`:

| Package | Classes | Job |
|---|---|---|
| `ir` | `TestPlan`, `CallBlock`, `ParamSlot`, `ParamKind`, `Clause`, `Snapshot`, `ClauseText`, `IrBuilder` | Step 2: the one shared IR. `ClauseText` does the rewrite directly on each clause's ORIGINAL substring (qualify state vars as `Helper.<name>`, then substitute `\old(v)`) rather than reprinting from an expression tree — the reason "kept with its original text" and "identical in both outputs" are literally true, not just asserted. |
| `check` | `InputChecker`, `GenatcInputException` | Rule 13: every check, run before anything is built, every violation reported as `file:line: message`. |
| `symbolic` | `TypeSupport`, `SymbolicDriverPrinter`, `JUnitPrinter` | Steps 3 and 6: the two printers, reading the same `CallBlock`s. `TypeSupport` is the solvability classifier (rule 9). |
| `spf` | `SpfRunner`, `SpfConfig`, `SpfHome`, `PathVector`, `PathVectorReader`, `InputVector`, `ValueExtractor`, `SpfRunResult` | Steps 4 and 5: launches SPF as a separate process, reads its listener's JSON-lines output, converts to Java literals. |
| `spf.listener` | `PathVectorListener` | The JPF listener: solves the path condition at each completed path via jpf-symbc's own `PathCondition` API and writes one JSON line — no console-text scraping (rule 7). |
| `atc` (new) | `GenatcMain`, `LibraryRegistry` | The entry point and the registry, replacing `Main`/`LibraryDryRunExamples`. |
| `verify` (rewritten) | `GeneratedSuiteHarness`, `JunitJars` | Compiles + runs the generated JUnit class against the real library with ONLY real JUnit 4 on the classpath. |

**Mutation module split**: `in.ac.iiitb.plproject.mutation`, `run-mutations.sh`,
`run-operator-suites.sh`, `mutations/`, `operator-suites/` all moved
(`git mv`, content preserved) to a new `genatc-mutation/` Maven module. A root
aggregator `pom.xml` ties `pl-platform-testing` and `genatc-mutation` together.
`MutationRunner` and `GeneratedSuiteRunner` are rewritten against
`GenatcMain.runOne`/`LibraryRegistry` — this turned out to be straightforward,
not just a TODO (see "Mutation testing" below), so it is **done**, not
stubbed.

**`pom.xml`**: JUnit 4.13.1 everywhere (generated classes, the project's own
tests, the harness) — one version, replacing the old JUnit 5 + hand-rolled
JUnit 4 stub split. `gov.nasa.jpf:jpf-core`/`jpf-symbc` added as `provided`
dependencies (installed locally — see below). JDK target raised to 11
throughout (matches what jpf-core/jpf-symbc actually build and run under).

## Setup, build, generate, test — exact commands

```bash
# 1. One-time: build and install jpf-core/jpf-symbc (needs network + git + ant; ~1 min)
./scripts/setup-spf.sh
# installs into ~/.genatc-spf by default; override with:
#   ./scripts/setup-spf.sh --spf-home /path/to/dir
# or at runtime:  export GENATC_SPF_HOME=/path/to/dir

# 2. Build both modules
mvn -o install                 # from the repo root

# 3. Generate + verify every registered library against real SPF
cd pl-platform-testing
java -cp target/classes:$(find ~/.m2/repository/gov/nasa/jpf ~/.m2/repository/junit ~/.m2/repository/org/hamcrest -name '*.jar' | tr '\n' ':') \
     in.ac.iiitb.plproject.atc.GenatcMain            # every library, or:
     ... GenatcMain stack queue mathlib              # a subset

# 4. This module's own test suite (12 tests, no SPF dependency except one
#    that is inherently dynamic and skips itself if setup-spf.sh wasn't run)
mvn -o test

# 5. The repo-root verification script: builds, regenerates every library
#    through real SPF, checks each one's status against what is documented
#    below, fails loudly on a mismatch
./verify-generated.sh

# 6. Mutation testing (separate module, depends on pl-platform-testing)
./genatc-mutation/run-mutations.sh [stack|hashmap|...]       # hand-written sets
./genatc-mutation/run-operator-suites.sh [stack|...]          # generated suites, SLOW
```

## Versions, solver, bounds

| | |
|---|---|
| JDK | 11 (`java-11-openjdk-amd64`), used to build jpf-core, jpf-symbc, and this project |
| jpf-core | `javapathfinder/jpf-core`, commit `f70927ce05b165addb02e9c232af74e9737e09bf` (master, 2025-12-16), built with `./gradlew buildJars` |
| jpf-symbc | `javapathfinder/jpf-symbc`, commit `3bedf0b168b3c85dd0f05434dd61c4c011cc447a` (master, 2022-06-08), built with `ant build` |
| Maven coordinates installed | `gov.nasa.jpf:jpf-core:8.0-f70927c` (= `build/jpf.jar`, the host engine), `gov.nasa.jpf:jpf-symbc:1.0-3bedf0b` (= `build/jpf-symbc.jar`) |
| Solver | `z3` (default; `-Dgenatc.spf.solver=choco` also works — both hit the same modulo limitation, see below) |
| Int range | `symbolic.min_int=-1000`, `max_int=1000` (`-Dgenatc.spf.minInt`/`maxInt`) |
| Depth bound | `search.depth_limit=300` (`-Dgenatc.spf.depthLimit`) |
| Timeout | `search.time_limit=120`, external process kill at `timeoutSeconds+30` (`-Dgenatc.spf.timeoutSeconds`) |
| Pool size | 3 distinct literals per pooled type (`"s1","s2","s3"` / `101,103,105`) |
| Tests per class cap | 60 (`-Dgenatc.maxTestsPerClass`) — see arraylib/graph/lrucache below |

Two jar families matter for jpf-core/jpf-symbc, with disjoint class sets —
this cost real debugging time (see "Things I found and fixed" below):
`build/jpf.jar`/`build/jpf-symbc.jar` are the HOST-side engine (`Config`,
`JPF`, `PropertyListenerAdapter`, `PathCondition`, …), used to compile the
generator's own JPF listener and declared as the Maven `provided`
dependency; `build/jpf-classes.jar`/`build/jpf-symbc-classes.jar` are the
MODELED/target-side classes (`Verify`, `Debug`), used to compile the
generated symbolic driver, resolved at runtime from the checkout directory
(`SpfHome`), never through Maven.

## Per-library table (from the run saved at `pl-platform-testing/golden/`; sequence is each library's checked-in `.tests` file)

| Library | Supported? | Sequence | Paths explored | Tests generated | Params left unsolved (why) | SPF time | JUnit vs. unmodified library |
|---|---|---|---|---|---|---|---|
| stack | yes | push→push→pop→peek | 9 | 9 | `elem`×2 (String — weak solver support) | 1718ms | 9/9 pass |
| queue | yes | enqueue→enqueue→dequeue→front→isEmpty | 9 | 9 | `elem`×2 (String) | 1251ms | 9/9 pass |
| mathlib | **partial** | abs→gcd→power→factorial | 0 | 0 | — | 5244ms | **EXECUTION_ERROR on `gcd`** — see below |
| arraylib | yes | set→set→get→sum→indexOf | 240 | 60 (capped) | none (all `int`) | 1728ms | **26/60 pass, 34 fail** — real contract bug, see below |
| linkedlist | yes | addFirst→addLast→removeFirst→indexOf | 2 | 2 | none (all `Integer`) | 1127ms | 2/2 pass |
| heap | yes | insert→insert→insert→peekMin→extractMin | 6 | 6 | none (all `Integer`) | 1160ms | 6/6 pass |
| set | yes | add→add→contains→remove | 45 | 45 | `elem`×4 (String) | 1411ms | 45/45 pass |
| hashmap | yes | put→put→getOldValue→remove | 27 | 27 | `key`×4 (String) | 1281ms | 27/27 pass |
| bst | yes | insert→insert→insert→contains→min→delete | 54 | 54 | `key`×5 (`int` — used as a `Set<Integer>` key, breaks both solvers' hash handling) | 1478ms | 54/54 pass |
| graph | yes | addVertex→addVertex→addEdge→degree→hasEdge | 195 | 60 (capped) | `name`/`from`/`to`×7 (String) | 2421ms | 60/60 pass |
| lrucache | yes | put→put→put→get→restore | 4374 | 60 (capped) | `key`/`value`/`evictedKey`×9 (String) | 27283ms | 60/60 pass |
| taskqueue | **no** | submit→getResult→cancelTask | — | — | — | — | — |
| ticketservice | **no** | numSeatsAvailable→findAndHoldSeats→reserveSeats | — | — | — | — | — |
| orderservice | **no** | restock→placeOrder→ship→stockLevel | — | — | — | — | — |

**Totals**: 10 of 14 libraries fully generated and verified; 1 partially
(mathlib); 3 marked unsupported by design.

### Test failures with the clause that failed

**arraylib** — every failure is the same clause: `POST sum: total >= 0`
(`sum`'s postcondition). `set`'s precondition (`index >= 0 && index < length`)
never constrains `value`'s sign, so SPF correctly finds — and the generated
JUnit correctly catches — sequences where a negative `value` makes the running
total go negative, directly contradicting `total >= 0`. **This is a real
finding against `IntArray.spec`, not a generator bug**; left as-is per "do not
fix the contracts" (the task names two specific known bugs to leave, Queue and
MathLib — this is a third, found by actually running SPF rather than listed in
advance, and is reported here rather than fixed or hidden).

**mathlib** — `gcd` cannot be executed by SPF AT ALL with this jpf-symbc
build. Traced to `gov.nasa.jpf.symbc.bytecode.IREM`'s div-by-zero check:

```java
int v1 = sf.peek(0);              // the divisor's CONCRETE shadow value
if (v1 == 0)
    return th.createAndThrowException("java.lang.ArithmeticException", "div by 0");
```

A freshly created symbolic int's underlying concrete shadow slot defaults to
Java's `0`, and this check fires on that value BEFORE the symbolic,
path-condition-aware branch below it ever runs — so ANY symbolic int used as
a `%`/`/` divisor throws immediately, regardless of what was `Debug.assume`d
about it. Reproduced from a 6-line program with nothing from this project:

```java
int a = Debug.makeSymbolicInteger("a");
int b = Debug.makeSymbolicInteger("b");
Debug.assume(b > 0);
int remainder = a % b;   // ArithmeticException: div by 0 — with BOTH z3 and choco
```

`abs`, `power`, and `factorial` (no internal `%`/`/`) are unaffected; only
`gcd` is blocked. `GenatcMain` reports this as `EXECUTION_ERROR`, distinct from
`INFEASIBLE` (rule 10): this is not "the precondition is unsatisfiable", it is
"SPF cannot execute this call at all". **No class is generated for the
mathlib sequence** since it never reaches the end (rule 10).

### Not solved for (rule 9), by library

- **String**, everywhere it appears as a parameter (stack, queue, set,
  hashmap, graph, lrucache): SPF's string support is weak; pooled behind a
  3-element shared literal pool (`"s1","s2","s3"`) selected by a symbolic
  index, so the CHOICE is still genuinely solved by SPF even though the
  literal itself is fixed ahead of time.
- **bst's `int key`**: not a String, but used as the key of `Set<Integer>
  Keys`. A fully symbolic int reaching `Set.contains`/`.add`/`.remove`
  triggers the same `hashCode()`-bit-shift problem Strings have (confirmed by
  experiment: `## Error Z3` / `Choco does not support bitwise SHIFT`), so it
  is pooled the same way, over `101,103,105`.

### Infeasible sequence found

None of the 11 generated libraries' own checked-in sequences are infeasible.
`push -> pop -> pop` (constructed directly, not checked in) IS correctly
reported infeasible at `pop` (`requires: size > 0`) with no class generated —
exercised by `GeneratorPipelineTest.infeasibleSequenceProducesNoClassAndAReport`.

### The two documented contract bugs — not fixed, checked

- **Queue.spec's `isEmpty`** (`ensures: emptyFlag == false`): not exposed by
  the checked-in sequence `enqueue→enqueue→dequeue→front→isEmpty`, because
  one element is always left on the queue by the time `isEmpty` runs, so the
  (wrong) postcondition and the real behaviour happen to agree. Exposing it
  needs a sequence that reaches `isEmpty` on a genuinely empty queue; the
  checked-in sequence never does, and I did not add one, since the task
  gives sequences-as-written for the priority libraries and doesn't ask for
  additional sequences per library.
- **MathLib.spec's `power`** (`exponent <= 30`, e.g. `power(7,18)` overflows
  `int`): moot for this run — `power` is never reached, because the sequence
  is `abs→gcd→power→factorial` and `gcd` blocks everything after it (see
  above).

## Decisions made without asking first (please review)

1. **taskqueue/ticketservice/orderservice marked unsupported**, rather than
   adapted. Each needs an opaque, library-chosen id (`taskId`/`seatHoldId`/
   `orderId`) returned by one call threaded into a later call's argument;
   rule 1 forbids threading a return value, and rule 12 (never read the
   library) blocks any pool-based guess at what the id will be. I asked
   about this and about bst earlier; you said "whichever you think best" —
   this is option (a) from that message (mark unsupported, one-line reason
   each), the one I'd recommended, since the alternative (pooling a small
   range of ids) would bake in an assumption about the library's own
   id-allocation scheme that the generator has no way to verify from the
   spec alone.
2. **hashmap and lrucache needed no adaptation at all**, contrary to my own
   earlier guess that hashmap might need it: `getOldValue`/`remove`'s `key`
   parameter is CLIENT-chosen (pooled String), not server-generated, so the
   shared pool already lets a later call's key match an earlier `put`'s.
   lrucache's `restore(evictedKey, value)` degrades gracefully too — its
   precondition (`!C.containsKey(evictedKey)`) only needs an unused pooled
   key, not literally the one `put` evicted, so the sequence completes
   under the no-propagation design; the ORIGINAL scenario's intent ("restore
   the evicted key") is no longer exercised, but the contract AS WRITTEN is.
3. **A 60-test-per-class cap** (`-Dgenatc.maxTestsPerClass`). StringSet/
   Graph/LruCache's pool combinations multiply out to 45/195/4374 SPF-feasible
   paths; shipping all of them as `@Test` methods is impractical (LruCache's
   full run alone explores 4374 paths in ~27s and would produce a
   multi-thousand-method file). The cap truncates which paths become TESTS,
   not what SPF explores — the report always states both numbers.
4. **Pool size fixed at 3** per pooled type, shared across the whole
   sequence (not per-parameter) — e.g. StringSet's `add`/`remove` draw from
   the SAME `"s1","s2","s3"` pool, which is what lets `remove`'s
   `E.contains(elem)` precondition be satisfiable by a later call reusing an
   earlier call's literal.
5. **`verify-generated.sh` checks against previously-observed statuses**,
   not against "every library must fully pass" — since arraylib's JUnit
   failures and mathlib's EXECUTION_ERROR are both expected, documented
   outcomes, not script bugs.

## What I could not do / did not finish

- **mathlib is not "fully working end to end"** as asked, for the reason
  above — a confirmed jpf-symbc bug, not a limitation of this generator's
  design. `abs`/`power`/`factorial` individually have no internal `%`/`/`
  and would work; I did not change the checked-in sequence to route around
  `gcd` without asking first.
- **`run-operator-suites.sh`'s full run was not executed to completion** —
  by its own documentation it is well over an hour for the full set
  (260 generated test strings × real SPF, per library); I smoke-tested it
  (confirmed it starts, and correctly runs the baseline + mutant phases
  against real SPF) rather than running it to completion, given the time
  this implementation already took.
- **The mutation module's adaptation to the new JUnit-based pipeline was a
  stretch goal in the brief ("do it if straightforward, otherwise leave a
  clear TODO")** — it turned out straightforward and is DONE, not a TODO:
  `MutationRunner.run(mutant)` now calls `GenatcMain.runOne` on a sandboxed
  library copy and classifies KILLED/SURVIVED/INVALID from its JUnit
  result; smoke-tested against stack's 11 hand-written mutants (8 killed,
  3 survived, matching each mutant's own description of what the checked-in
  sequence can and can't catch).
- **Line-number precision in input-check errors** is per spec BLOCK (the
  `spec <name> { ... }` line), not per individual clause — matches the
  granularity the rest of this codebase's regex-based parsing already uses,
  but a clause three lines into a block is reported at the block's line, not
  its own.
