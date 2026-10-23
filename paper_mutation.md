# GENATC mutation experiments: TestGen concretisation (genCTC)

Every number below comes from a run made on 2026-10-23 on the machine in part 1.

GENATC is unchanged up to the abstract test case (ATC): input checks, the
shared IR, the contract and the sequence. After that, the TestGen algorithm
from the "Test the REST" slides replaces the old contract-only driver:

    genCTC(t, L, sigma):
      if not isAbstract(t): return t
      t' = rewriteATC(t, L)
      L' = symex(t', sigma)          // SPF; ready calls run on the real library
      if L' == NO_SOLUTION: reject t
      return genCTC(t', L', sigma)

The library is never read. It is only called, by SPF while symex runs the
ready calls and by the final JUnit test, and only with concrete arguments. A
JPF listener (the black-box guard) fails the run if a symbolic value ever
reaches it. Because generation runs the library, each test was generated
**once, on the correct library**, compiled once, and that same compiled test
was run unchanged on every mutant.

Raw logs and tables:
- Correct-library check: `./verify-generated.sh` output (kept at the session
  scratchpad, not committed); the traces are in
  `pl-platform-testing/golden/<key>/ATC.txt`.
- Hand-written run: `genatc-mutation/target/handwritten.log` (not committed).
- Generated suites: `genatc-mutation/operator-suites/SUMMARY.txt` and
  `genatc-mutation/operator-suites/results/*.tsv`; the log is
  `genatc-mutation/target/operator-suites.log` (not committed).
- Previous contract-only design: `genatc-mutation/operator-suites/SUMMARY-contract-only.txt`
  and git history of this file.

## 1. Setup

| Item | Value |
|---|---|
| Java | OpenJDK 11.0.32.1 (2026-08-18) |
| jpf-core | commit `f70927c` (`~/.genatc-spf/jpf-core`) |
| jpf-symbc | commit `3bedf0b` (`~/.genatc-spf/jpf-symbc`) |
| Solver | Z3 (`symbolic.dp = z3`) |
| Symbolic int range | `symbolic.min_int = -1000`, `symbolic.max_int = 1000` |
| SPF time limit | `search.time_limit = 60` s per genCTC round (process killed at 90 s); depth limit 300 |
| SPF heap | `-Xmx384m` per SPF process |
| SPF runs per sequence | one per genCTC round: the first symex, then one per call with arguments |
| Search | stops at the first completed path whose path condition Z3 solves |
| Pool | 3 values (`"s1","s2","s3"`; numeric keys 101, 103, 105), plus, for an argument whose precondition tests membership in a state collection, the members that real collection holds at that point |
| Test time limit | `@Test(timeout = 2000)` per test method; child JVM watchdog 20 s + 3 s per test |
| Family size per library | 260 for every library (see part 9) |
| Machine | AMD Ryzen 7 5800H, 16 logical cores, 6.7 GiB RAM (WSL2), Ubuntu 24.04.1 LTS, kernel 6.18.54.1-microsoft-standard-WSL2 |
| Parallelism (operator suites) | 11 library workers (`min(cores 16, libraries 14, free MB / 300)`) |
| Wall clock: correct-library check (experiment 1) | 53.3 s for `./verify-generated.sh` (build, all 14 libraries generated and run, golden comparison) |
| Wall clock: hand-written mutants (experiment 2) | 126.4 s total (`real`), of which 121 s in the runner after the build |
| Wall clock: generated operator suites (experiment 3) | 41 min 56 s total (`real`); 2510 s in the runner after the build. Per library (phase 1 + phases 2–3): from 934 s + 39 s (orderservice) to 2391 s + 104 s (mathlib) |

## 2. Generation table (checked-in sequences, correct library)

"Rounds" counts genCTC rounds that turned inputs concrete (one per call with
arguments). The SPF runs are rounds + 1: the first symex on the ATC itself,
then one per round; the last one runs the whole, fully concrete test case and
checks every assume. SPF time is the sum over those runs, from experiment 2's
baseline step.

| Library | Sequence | Test generated | Passes on correct library | Failing clause | genCTC rounds | SPF runs | SPF time (ms) |
|---|---|---|---|---|---|---|---|
| stack | push -> push -> pop -> peek | yes | yes | — | 2 | 3 | 2166 |
| queue | enqueue -> enqueue -> dequeue -> front -> isEmpty | yes | yes | — | 2 | 3 | 2042 |
| mathlib | abs -> gcd -> power -> factorial | yes | yes | — | 4 | 5 | 3080 |
| arraylib | set -> set -> get -> sum -> indexOf | yes | yes | — | 4 | 5 | 3079 |
| linkedlist | addFirst -> addLast -> removeFirst -> indexOf | yes | yes | — | 3 | 4 | 2578 |
| heap | insert -> insert -> insert -> peekMin -> extractMin | yes | yes | — | 3 | 4 | 5378 |
| set | add -> add -> contains -> remove | yes | yes | — | 4 | 5 | 3208 |
| hashmap | put -> put -> getOldValue -> remove | yes | yes | — | 4 | 5 | 3153 |
| bst | insert -> insert -> insert -> contains -> min -> delete | yes | yes | — | 5 | 6 | 3904 |
| graph | addVertex -> addVertex -> addEdge -> degree -> hasEdge | yes | yes | — | 5 | 6 | 3865 |
| lrucache | put -> put -> put -> get -> restore | yes | yes | — | 5 | 6 | 3856 |
| taskqueue | submit -> getResult -> cancelTask | yes | yes | — | 3 | 4 | 2492 |
| ticketservice | numSeatsAvailable -> findAndHoldSeats -> reserveSeats -> numSeatsAvailable | yes | yes | — | 2 | 3 | 1899 |
| orderservice | restock -> placeOrder -> ship -> stockLevel | yes | yes | — | 4 | 5 | 3216 |

All 14 libraries are supported now. taskqueue, ticketservice and orderservice
were unsupported in the contract-only design; part 10 explains why they work
here.

Inputs genCTC chose (this is what every mutant was tested with):

| Library | Inputs |
|---|---|
| stack | push "s1", push "s2" |
| queue | enqueue "s1", enqueue "s2" |
| mathlib | abs(7)\*, gcd(1, 1), power(1, 0), factorial(0) |
| arraylib | set(0, 9\*), set(0, 13\*), get(0), indexOf(17\*) |
| linkedlist | addFirst(7\*), addLast(9\*), indexOf(11\*) |
| heap | insert(7\*), insert(9\*), insert(11\*) |
| set | add "s1", add "s2", contains "s3", remove "s1" |
| hashmap | put("s1", 9\*), put("s3", 13\*), getOldValue("s3"), remove("s3") |
| bst | insert 101, 103, 105; contains(101); delete(103) |
| graph | addVertex "s1", "s2"; addEdge("s1", "s1"); degree("s2"); hasEdge("s1", "s1") |
| lrucache | put("s1","s2"), put("s3","s1"), put("s2","s3"), get("s2"), restore("s1","s3") |
| taskqueue | submit("s1"), getResult(1), cancelTask(1) — `1` is the id the real `submit` returned, picked from the real `Tasks` map |
| ticketservice | findAndHoldSeats(1, "s2"), reserveSeats(1, "s2") — `1` picked from the real `Holds` map |
| orderservice | restock("s1", 1), placeOrder("s1", 1), ship(1), stockLevel("s1") — `1` picked from the real `Open` map |

\* left free by the path condition: GENATC fills in a small, distinct,
non-zero literal (7 + 2 × the input's position in the ATC).

Pooled inputs are chosen by a symbolic index over the candidates, and SPF
explores the candidate at position (input's position in the ATC) mod 3 first.
So a free pooled input gets the pool values in rotation ("s1", "s2", "s3",
...), and a constrained one (`M.containsKey(key)`, `!C.containsKey(k)`,
`V.contains(name)`) gets the first candidate in that order that the real
state satisfies. This is why, unlike the contract-only run, repeated values
are now the exception: hashmap's later keys are "s3" because "s2" is not in
the map when they are chosen, graph's `addEdge` gets ("s1","s1") because "s3"
was never added.

### Worked example: stack

The ATC (`getATC`), printed from the IR:

    // starting state: the library's real state after Helper.reset()
    // call 0: push
    old0_size := Helper.size
    c0_elem := input<String>()
    assume(c0_elem != null)
    push(c0_elem)
    assert(Helper.size == old0_size + 1)
    assert(Helper.top == c0_elem)
    // call 1: push
    old1_size := Helper.size
    c1_elem := input<String>()
    assume(c1_elem != null)
    push(c1_elem)
    assert(Helper.size == old1_size + 1)
    assert(Helper.top == c1_elem)
    // call 2: pop
    old2_size := Helper.size
    assume(Helper.size > 0)
    c2_result := pop()
    assert(Helper.size == old2_size - 1)
    // call 3: peek
    old3_size := Helper.size
    assume(Helper.size > 0)
    c3_result := peek()
    assert(c3_result == Helper.top)
    assert(Helper.size == old3_size)

pop's `\result == poppedElem` and peek's `\result == peekedElem` only name
the result and are not asserted. Calls are numbered from 0.

**Round 0** — t' = rewriteATC(t, []) = t. symex runs 5 ready statements and
stops at `push(c0_elem)` (its argument is symbolic). SPF runs `Helper.reset()`,
the snapshot and the symbolic pick of `c0_elem`; pc = `(c0_elem != null)`.
L' = `["s1"]`.

**Round 1** — t' = rewriteATC(t, ["s1"]):

    // starting state: the library's real state after Helper.reset()
    // call 0: push
    old0_size := Helper.size
    c0_elem := "s1"
    assume(c0_elem != null)
    push(c0_elem)
    assert(Helper.size == old0_size + 1)
    assert(Helper.top == c0_elem)
    // call 1: push
    old1_size := Helper.size
    c1_elem := input<String>()
    assume(c1_elem != null)
    push(c1_elem)
    ... (calls 2 and 3 as in the ATC)

symex runs 12 statements: the real `Helper.push("s1")` runs inside SPF, then
it stops at `push(c1_elem)`. pc = `("s1" != null) && (c1_elem != null)`.
L' = `["s2"]`. The SPF program for this round
(`outputs/stack/spf-work/round1/src/.../GeneratedATCs.java`) is:

    Helper.reset();
    int old0_size = Helper.size;
    String c0_elem = "s1";
    try { ok = (c0_elem != null); } catch (RuntimeException e) { ok = false; } Verify.ignoreIf(!ok);
    Helper.push(c0_elem);
    int old1_size = Helper.size;
    String c1_elem = (String) pick("c1_elem", 1, String.class, new Object[] {"s1", "s2", "s3"});
    try { ok = (c1_elem != null); } catch (RuntimeException e) { ok = false; } Verify.ignoreIf(!ok);
    // first not-ready statement: push(c1_elem) — the program ends here

**Round 2** — t' = rewriteATC(t, ["s2"]):

    // starting state: the library's real state after Helper.reset()
    // call 0: push
    old0_size := Helper.size
    c0_elem := "s1"
    assume(c0_elem != null)
    push(c0_elem)
    assert(Helper.size == old0_size + 1)
    assert(Helper.top == c0_elem)
    // call 1: push
    old1_size := Helper.size
    c1_elem := "s2"
    assume(c1_elem != null)
    push(c1_elem)
    assert(Helper.size == old1_size + 1)
    assert(Helper.top == c1_elem)
    // call 2: pop
    old2_size := Helper.size
    assume(Helper.size > 0)
    c2_result := pop()
    assert(Helper.size == old2_size - 1)
    // call 3: peek
    old3_size := Helper.size
    assume(Helper.size > 0)
    c3_result := peek()
    assert(c3_result == Helper.top)
    assert(Helper.size == old3_size)

Every statement is ready: symex runs all 26 (both pushes, pop and peek run on
the real library). pc = `("s1" != null) && ("s2" != null) && (Helper.size > 0) && (Helper.size > 0)`,
which holds, so L' = `[]`. genCTC is called with t' and `[]`; t' has no input
command, so it is returned. 2 rounds, 3 SPF runs.

**The concrete test** (`golden/stack/GeneratedATCsTest.java`) is exactly that
t', statement for statement:

    @Test(timeout = 2000)
    public void stackDryRun() {
        Helper.reset();

        // call 0: push
        int old0_size = Helper.size;
        String c0_elem = "s1";
        assertTrue("PRE push: elem != null", c0_elem != null);
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        int old1_size = Helper.size;
        String c1_elem = "s2";
        assertTrue("PRE push: elem != null", c1_elem != null);
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        int old2_size = Helper.size;
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        String c2_result = Helper.pop();
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        int old3_size = Helper.size;
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        String c3_result = Helper.peek();
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

An incorrect sequence is rejected in the round that reaches it:
push -> pop -> pop gives, in round 1 (all calls ready, the second pop's assume
fails on the real state, every path is pruned there),
`INCORRECT SEQUENCE: call 2 (pop): size > 0` and no test
(`GeneratorPipelineTest.incorrectSequenceIsRejectedAtTheCallThatCannotRun`).

## 3. Q1: hand-written mutants by library

| Library | Mutants | Killed | Survived | Invalid | Score |
|---|---|---|---|---|---|
| stack | 11 | 8 | 3 | 0 | 72.7% |
| queue | 8 | 7 | 1 | 0 | 87.5% |
| mathlib | 12 | 5 | 7 | 0 | 41.7% |
| arraylib | 9 | 5 | 4 | 0 | 55.6% |
| linkedlist | 8 | 5 | 3 | 0 | 62.5% |
| heap | 9 | 5 | 4 | 0 | 55.6% |
| set | 6 | 6 | 0 | 0 | 100.0% |
| hashmap | 11 | 10 | 1 | 0 | 90.9% |
| bst | 9 | 5 | 4 | 0 | 55.6% |
| graph | 8 | 4 | 4 | 0 | 50.0% |
| lrucache | 8 | 5 | 3 | 0 | 62.5% |
| taskqueue | 11 | 10 | 1 | 0 | 90.9% |
| ticketservice | 13 | 12 | 1 | 0 | 92.3% |
| orderservice | 14 | 13 | 1 | 0 | 92.9% |
| **All** | **137** | **100** | **37** | **0** | **73.0%** |

On the 11 libraries the contract-only design supported (99 mutants), the
score is 65 / 99 = 65.7%.

Four of the 100 kills are **frozen-id kills** (part 5): the mutant hands out a
different id than the correct library, and the test, generated on the correct
library, uses the id that library returned. Without them the score is
96 / 137 = 70.1%.

## 4. Q2: hand-written mutants by operator

| Operator | Abbr | Mutants | Killed | Survived | Score |
|---|---|---|---|---|---|
| arithmetic-operator-replacement | AOR | 6 | 4 | 2 | 66.7% |
| arithmetic-operator-insertion | AOI | 7 | 7 | 0 | 100.0% |
| relational-operator-replacement | ROR | 5 | 2 | 3 | 40.0% |
| conditional-operator-replacement | COR | 2 | 2 | 0 | 100.0% |
| conditional-operator-insertion | COI | 4 | 2 | 2 | 50.0% |
| unary-operator-insertion | UOI | 7 | 7 | 0 | 100.0% |
| absolute-value-insertion | ABS | 2 | 2 | 0 | 100.0% |
| constant-replacement | CRP | 7 | 4 | 3 | 57.1% |
| condition-to-constant | CTC | 2 | 2 | 0 | 100.0% |
| loop-bound-replacement | LBR | 4 | 0 | 4 | 0.0% |
| return-value-replacement | RVR | 14 | 8 | 6 | 57.1% |
| statement-deletion | SDL | 41 | 35 | 6 | 85.4% |
| statement-replacement | STR | 6 | 3 | 3 | 50.0% |
| argument-replacement | ARP | 13 | 5 | 8 | 38.5% |
| method-call-replacement | MCR | 5 | 5 | 0 | 100.0% |
| side-effect-insertion | SEI | 10 | 10 | 0 | 100.0% |
| variable-replacement | VRO | 2 | 2 | 0 | 100.0% |

(No mutant is invalid; score = killed / (killed + survived).)

## 5. What killed them (hand-written, 100 kills)

| Kill kind | Count |
|---|---|
| POST assertion failed | 91 |
| PRE assertion failed | 5 |
| Library threw an exception | 4 |
| Timeout | 0 |

The four exception kills: ARRAY_SUM_RUNS_PAST_END (ArrayIndexOutOfBoundsException),
BST_INSERT_NEVER_GROWS (NullPointerException), HEAP_SIFT_DOWN_UNGUARDED
(IndexOutOfBoundsException), LRU_EVICTS_ON_EVERY_MISS (IndexOutOfBoundsException).

The five PRE kills:
- LRU_EVICTS_MOST_RECENT — `PRE restore: !C.containsKey(evictedKey)`. genCTC
  chose `restore("s1", ...)` because the correct library had evicted "s1"; the
  mutant evicts "s3" instead, so "s1" is still cached. A real kill: the
  mutant's state differs from what the contract allows the test to rely on.
- TASKQUEUE_ID_OFF_BY_ONE, TASKQUEUE_SUBMIT_ID_NEGATED (`PRE getResult:
  Tasks.containsKey(taskId)`), LEDGER_OPEN_ID_OFF_BY_ONE (`PRE ship:
  Open.containsKey(orderId)`), TICKETSERVICE_HOLD_ID_FROM_SEAT_PRODUCT
  (`PRE reserveSeats: Holds.containsKey(seatHoldId)`) — **frozen-id kills**.
  The test passes the literal id `1` that the correct library returned. Each
  mutant hands out a different id (2, -1, 2, a product of seat numbers) and
  files the task under it, so it is self-consistent, but `1` is not a key.
  The contract never says which id is issued, so these kills come from the
  test being generated on the correct library, not from a clause the mutant
  breaks. They count as kills under the kill rule (a PRE fails); all four
  were declared `expect: survives`.

## 6. Q3: why the 37 hand-written survivors survive

Each survivor is in exactly one group. The grouping comes from reading each
mutant's change next to the inputs in part 2; it was not checked by running
extra tests. The generated families (part 9) measure group (3)/(5)-type
effects for the generated mutants.

| Group | Count |
|---|---|
| (1) the language cannot say it | 9 |
| (2) the contract could say it but does not | 7 |
| (3) the sequence never asks | 4 |
| (4) the precondition rules it out | 1 |
| (5) the inputs genCTC chose did not hit it | 16 |
| **Total** | **37** |

**(1) The language cannot say it (9)**
- ARRAY_SUM_SKIPS_FIRST — no summation over A's cells; only `total >= 0` can be said (cell 0 holds 13, the rest 0, so the wrong sum is 0).
- ARRAY_INDEXOF_ALWAYS_FIRST — "first position holding value" needs a quantifier; only the range is said.
- LIST_INDEXOF_NEVER_MATCHES — -1 is inside the stated range `-1 <= position < size`.
- LIST_INDEXOF_NEVER_WALKS — same gap, reached by never entering the loop.
- MATH_GCD_IGNORES_FIRST_ARG — the spec can say "a positive common divisor", not "the greatest"; 1 qualifies (and gcd(1, 1) is 1 anyway).
- MATH_POWER_ONE_TOO_FEW — no exponentiation in the grammar; `powerValue > 0` is all that can be said (and power(1, 0) is 1 either way).
- MATH_POWER_ADDS_INSTEAD — same: any positive answer satisfies `powerValue > 0`.
- MATH_FACTORIAL_STOPS_EARLY — `factValue >= 1` bounds the answer but cannot compute it (and factorial(0) is 1 either way).
- TICKETSERVICE_ALLOCATES_WORST_SEATS — which seats a hold gets cannot be named; the spec only has counts and the two maps.

**(2) The contract could say it but does not (7)**
- STACK_POP_FROM_BOTTOM — pop's only result clause is `\result == poppedElem`, which names the result; `poppedElem == \old(top)` would say which element.
- QUEUE_ENQUEUE_AT_FRONT — nothing says which end enqueue writes; `head` is re-derived, so the queue stays self-consistent.
- HASHMAP_PUT_OLDVAL_FABRICATED — `\result == oldVal` names the result only; nothing ties oldVal to the previous entry.
- LRU_PUT_HIDES_EVICTION — the third put does evict "s1", but `\result == evictedKey` names the result only.
- LRU_RESTORE_LOSES_VALUE — restore never says `C.get(evictedKey) == value`.
- TASKQUEUE_RESULT_IS_NULL — getResult only says Results has an entry under taskId, not what it holds.
- CATALOGUE_TAKE_LEAVES_SHELF — nothing relates Stock's contents to the stockUnits counter.

**(3) The sequence never asks (4)**
- STACK_PUSH_AT_BOTTOM — the pop re-derives top before peek reads it; push -> push -> peek -> pop would separate the orders.
- STACK_PEEK_RETURNS_BOTTOM — only one element is left when peek runs, so bottom = top.
- HEAP_EXTRACT_NO_REFILL — the sequence ends at extractMin; nothing reads the heap afterwards.
- GRAPH_ADDVERTEX_REDECLARES — no vertex is re-added after it has an edge.

**(4) The precondition rules it out (1)**
- BST_INSERT_TIES_GO_LEFT — insert requires `!Keys.contains(key)`, so equal keys never meet.

**(5) The inputs genCTC chose did not hit it (16)**
- ARRAY_SET_WRONG_CELL — both sets write index 0, which is the cell the mutant always writes.
- ARRAY_GET_WRONG_CELL — get reads index 0, which is the cell the mutant always reads.
- BST_INSERT_ALWAYS_RIGHT — keys 101, 103, 105 arrive in increasing order and all go right anyway.
- BST_MIN_WALKS_RIGHT — the root has no left child, so the loop never runs and min is the root.
- BST_CONTAINS_DESCENDS_WRONG_WAY — contains(101) is answered at the root, before any descent.
- GRAPH_ADDEDGE_ONE_DIRECTION — the edge is s1–s1, so one direction is both.
- GRAPH_DEGREE_ALWAYS_ZERO — degree is asked about "s2", which has no edge, so 0 is right.
- GRAPH_HASEDGE_ALWAYS_TRUE — hasEdge is asked about the pair that was joined.
- HEAP_NO_SIFT_UP — 7, 9, 11 are inserted in increasing order; nothing needs to sift.
- HEAP_WRONG_PARENT_INDEX — same: no sift-up is ever needed.
- HEAP_SIFT_UP_STOPS_ONE_SHORT — same.
- LIST_INDEXOF_COUNTS_FROM_ONE — the searched value (11, left free) is not in the list, so both versions answer -1.
- LRU_GET_DOES_NOT_PROMOTE — get is asked about "s2", already the most recent key.
- MATH_ABS_NO_FLIP — x = 7 (left free) is already non-negative.
- MATH_GCD_NO_LOOP — returns its first argument, and a = b = 1.
- MATH_LASTRESULT_STALE — the stale value (gcd's 1) equals power(1, 0) = 1.

## 7. Comparison with the declared `expect:` lines

The `expect:` lines were written for earlier designs and were not edited.

- 16 mutants declared `expect: killed` **survive**: ARRAY_SET_WRONG_CELL,
  ARRAY_GET_WRONG_CELL, BST_INSERT_ALWAYS_RIGHT, BST_MIN_WALKS_RIGHT,
  BST_CONTAINS_DESCENDS_WRONG_WAY, GRAPH_ADDEDGE_ONE_DIRECTION,
  GRAPH_DEGREE_ALWAYS_ZERO, HEAP_NO_SIFT_UP, HEAP_WRONG_PARENT_INDEX,
  HEAP_SIFT_UP_STOPS_ONE_SHORT, LIST_INDEXOF_COUNTS_FROM_ONE,
  LRU_PUT_HIDES_EVICTION, LRU_GET_DOES_NOT_PROMOTE, MATH_ABS_NO_FLIP,
  MATH_GCD_NO_LOOP, MATH_LASTRESULT_STALE. 15 of them are in group (5); the
  16th, LRU_PUT_HIDES_EVICTION, is in group (2).
- 7 mutants declared `expect: survives` are **killed**: HASHMAP_REMOVE_CLEARS_MAP
  and SET_REMOVE_CLEARS_EVERYTHING (the map/set now holds two distinct keys,
  so clearing it breaks `size == \old(size) - 1`), SET_CONTAINS_ALWAYS_TRUE
  (contains is now asked about "s3", which is absent), and the four
  frozen-id kills of part 5: TASKQUEUE_ID_OFF_BY_ONE,
  TASKQUEUE_SUBMIT_ID_NEGATED, LEDGER_OPEN_ID_OFF_BY_ONE,
  TICKETSERVICE_HOLD_ID_FROM_SEAT_PRODUCT. Their notes say "taskId is
  propagated, so shifting it moves every block together"; here the id is
  not propagated at run time but fixed when the test is generated, so the
  shift is visible.

Against the contract-only run, 9 of its 25 "declared killed, survived"
mutants are now killed (QUEUE_DEQUEUE_FROM_BACK, QUEUE_HEAD_NOT_REFRESHED,
QUEUE_ENQUEUE_HEAD_FROM_BACK, ARRAY_SUM_SUBTRACTS, ARRAY_GET_NEGATES,
LIST_ADDLAST_AT_FRONT, LIST_REMOVEFIRST_FROM_TAIL, LRU_NEVER_EVICTS,
LRU_EVICTS_MOST_RECENT): the inputs are now distinct and non-zero, because the
pool rotates and free values are 7, 9, 11, ... rather than Z3's zeros. One
mutant goes the other way: GRAPH_DEGREE_ALWAYS_ZERO was killed by the
contract-only test and survives now, because genCTC asks degree about "s2",
which has no edge.

## 8. Contract findings (tests that failed on the correct library)

- **Checked-in sequences:** none. All 14 generated tests pass on the correct
  library.
- **Queue `isEmpty` (`emptyFlag == false`)** and **MathLib `power`
  (`exponent <= 30`, which overflows)** are not exposed by the checked-in
  sequences: isEmpty runs with one element left, and genCTC chose power(1, 0).
- **Generated families:** 102 of the 3640 family tests fail on the correct
  library (part 9):
  - **30 are a contract finding, all the same clause: Queue `isEmpty`
    ensures `emptyFlag == false`.** Every family sequence that calls isEmpty
    on an empty queue (before any enqueue, or after the last dequeue) fails
    `POST isEmpty: emptyFlag == false` on the correct library, e.g. queue-057
    `isEmpty -> enqueue` and queue-173 `enqueue -> dequeue -> isEmpty`
    (inputs "s1"). This is the known bug in Queue.spec; it was not fixed.
    20 of the 30 had input commands and went through genCTC (the assumes
    hold; the assert is not a constraint), 10 had none.
  - **72 are incorrect sequences that genCTC never saw**: sequences with no
    input command at all (stack 24, queue 27, heap 19, linkedlist 1, bst 1),
    e.g. stack-075 `pop -> pop`, heap-131 `extractMin -> extractMin`. genCTC
    returns a non-abstract t unchanged without symex (decision 6), so their
    first precondition (`size > 0`) is checked only by the JUnit test, where
    it fails as `PRE`. They are not contract findings.
  - MathLib `power`'s overflow (`exponent <= 30`) was not exposed by any
    family test: all 260 mathlib tests pass.

## 9. Generated operator suites

Each library's family is 260 test strings generated from its spec
(seeded by the library key, so the strings are the same as in the
contract-only run for the 11 libraries it had). Each string's test was
generated once on the correct library and run on it (phase 1). Phase 2 runs
the checked-in sequence's test on every generated mutant; phase 3 runs every
phase-2 survivor against the passing family tests until one kills it.

### Phase 1: the families on the correct library

| Library | Family | Passed | Incorrect sequence | Failed | Other | Mean genCTC rounds (passed) | Seconds |
|---|---|---|---|---|---|---|---|
| stack | 260 | 78 | 158 | 24 | 0 | 2.74 | 983 |
| queue | 260 | 95 | 108 | 57 | 0 | 2.40 | 1038 |
| mathlib | 260 | 260 | 0 | 0 | 0 | 4.94 | 2391 |
| arraylib | 260 | 260 | 0 | 0 | 0 | 4.00 | 2169 |
| linkedlist | 260 | 133 | 126 | 1 | 0 | 4.03 | 1546 |
| heap | 260 | 114 | 127 | 19 | 0 | 3.17 | 1186 |
| set | 260 | 135 | 125 | 0 | 0 | 5.12 | 1988 |
| hashmap | 260 | 89 | 171 | 0 | 0 | 5.26 | 1645 |
| bst | 260 | 110 | 149 | 1 | 0 | 4.59 | 1696 |
| graph | 260 | 143 | 117 | 0 | 0 | 5.50 | 1972 |
| lrucache | 260 | 189 | 71 | 0 | 0 | 5.49 | 2258 |
| taskqueue | 260 | 66 | 194 | 0 | 0 | 4.95 | 1132 |
| ticketservice | 260 | 118 | 142 | 0 | 0 | 3.06 | 1100 |
| orderservice | 260 | 43 | 217 | 0 | 0 | 4.77 | 934 |
| **All** | **3640** | **1833** | **1705** | **102** | **0** | | |

"Incorrect sequence" means genCTC rejected the string (`INCORRECT SEQUENCE:
call i (op): clause`) and no test was generated. "Other" (solver error,
timeout, library exception, black-box violation, no progress) never
occurred. The 102 failures are part 8's 30 queue `isEmpty` POST failures and
72 PRE failures of sequences without input commands.

### Phases 2 and 3 by library (score alone / with family)

| Library | Mutants | Killed | Survived | Invalid | Score alone | Killed by family | Score with family | Contract-only: alone / with family |
|---|---|---|---|---|---|---|---|---|
| stack | 51 | 37 | 11 | 3 | 77.1% | 6 | 89.6% | 75.0% / 83.3% |
| queue | 49 | 40 | 6 | 3 | 87.0% | 1 | 89.1% | 80.4% / 87.0% |
| mathlib | 156 | 88 | 68 | 0 | 56.4% | 4 | 59.0% | 57.1% / 59.0% |
| arraylib | 92 | 36 | 52 | 4 | 40.9% | 3 | 44.3% | 34.1% / 35.2% |
| linkedlist | 84 | 20 | 42 | 22 | 32.3% | 5 | 40.3% | 27.4% / 35.5% |
| heap | 215 | 92 | 120 | 3 | 43.4% | 40 | 62.3% | 43.4% / 62.3% |
| set | 27 | 13 | 8 | 6 | 61.9% | 2 | 71.4% | 66.7% / 71.4% |
| hashmap | 33 | 20 | 8 | 5 | 71.4% | 1 | 75.0% | 60.7% / 60.7% |
| bst | 130 | 41 | 57 | 32 | 41.8% | 27 | 69.4% | 46.9% / 59.2% |
| graph | 70 | 38 | 19 | 13 | 66.7% | 10 | 84.2% | 71.9% / 73.7% |
| lrucache | 68 | 38 | 20 | 10 | 65.5% | 8 | 79.3% | 31.0% / 75.9% |
| taskqueue | 50 | 28 | 16 | 6 | 63.6% | 4 | 72.7% | — (unsupported) |
| ticketservice | 224 | 110 | 87 | 27 | 55.8% | 6 | 58.9% | — (unsupported) |
| orderservice | 185 | 110 | 47 | 28 | 70.1% | 9 | 75.8% | — (unsupported) |
| **All 14** | **1434** | **711** | **561** | **162** | **55.9%** | **126** | **65.8%** | |
| **Same 11 as contract-only** | **975** | **463** | **411** | **101** | **53.0%** | **107** | **65.2%** | **50.0% / 61.0%** |

All 162 invalid mutants are invalid because the mutated library does not
compile. Kill kinds in phase 2: POST 512, EXCEPTION 165, PRE 23, TIMEOUT 11.
Family kills in phase 3: POST 88, EXCEPTION 20, PRE 10, TIMEOUT 8.

Three libraries lost score alone: set (66.7% -> 61.9%, one more survivor),
bst (46.9% -> 41.8%, 5 fewer kills) and graph (71.9% -> 66.7%, 3 fewer
kills); for bst and graph the families more than make up for it. lrucache gained the most alone
(31.0% -> 65.5%): its checked-in test now puts three distinct keys into a
cache of capacity 2, so eviction actually happens.

### Phases 2 and 3 by operator (all 14 libraries)

| Operator | Abbr | Mutants | Killed | Survived | Invalid | Score alone | Score with family |
|---|---|---|---|---|---|---|---|
| arithmetic-operator-replacement | AOR | 128 | 77 | 45 | 6 | 63.1% | 68.0% |
| arithmetic-operator-insertion | AOI | 60 | 43 | 17 | 0 | 71.7% | 75.0% |
| relational-operator-replacement | ROR | 155 | 43 | 72 | 40 | 37.4% | 59.1% |
| conditional-operator-replacement | COR | 3 | 3 | 0 | 0 | 100.0% | 100.0% |
| conditional-operator-insertion | COI | 24 | 12 | 11 | 1 | 52.2% | 73.9% |
| unary-operator-insertion | UOI | 56 | 41 | 15 | 0 | 73.2% | 80.4% |
| absolute-value-insertion | ABS | 53 | 39 | 14 | 0 | 73.6% | 79.2% |
| constant-replacement | CRP | 194 | 71 | 121 | 2 | 37.0% | 41.7% |
| condition-to-constant | CTC | 32 | 14 | 18 | 0 | 43.8% | 68.8% |
| loop-bound-replacement | LBR | 8 | 2 | 6 | 0 | 25.0% | 25.0% |
| return-value-replacement | RVR | 92 | 58 | 34 | 0 | 63.0% | 73.9% |
| statement-deletion | SDL | 136 | 67 | 37 | 32 | 64.4% | 79.8% |
| statement-replacement | STR | 36 | 11 | 25 | 0 | 30.6% | 30.6% |
| argument-replacement | ARP | 114 | 53 | 26 | 35 | 67.1% | 82.3% |
| method-call-replacement | MCR | 75 | 49 | 24 | 2 | 67.1% | 80.8% |
| side-effect-insertion | SEI | 165 | 76 | 59 | 30 | 56.3% | 65.9% |
| variable-replacement | VRO | 103 | 52 | 37 | 14 | 58.4% | 61.8% |

## 10. Comparison with the contract-only design

Same machine, same specs, same hand-written mutants, same family seeds and
the same generated mutants for the 11 libraries both designs support.
Contract-only numbers are from the previous version of this file and
`operator-suites/SUMMARY-contract-only.txt`.

| Measure | Contract-only | genCTC, all 14 libraries | genCTC, same 11 libraries |
|---|---|---|---|
| Libraries with a test | 11 of 14 | 14 of 14 | 11 |
| Checked-in tests passing on the correct library | 11 / 11 | 14 / 14 | 11 / 11 |
| Hand-written mutation score | **53.5%** (53 / 99; 38 invalid) | **73.0%** (100 / 137; 0 invalid) | **65.7%** (65 / 99) |
| Hand-written, without the 4 frozen-id kills | — | 70.1% (96 / 137) | 65.7% (none in these 11) |
| Generated mutants, score alone | **50.0%** (437 / 874) | **55.9%** (711 / 1272) | **53.0%** (463 / 874) |
| Generated mutants, score with family | **61.0%** (533 / 874) | **65.8%** (837 / 1272) | **65.2%** (570 / 874) |
| Family tests passing on the correct library | 1532 / 2860 | 1833 / 3640 | 1606 / 2860 |
| SPF runs per sequence | 1 | rounds + 1 | rounds + 1 |

On the same 11 libraries the hand-written score rises by 12.2 points and
the generated scores by 3.0 (alone) and 4.2 (with family) points. The
hand-written gain is almost all group (5) of the contract-only run turning
into kills: the inputs are no longer Z3's repeated pool values and zeros
(part 7). The rest of the gain on all 14 comes from the three id libraries,
whose 38 hand-written mutants were invalid before and are 35 kills / 3
survivors now (4 of the kills frozen-id).

## 11. What I could not do, and decisions made without asking

**Could not do / limits**
- The survivor grouping in part 6 is my reading of each mutant against the
  chosen inputs. I did not run extra tests to confirm each group-(5) claim.
- The four frozen-id kills (part 5) count as kills under the kill rule, but
  they are not violations of any clause: the contract does not fix which id
  is issued. A test generated once on the correct library cannot avoid this
  without propagating return values at run time, which this design does not
  do (the id is concrete when the test is generated, and stays a literal).
- No golden directories were added for taskqueue, ticketservice and
  orderservice ("no new folder"). `verify-generated.sh` checks their status,
  their JUnit run and the black-box guard, but does not diff their outputs.
- `IMPLEMENTATION_NOTES.md` and `ARCHITECTURE.md` still describe earlier
  designs; I did not edit them (not asked, and they may be paper-side).

**Decisions made without asking**
1. *Calls are numbered from 0* (`// call 0: push`, `c0_elem`, `old0_size`),
   keeping the IR's existing names. The ATC shape in the request used `e1`,
   `size1` for the first call; the requested test "push -> pop -> pop is
   rejected at call 2" names the second pop, which is call 2 only when
   counting from 0.
2. *A call with no stored result is printed without a target*
   (`push(c0_elem)`, not `r1 := push(c0_elem)`). A result is stored when the
   postcondition names it (`c2_result := pop()`).
3. *Snapshots* are taken of each state variable the postcondition reads
   through `\old`, as before, and come first in each call block.
4. *What counts as a genCTC round.* genCTC starts with L empty; that first
   symex is not a round. A round is a step whose L is non-empty, i.e. one
   that turned inputs concrete. So rounds = calls with arguments, and SPF
   runs = rounds + 1. The no-progress guard is "symex returned an empty list
   while t' still has an input command"; it can therefore never fire on the
   first step by accident.
5. *NO_SOLUTION and failures* are kinds of a `Solution` object, separate from
   the list of values. NO_SOLUTION names the assume no path got past (the
   highest driver line at which SPF pruned, as before). SOLVER_ERROR,
   TIMEOUT, LIBRARY_EXCEPTION (a ready call threw on every path),
   BLACK_BOX_VIOLATION and COMPILE_ERROR are reported on their own.
6. *A sequence with no input command is returned by genCTC unchanged*,
   without symex, because the algorithm's first line returns a non-abstract
   t. Its preconditions are then checked only when the JUnit test runs. None
   of the 14 checked-in sequences is like this; in the families (part 9) such
   a test that fails a PRE on the correct library is counted as FAILED (PRE),
   and listed separately.
7. *An assume whose evaluation throws counts as false* in the SPF program
   (`try { ok = (c); } catch (RuntimeException e) { ok = false; }`), so it
   prunes the path rather than becoming a library exception. In the JUnit
   test the same exception fails the test as EXCEPTION.
8. *Candidates for a pooled input* are the 3-value pool plus the members of
   every state collection its own precondition tests it against
   (`Tasks.containsKey(taskId)` adds the keys of the real `Helper.Tasks`).
   The index is a `Debug.makeSymbolicInteger`; SPF branches on it, the real
   collection prunes candidates through the assume, and Z3 solves the index.
   The candidate at (input position mod 3) is explored first, which gives
   free pooled inputs the pool values in rotation. The picked value is read
   from a static field of the SPF program that JPF restores on backtracking.
9. *Which inputs are pooled is decided per parameter*, not per type: every
   String, and a number only when its own call's contract uses it as a
   Set/Map key (`Keys.contains(key)`). So ticketservice's `numSeats` and
   orderservice's `quantity` are symbolic, where the contract-only design
   pooled every `Integer` once any map had Integer keys.
10. *Free values.* A symbolic input the solved path condition does not
    mention gets 7 + 2 × its position in the ATC (7, 9, 11, ...).
11. *Boxed symbolic inputs* are `new Integer(Debug.makeSymbolicInteger(..))`,
    not `Integer.valueOf`, which would branch on the integer cache.
12. *The black-box guard* (`spf/listener/BlackBoxGuard`) checks, on entry to
    every method of a library class, each argument slot and each field (or
    array element) of an object argument, and every value read by a field
    instruction executed in a library class. jpf-symbc `*Constant`
    attributes are concrete and do not count. On a violation it writes
    `blackbox.txt` and stops the search; the round fails as
    BLACK_BOX_VIOLATION. It never fired in any run reported here
    (`verify-generated.sh` checks every round directory).
13. *taskqueue, ticketservice and orderservice are supported.* Their later
    calls' ids (`taskId`, `seatHoldId`, `orderId`) are pooled because the
    contract uses them as map keys, and their membership precondition makes
    the real map a candidate source. Because submit/findAndHoldSeats/
    placeOrder are ready calls by then, they have run, and their id is a key
    of the real map when the next round picks. All three checked-in tests
    pass on the correct library. Nothing in the library was read: the map is
    a state variable of the spec, read through `Helper.<name>` exactly as the
    ATC's assume reads it.
14. *Golden files* are now `ATC.txt` (the ATC, t' and L' of every round, the
    concrete test) and `GeneratedATCsTest.java`. The old contract-only
    drivers (`golden/*/GeneratedATCs.java`) were deleted: there is one SPF
    program per round now, under `outputs/<key>/spf-work/round<k>/`.
15. *Removed* with the contract model: `ModelTranslator`, `ClauseExpr`,
    `ValueExtractor`, `InputVector`, `SpfRunner`, `SpfRunResult`. Added in
    existing packages: `symbolic/{TestGen, Stmt, Expr, Sigma, Solution,
    SymexEngine}`, `spf/SpfSymexEngine`, `spf/listener/BlackBoxGuard`.
16. *Assumes stay `Verify.ignoreIf(!(c))`*, for the reason given before:
    `Debug.assume` in jpf-symbc 3bedf0b prunes only when its argument still
    carries a symbolic attribute.
17. *Workers = 11* for experiment 3: `min(16 cores, 14 libraries, 3678 MB
    free / 300)`.
18. *Old results.* The uncommitted contract-only `SUMMARY.txt` was copied to
    `operator-suites/SUMMARY-contract-only.txt` before experiment 3
    overwrote `SUMMARY.txt` and `results/`.
