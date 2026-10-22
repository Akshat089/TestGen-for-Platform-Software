# GENATC mutation experiments: contract-only SPF

Every number below comes from a run made on 2026-10-22 on the machine in part 1.
GENATC is black-box here: the symbolic driver is built from the `.spec`
contract only, never calls the library, and SPF runs with the library off its
classpath. So a test depends only on (spec, sequence). Each test was generated
once on the correct library, compiled once, and that same compiled test was run
against every mutant.

Raw logs and tables:
- Hand-written run: console output summarised below (log kept at
  `genatc-mutation/target/handwritten.log`, not committed).
- Generated suites: `genatc-mutation/operator-suites/SUMMARY.txt` and
  `genatc-mutation/operator-suites/results/*.tsv`.
- Previous (white-box / random-sampling era) numbers:
  `genatc-mutation/operator-suites/results-old/`.

## 1. Setup

| Item | Value |
|---|---|
| Java | OpenJDK 11.0.32.1 (2026-08-18) |
| jpf-core | commit `f70927c` (`~/.genatc-spf/jpf-core`) |
| jpf-symbc | commit `3bedf0b` (`~/.genatc-spf/jpf-symbc`) |
| Solver | Z3 (`symbolic.dp = z3`) |
| Symbolic int range | `symbolic.min_int = -1000`, `symbolic.max_int = 1000` |
| SPF time limit | `search.time_limit = 60` s per sequence (process killed at 90 s); depth limit 300 |
| SPF heap | `-Xmx384m` per SPF process |
| Search | stops at the first completed, solved path (one input vector per sequence) |
| Pool size | 3 (`"s1","s2","s3"`; numeric keys 101, 103, 105) |
| Test time limit | `@Test(timeout = 2000)` per test method; child JVM watchdog 20 s + 3 s per test |
| Family size per library | 260 for every supported library (see part 9) |
| Machine | AMD Ryzen 7 5800H, 16 logical cores, 6.7 GiB RAM (WSL2), Ubuntu 24.04.1 LTS, kernel 6.18.54.1-microsoft-standard-WSL2 |
| Parallelism (operator suites) | 9 library workers (memory-capped: 16 cores, ~3 GB free, ~300 MB per worker) |
| Wall clock: correct-library check (experiment 1) | 55.6 s for all 14 registered libraries (`GenatcMain`, all 11 supported pass). This run overlapped experiment 3 (load average above 20), so its per-library SPF times (3.8–6.6 s) are inflated; part 2 uses the unloaded times from experiment 2's baseline step. |
| Wall clock: hand-written mutants (experiment 2) | 54.6 s total (`real`), of which 48 s in the runner after the build |
| Wall clock: generated operator suites (experiment 3) | WALL_OPS |

## 2. Generation table (checked-in sequences, correct library)

SPF time is the time of the one SPF run per sequence (compile the driver,
start SPF, solve), measured in the hand-written experiment's baseline step.

| Library | Sequence | Test generated | Passes on correct library | Failing clause | SPF time (ms) |
|---|---|---|---|---|---|
| stack | push -> push -> pop -> peek | yes | yes | — | 1108 |
| queue | enqueue -> enqueue -> dequeue -> front -> isEmpty | yes | yes | — | 1059 |
| mathlib | abs -> gcd -> power -> factorial | yes | yes | — | 906 |
| arraylib | set -> set -> get -> sum -> indexOf | yes | yes | — | 983 |
| linkedlist | addFirst -> addLast -> removeFirst -> indexOf | yes | yes | — | 937 |
| heap | insert -> insert -> insert -> peekMin -> extractMin | yes | yes | — | 999 |
| set | add -> add -> contains -> remove | yes | yes | — | 1033 |
| hashmap | put -> put -> getOldValue -> remove | yes | yes | — | 1137 |
| bst | insert -> insert -> insert -> contains -> min -> delete | yes | yes | — | 1104 |
| graph | addVertex -> addVertex -> addEdge -> degree -> hasEdge | yes | yes | — | 1549 |
| lrucache | put -> put -> put -> get -> restore | yes | yes | — | 1706 |
| taskqueue | submit -> getResult -> cancelTask | no: unsupported (needs the taskId submit() returns passed forward) | — | — | — |
| ticketservice | findAndHoldSeats -> reserveSeats | no: unsupported (needs the returned seatHoldId) | — | — | — |
| orderservice | placeOrder -> ship | no: unsupported (needs the returned orderId) | — | — | — |

Inputs SPF chose (this is what every mutant was tested with):

| Library | Inputs |
|---|---|
| stack | push "s1", push "s1" |
| queue | enqueue "s3", enqueue "s3" |
| mathlib | abs(7)\*, gcd(1, 1), power(1, 0), factorial(0) |
| arraylib | set(3, 0), set(3, 0), get(3), indexOf(7)\* |
| linkedlist | addFirst(0), addLast(0), indexOf(7)\* |
| heap | insert(7)\*, insert(9)\*, insert(11)\* |
| set | add/add/contains/remove all "s3" |
| hashmap | put("s3", 0), put("s3", 0), getOldValue("s3"), remove("s3") |
| bst | insert 101, 103, 105; contains(105); delete(101) |
| graph | every name "s3" (addEdge("s3","s3"), hasEdge("s3","s3")) |
| lrucache | put("s3","s1") three times, get("s3"), restore("s1","s1") |

\* left free by the solver (no modelled clause constrains it): GENATC fills in a
small, distinct, non-zero literal (7, 9, 11, ...).

Check-only clauses (left out of the driver, asserted in JUnit only):

| Library | Clause | Why |
|---|---|---|
| mathlib | gcd ensures `a % divisor == 0`, `b % divisor == 0` | `%` by a symbolic divisor: jpf-symbc's IREM/IDIV test the divisor's concrete value, not the path condition |
| linkedlist | addFirst/addLast ensures `L.contains(value)` | `L` is a `List<Integer>` whose elements are not pooled, so it has no model |
| heap | insert ensures `H.contains(value)` (×3) | same: `List<Integer>` not pooled |
| graph | degree ensures `vertexDegree == Adj.get(name).size()` | `Adj` maps to a `List`, whose `size()` counts duplicates; the per-pair flags cannot |

## 3. Q1: hand-written mutants by library

| Library | Mutants | Killed | Survived | Invalid | Score |
|---|---|---|---|---|---|
| stack | 11 | 8 | 3 | 0 | 72.7% |
| queue | 8 | 4 | 4 | 0 | 50.0% |
| mathlib | 12 | 5 | 7 | 0 | 41.7% |
| arraylib | 9 | 3 | 6 | 0 | 33.3% |
| linkedlist | 8 | 3 | 5 | 0 | 37.5% |
| heap | 9 | 5 | 4 | 0 | 55.6% |
| set | 6 | 4 | 2 | 0 | 66.7% |
| hashmap | 11 | 9 | 2 | 0 | 81.8% |
| bst | 9 | 5 | 4 | 0 | 55.6% |
| graph | 8 | 5 | 3 | 0 | 62.5% |
| lrucache | 8 | 2 | 6 | 0 | 25.0% |
| taskqueue | 11 | 0 | 0 | 11 | — (no test: unsupported) |
| ticketservice | 13 | 0 | 0 | 13 | — (no test: unsupported) |
| orderservice | 14 | 0 | 0 | 14 | — (no test: unsupported) |
| **All** | **137** | **53** | **46** | **38** | **53.5%** |

## 4. Q2: hand-written mutants by operator

Invalid mutants are all in the three unsupported libraries.

| Operator | Abbr | Mutants | Killed | Survived | Score |
|---|---|---|---|---|---|
| arithmetic-operator-replacement | AOR | 6 | 0 | 3 | 0.0% |
| arithmetic-operator-insertion | AOI | 7 | 4 | 0 | 100.0% |
| relational-operator-replacement | ROR | 5 | 1 | 4 | 20.0% |
| conditional-operator-replacement | COR | 2 | 2 | 0 | 100.0% |
| conditional-operator-insertion | COI | 4 | 2 | 2 | 50.0% |
| unary-operator-insertion | UOI | 7 | 5 | 1 | 83.3% |
| absolute-value-insertion | ABS | 2 | 1 | 0 | 100.0% |
| constant-replacement | CRP | 7 | 2 | 3 | 40.0% |
| condition-to-constant | CTC | 2 | 1 | 1 | 50.0% |
| loop-bound-replacement | LBR | 4 | 0 | 3 | 0.0% |
| return-value-replacement | RVR | 14 | 6 | 6 | 50.0% |
| statement-deletion | SDL | 41 | 18 | 7 | 72.0% |
| statement-replacement | STR | 6 | 1 | 4 | 20.0% |
| argument-replacement | ARP | 13 | 1 | 10 | 9.1% |
| method-call-replacement | MCR | 5 | 1 | 2 | 33.3% |
| side-effect-insertion | SEI | 10 | 6 | 0 | 100.0% |
| variable-replacement | VRO | 2 | 2 | 0 | 100.0% |

(Mutants column includes the 38 invalid ones; score = killed / (killed + survived).)

## 5. What killed them (hand-written, 53 kills)

| Kill kind | Count |
|---|---|
| POST assertion failed | 49 |
| PRE assertion failed | 0 |
| Library threw an exception | 4 |
| Timeout | 0 |

The four exception kills: ARRAY_SUM_RUNS_PAST_END (ArrayIndexOutOfBoundsException),
BST_INSERT_NEVER_GROWS (NullPointerException), HEAP_SIFT_DOWN_UNGUARDED
(IndexOutOfBoundsException), LRU_EVICTS_ON_EVERY_MISS (IndexOutOfBoundsException).

## 6. Q3: why the 46 hand-written survivors survive

Each survivor is in exactly one group. The grouping comes from reading each
mutant's change next to the inputs in part 2. It was not checked by running
extra tests; the generated-suite family (part 9) measures group (3)/(5)-type
effects for the generated mutants.

| Group | Count |
|---|---|
| (1) the language cannot say it | 8 |
| (2) the contract could say it but does not | 5 |
| (3) the sequence never asks | 4 |
| (4) the precondition rules it out | 1 |
| (5) the solver's chosen inputs did not hit it | 28 |
| **Total** | **46** |

**(1) The language cannot say it (8)**
- MATH_GCD_IGNORES_FIRST_ARG — the spec can say "a positive common divisor", not "the greatest"; 1 qualifies.
- MATH_POWER_ONE_TOO_FEW — no exponentiation in the grammar; `powerValue > 0` is all that can be said.
- MATH_POWER_ADDS_INSTEAD — same: any positive answer satisfies `powerValue > 0`.
- MATH_FACTORIAL_STOPS_EARLY — same shape: `factValue >= 1` bounds the answer but cannot compute it.
- ARRAY_SUM_SKIPS_FIRST — no summation over A's cells; only `total >= 0` can be said.
- ARRAY_INDEXOF_ALWAYS_FIRST — "first position holding value" needs a quantifier; only the range is said.
- LIST_INDEXOF_NEVER_MATCHES — same gap: -1 is inside the stated range `-1 <= position < size`.
- LIST_INDEXOF_NEVER_WALKS — same gap, reached by never entering the loop.

**(2) The contract could say it but does not (5)**
- STACK_POP_FROM_BOTTOM — pop's only result clause is `\result == poppedElem`, which names the result and asserts nothing; `poppedElem == \old(top)` would.
- QUEUE_ENQUEUE_AT_FRONT — nothing says which end enqueue writes; `head` is re-derived, so the queue stays self-consistent.
- HASHMAP_PUT_OLDVAL_FABRICATED — `\result == oldVal` names the result only; nothing ties oldVal to the previous entry.
- LRU_PUT_HIDES_EVICTION — `\result == evictedKey` names the result only; nothing says which key (if any) was evicted.
- LRU_RESTORE_LOSES_VALUE — restore never says `C.get(evictedKey) == value`.

**(3) The sequence never asks (4)**
- STACK_PUSH_AT_BOTTOM — the pop re-derives top before peek reads it; push -> push -> peek -> pop would separate the orders.
- STACK_PEEK_RETURNS_BOTTOM — only one element is left when peek runs, so bottom = top.
- HEAP_EXTRACT_NO_REFILL — the sequence ends at extractMin; nothing reads the heap afterwards.
- GRAPH_ADDVERTEX_REDECLARES — no vertex is re-added after it has an edge.

**(4) The precondition rules it out (1)**
- BST_INSERT_TIES_GO_LEFT — insert requires `!Keys.contains(key)`, so equal keys never meet.

**(5) The solver's chosen inputs did not hit it (28)**
- QUEUE_DEQUEUE_FROM_BACK — both enqueued elements are "s3", so front and back are equal.
- QUEUE_HEAD_NOT_REFRESHED — the stale head is "s3" and so is the true one.
- QUEUE_ENQUEUE_HEAD_FROM_BACK — newest = oldest = "s3".
- MATH_ABS_NO_FLIP — x = 7 is already non-negative.
- MATH_GCD_NO_LOOP — returns its first argument, and a = b = 1.
- MATH_LASTRESULT_STALE — the stale value (gcd's 1) equals power(1, 0) = 1.
- ARRAY_SET_WRONG_CELL — writes 0 into cell 0 instead of cell 3; every cell already holds 0.
- ARRAY_GET_WRONG_CELL — reads cell 0 instead of cell 3; both are 0.
- ARRAY_SUM_SUBTRACTS — the sum of all-zero cells is 0 either way, and `total >= 0` holds.
- ARRAY_GET_NEGATES — -0 = 0.
- LIST_ADDLAST_AT_FRONT — both values are 0, so front and back agree.
- LIST_REMOVEFIRST_FROM_TAIL — head and tail are both 0.
- LIST_INDEXOF_COUNTS_FROM_ONE — the searched value (7, left free) is not in the list, so both versions answer -1.
- HEAP_NO_SIFT_UP — 7, 9, 11 are inserted in increasing order; nothing needs to sift.
- HEAP_WRONG_PARENT_INDEX — same: no sift-up is ever needed.
- HEAP_SIFT_UP_STOPS_ONE_SHORT — same.
- SET_CONTAINS_ALWAYS_TRUE — contains is asked about the element that was added ("s3").
- SET_REMOVE_CLEARS_EVERYTHING — the set only ever holds "s3", so remove = clear.
- HASHMAP_REMOVE_CLEARS_MAP — both puts use key "s3", so the map has one key.
- BST_INSERT_ALWAYS_RIGHT — keys 101, 103, 105 arrive in increasing order and all go right anyway.
- BST_MIN_WALKS_RIGHT — the root has no left child, so the loop never runs and min is the root, 101.
- BST_CONTAINS_DESCENDS_WRONG_WAY — contains(105) only ever descends right.
- GRAPH_ADDEDGE_ONE_DIRECTION — the edge is s3–s3, so one direction is both.
- GRAPH_HASEDGE_ALWAYS_TRUE — hasEdge is asked about the pair that was joined.
- LRU_NEVER_EVICTS — one key ("s3") is put three times; the cache never fills.
- LRU_EVICTS_MOST_RECENT — no eviction happens.
- LRU_GET_DOES_NOT_PROMOTE — "s3" is already the most recent key.
- LRU_EVICTS_ONE_TOO_LATE — no eviction happens.

## 7. Comparison with the declared `expect:` lines

The `expect:` lines were written for earlier designs and were not edited.

- 25 mutants declared `expect: killed` now **survive**: ARRAY_SET_WRONG_CELL,
  ARRAY_GET_WRONG_CELL, ARRAY_SUM_SUBTRACTS, ARRAY_GET_NEGATES,
  BST_INSERT_ALWAYS_RIGHT, BST_MIN_WALKS_RIGHT, BST_CONTAINS_DESCENDS_WRONG_WAY,
  GRAPH_ADDEDGE_ONE_DIRECTION, HEAP_NO_SIFT_UP, HEAP_WRONG_PARENT_INDEX,
  HEAP_SIFT_UP_STOPS_ONE_SHORT, LIST_ADDLAST_AT_FRONT, LIST_REMOVEFIRST_FROM_TAIL,
  LIST_INDEXOF_COUNTS_FROM_ONE, LRU_NEVER_EVICTS, LRU_EVICTS_MOST_RECENT,
  LRU_PUT_HIDES_EVICTION, LRU_GET_DOES_NOT_PROMOTE, LRU_EVICTS_ONE_TOO_LATE,
  MATH_ABS_NO_FLIP, MATH_GCD_NO_LOOP, MATH_LASTRESULT_STALE,
  QUEUE_DEQUEUE_FROM_BACK, QUEUE_HEAD_NOT_REFRESHED, QUEUE_ENQUEUE_HEAD_FROM_BACK.
- 0 mutants declared `expect: survives` are now killed.
- 38 mutants (taskqueue, ticketservice, orderservice) could not be run: their
  libraries are unsupported, so there is no test.

Why the 25 differ: the declared expectations assumed hand-picked, distinct
inputs (the `inputs { }` blocks, now ignored: "alpha"/"beta", 7/11, 9/4/7,
50/30/70, three distinct cache keys). The black-box generator asks SPF for
*one* input vector that satisfies the contract, and Z3 returns the simplest
one: repeated pool values, zeros, and keys in increasing order. 24 of the 25
are in group (5) above. The 25th, LRU_PUT_HIDES_EVICTION, is in group (2):
with one key, no eviction happens, and the only clause about put's result is
`\result == evictedKey`, which GENATC no longer asserts because it only names
the result.

## 8. Contract findings (tests that failed on the correct library)

- **Checked-in sequences:** none. All 11 generated tests pass on the correct
  library.
- **Queue `isEmpty` (`emptyFlag == false`)** and **MathLib `power`
  (`exponent <= 30`, which overflows)** were not exposed by the checked-in
  sequences. The solver chose `power(1, 0)`, and isEmpty runs with one element
  left.
- **Generated families:** FAMILY_FINDINGS

## 9. Generated operator suites

PART9

## 10. What I could not do, and decisions made without asking

**Could not do / limits**
- taskqueue, ticketservice and orderservice stay unsupported: they need a
  library-chosen id returned by one call and passed to a later one. Their 38
  hand-written mutants are INVALID ("no test").
- Check-only clauses (part 2) are never used by SPF. In particular, linkedlist
  and heap values are left free by the solver (filled with 7, 9, 11...),
  because `List<Integer>` is not a pooled collection under the rule "Strings and
  Set/Map keys are pooled".
- The survivor grouping in part 6 is my reading of each mutant against the
  chosen inputs. I did not run extra tests to confirm each group-(5) claim.
- `MUTATION_TESTING.md` had uncommitted edits describing the removed random
  sampler. I left that file untouched and uncommitted (it may be a paper-side
  document). All of that sampler work is saved in git at
  `refs/backup/random-sampler-wip` (commit `1b69d76`) and was not committed to
  the branch.

**Decisions made without asking**
1. *Assumptions are printed as `Verify.ignoreIf(!(cond))`, not
   `Debug.assume(cond)`.* In jpf-symbc 3bedf0b, `Debug.assume` only prunes when
   its argument still carries a symbolic attribute
   (`JPF_gov_nasa_jpf_symbc_Debug.assume`). A Java comparison has already been
   branched into a concrete boolean by then, so `Debug.assume` would prune
   nothing. The meaning (keep only paths where the clause holds) is the one
   asked for.
2. *SPF stops at the first completed, solved path.* The driver has no `if`,
   but `&&`/`||`, null-aware equality and array loads with a symbolic pool
   index still branch inside SPF. Stopping at the first solution gives exactly
   one input vector per sequence.
3. *Frame rule per model entry for collections.* A Set/Map/array is modelled as
   one flag/value per pool key. When a postcondition mentions a collection only
   through keyed access (`E.contains(x)`, `M.get(k)`, `A[i]`,
   `Adj.get(a).contains(b)`), only those entries get fresh values; every other
   entry keeps its value. Any other mention (e.g. `size()`) makes the whole
   collection fresh. With whole-collection freshness, hashmap, bst and lrucache
   produced tests that failed a PRE check on the correct library: the model
   "forgot" keys that the real library keeps.
4. *How INCORRECT SEQUENCE is located.* The driver is straight-line with one
   assumption per line. The listener records the highest driver line at which
   SPF pruned a path, which is the first clause no path got past. That one SPF
   run names the call and clause; there is no prefix re-running.
5. *Nullable values.* Strings are a pool index plus a 0/1 null flag; boxed
   numbers are a value plus a null flag. Fresh results and state may be null,
   which is needed because, for example, heap's `minValue` stays null by the
   frame rule.
6. *Pooled numbers* (bst keys) are modelled as `101 + 2*index`, so they can
   appear in arithmetic and comparisons, and the index is the Set key.
7. *Spec starting values added (the only spec change):* `IntArray.spec`:
   `int[] A = {0, 0, 0, 0};` and `int length = 4;`. `LruCache.spec`:
   `int capacity = 2;`. These are the only state variables whose `reset()`
   value differs from Java's default.
8. *Phases 2 and 3 share one child JVM per mutant.* The checked-in sequence's
   test runs first. Only if it passes do the passing family tests run, in
   order, until the first failure. Results are the same as running the two
   phases separately, with one mutant compile and one JVM start.
9. *Phase 3 uses every passing family test* (the old runner capped it at 20).
10. *Workers = 9, not 16.* `workers = min(cores, libraries, free MB / 300)`. An
    SPF process peaked at 174 MB resident (measured), and the machine had
    about 3 GB free.
11. *A library whose test is missing or fails on the correct library makes its
    mutants INVALID* ("the library has no test"). For the checked-in sequences
    this only affected the three unsupported libraries.
12. *Kill classification.* PRE/POST from the assertion message prefix, TIMEOUT
    from JUnit's `TestTimedOutException` or the JVM watchdog, EXCEPTION for
    anything else thrown.
13. *Unconstrained values* are small, distinct, non-zero and deterministic
    (7, 9, 11, ... for ints; pool values in rotation for pooled slots).
14. *SPF time limit lowered from 120 s to 60 s* per sequence, so that a stuck
    solve cannot hold a worker for long. No sequence hit it (see part 9).
15. *Old results.* `results-old/` holds the files that were in `results/` and
    `SUMMARY.txt` when this work started (the uncommitted random-sampling run).
    The committed white-box results are still in git history.
16. *The random sampler was replaced on the current branch (`genatc-blackbox`)*,
    restoring the SPF code and the jpf-core/jpf-symbc Maven dependencies from
    this branch's last commit. `scripts/setup-spf.sh` was still present and
    the SPF checkouts were already built, so nothing had to be reinstalled.
