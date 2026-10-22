# Mutation testing the generated black-box suites

`verify-generated.sh` answers *does the generated test suite pass on the real
library?* A suite that asserts nothing passes too, so mutation testing asks the
useful question: **when the library is broken, does the generated suite notice?**

## What is being measured

The generator is **black-box**. It never looks inside a library and never
explores paths (no SPF, no symbolic execution). For each library it:

1. parses the `.spec` contract and the `.tests` test string, and checks both;
2. picks the library's **one model state variable**, if it has one (see below);
3. runs the test string against the **unmodified** library with **random
   inputs**, many times (500 attempts, fixed seed 42), and keeps up to 10
   distinct input vectors on which every check held;
4. prints those vectors as `GeneratedATCsTest.java`, one `@Test` per vector.

Mutation testing then treats that generated class as the oracle. For each
mutant it seeds one fault into a private copy of the library, compiles the
**same, unchanged** test class against it, and runs it. The suite is **not**
regenerated per mutant. If it were, the sampler would pick inputs the mutant
happens to pass, and that is not black-box testing.

### Stateful libraries: one global model variable

A stateful library (stack, queue, …) has one int state variable that its
preconditions guard calls with, such as `size` in `pop`'s `requires: size > 0`.
The generated test keeps **its own copy** of that variable in a single global
field, `state`:

```java
/** The one global model state variable: the spec's 'size', kept by the test, not read from the library. */
private static int state;

@Test(timeout = 2000)
public void run1() {
    Helper.reset();
    state = 0;

    // call 0: push
    String c0_elem = "s3";
    assertTrue("PRE push: elem != null", c0_elem != null);
    int old0_size = Helper.size;
    Helper.push(c0_elem);
    state = state + 1;                                   // from: size == \old(size) + 1
    assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
    ...
    // call 2: pop
    assertTrue("SEQUENCE pop: size > 0", state > 0);     // checked against the model, not the library
    ...
```

- Each precondition conjunct that reads the model variable and no parameter
  (`size > 0`) is a **SEQUENCE** check. Its truth depends only on which calls
  came before it.
- The model moves by the call's own `v == \old(v) ± …` clause and is never
  read back from the library.
- So a test string that calls something out of order is **called out at
  generation time**, before any test is written:

  ```
  $ (test string: push -> pop -> pop)
  INCORRECT SEQUENCE: call 2 (pop) is out of sequence: requires size > 0,
  but the model's size is 0 after the calls before it
  ```

The model variable chosen for each library:

| Library | Model variable | Comes from |
|---|---|---|
| stack, queue, linkedlist, heap, bst | `size` | `requires: size > 0` |
| arraylib | `length` | `requires: index < length` |
| lrucache | `capacity` | `requires: capacity > 0` |
| mathlib | none (stateless) | no precondition reads state |
| set, hashmap, graph | none | preconditions only test membership (`M.containsKey(key)`) |

For the last group, an out-of-order call is still called out, by the sampler
instead of the model. For example, `remove -> put` on hashmap gives:

```
INCORRECT SEQUENCE: no random input met the precondition of call 0 (remove)
in 500 attempts — the calls before it never set up what it requires: M.containsKey(key)
```

### Stateless libraries: random inputs, test string must pass

For mathlib there is no model. Every argument is drawn at random: half the
time a small value in [-10, 10], otherwise a value in [-1000, 1000]. A draw is
retried until that call's precondition holds. An attempt is kept only if the
whole test string ran with every postcondition holding, so the delivered
suite always passes on the real library.

`String` parameters, and any value a spec uses as a `Set`/`Map` key (bst's
`int key`), are drawn from a small shared pool (`"s1","s2","s3"` /
`101,103,105`). This lets a later call's `contains`/`containsKey`
precondition name a value an earlier call stored.

Attempts that broke a postcondition on the **unmodified** library are thrown
away and reported. Those are contract findings, not generator bugs:

```
mathlib:  discarded 76 attempt(s):  POST at call 2 (power): powerValue > 0   (int overflow; spec allows exponent <= 30)
arraylib: discarded 243 attempt(s): POST at call 3 (sum): total >= 0         (spec never forbids negative values)
```

## Setup

Requirements: **JDK 11+** and **Maven 3**. SPF, `jpf-core` and `jpf-symbc`
are no longer needed (`scripts/setup-spf.sh` is obsolete), and nothing needs
the network after Maven's first dependency download.

```bash
# from the repository root
mvn -o install -DskipTests            # builds pl-platform-testing and genatc-mutation
                                      # (drop -o the very first time, to download JUnit/plugins)

./verify-generated.sh                 # every library generates, and its suite passes on the real library

./genatc-mutation/run-mutations.sh                    # hand-written mutants, every library
./genatc-mutation/run-mutations.sh stack queue        # a subset

./genatc-mutation/run-operator-suites.sh              # generated mutants + generated test strings
./genatc-mutation/run-operator-suites.sh stack        # a subset
```

Knobs (JVM system properties for `GenatcMain`):

| Property | Default | Meaning |
|---|---|---|
| `genatc.seed` | `42` | random seed; the same seed always gives the same suite |
| `genatc.attempts` | `500` | sampling attempts per test string |
| `genatc.maxTestsPerClass` | `10` | distinct passing vectors kept as `@Test` methods |

Each generated test has `@Test(timeout = 2000)`, so a mutant that loops
forever fails its test instead of hanging the run.

Where things are written:

| Path | What |
|---|---|
| `pl-platform-testing/outputs/<lib>/…/GeneratedATCsTest.java` | the suite, from `GenatcMain` / `verify-generated.sh` |
| `pl-platform-testing/outputs/<lib>/sampler-work/` | the generated `GeneratedSampler.java` that drew the inputs |
| `genatc-mutation/target/mutation/suites/` | baseline suites the mutation runner generated |
| `genatc-mutation/target/mutation/mutants/<ID>/` | each mutant's sandboxed library + the run |
| `genatc-mutation/operator-suites/` | generated mutants, test strings, per-row TSVs, `SUMMARY.txt` |

## Results: hand-written mutants (`run-mutations.sh`)

Baseline (generated from and run on the unmodified libraries). All 11
supported libraries pass:

```
stack      PASS  (9 test(s))      queue      PASS  (9 test(s))     mathlib    PASS  (10 test(s))
arraylib   PASS  (10 test(s))     linkedlist PASS  (10 test(s))    heap       PASS  (10 test(s))
set        PASS  (10 test(s))     hashmap    PASS  (10 test(s))    bst        PASS  (10 test(s))
graph      PASS  (10 test(s))     lrucache   PASS  (10 test(s))
```

(stack and queue have 9 tests because two pooled pushes give only 3 × 3 = 9
distinct vectors.)

```
library       mutants   killed  survived  invalid score
arraylib            9        7         2        0  77.8%
bst                 9        8         1        0  88.9%
graph               8        7         1        0  87.5%
hashmap            11       10         1        0  90.9%
heap                9        8         1        0  88.9%
linkedlist          8        5         3        0  62.5%
lrucache            8        6         2        0  75.0%
mathlib            12        9         3        0  75.0%
orderservice       14        0         0       14   0.0%
queue               8        7         1        0  87.5%
set                 6        6         0        0 100.0%
stack              11        8         3        0  72.7%
taskqueue          11        0         0       11   0.0%
ticketservice      13        0         0       13   0.0%
ALL               137       81        18       38  81.8%
```

The score is `killed / (killed + survived)` over the **99 runnable** mutants:
**81 killed, 18 survived**. The 38 invalid mutants all belong to the three
libraries that are unsupported by design (taskqueue, ticketservice,
orderservice). Each needs an id returned by one call passed into a later
call, and the no-propagation rule forbids that, so no suite exists to run
them against.

By operator:

```
operator                           abbr   mutants   killed  survived score
arithmetic-operator-replacement    AOR          6        2         1  66.7%
arithmetic-operator-insertion      AOI          7        4         0 100.0%
relational-operator-replacement    ROR          5        2         3  40.0%
conditional-operator-replacement   COR          2        2         0 100.0%
conditional-operator-insertion     COI          4        2         2  50.0%
unary-operator-insertion           UOI          7        6         0 100.0%
absolute-value-insertion           ABS          2        1         0 100.0%
constant-replacement               CRP          7        3         2  60.0%
condition-to-constant              CTC          2        2         0 100.0%
loop-bound-replacement             LBR          4        2         1  66.7%
return-value-replacement           RVR         14        9         3  75.0%
statement-deletion                 SDL         41       24         1  96.0%
statement-replacement              STR          6        5         0 100.0%
argument-replacement               ARP         13        6         5  54.5%
method-call-replacement            MCR          5        3         0 100.0%
side-effect-insertion              SEI         10        6         0 100.0%
variable-replacement               VRO          2        2         0 100.0%
```

(The `mutants` column counts the invalid ones too; `score` does not.)

### What killed them

Every kill is a failing **POST** assertion, an exception thrown by the mutant
(e.g. `ARRAY_SUM_RUNS_PAST_END`: `Index 4 out of bounds`), or, once,
a **PRE** check on library state (`LRU_EVICTS_MOST_RECENT`:
`!C.containsKey(evictedKey)`). No mutant was killed by a SEQUENCE check, and
none could be. The model lives in the test, not the library, so a mutant
cannot move it. The model's job is the one shown above: calling out an
incorrect test string before a suite is generated at all.

### The 18 survivors

Same groups as before. Each survivor's `note` in `mutations/*.mutants` says why.

| Why it survives | Mutants |
|---|---|
| **The spec can't say it** (range clause, not the value) | `ARRAY_SUM_SKIPS_FIRST`, `ARRAY_INDEXOF_ALWAYS_FIRST`, `LIST_INDEXOF_NEVER_MATCHES`, `LIST_INDEXOF_NEVER_WALKS`, `MATH_GCD_IGNORES_FIRST_ARG`, `MATH_POWER_ADDS_INSTEAD`, `MATH_FACTORIAL_STOPS_EARLY` |
| **The spec could say it and doesn't** | `STACK_POP_FROM_BOTTOM`, `QUEUE_ENQUEUE_AT_FRONT`, `LRU_RESTORE_LOSES_VALUE`, `HASHMAP_PUT_OLDVAL_FABRICATED` |
| **The test string never asks** | `STACK_PUSH_AT_BOTTOM`, `STACK_PEEK_RETURNS_BOTTOM`, `HEAP_EXTRACT_NO_REFILL`, `GRAPH_ADDVERTEX_REDECLARES` |
| **The precondition rules it out** | `BST_INSERT_TIES_GO_LEFT` (insert requires the key absent, so ties never happen) |
| **Random inputs didn't hit it** (new, see below) | `LIST_INDEXOF_COUNTS_FROM_ONE`, `LRU_PUT_HIDES_EVICTION` |

### Where black-box random inputs differ from the old SPF run

`run-mutations.sh` exits non-zero because 7 mutants now disagree with the
`expect:` they were declared with under the SPF pipeline (plus the 38
unsupported-library mutants, which were already reported as unrunnable before):

| Mutant | Declared | Now | Why |
|---|---|---|---|
| `SET_CONTAINS_ALWAYS_TRUE` | survives | **killed** | random pool draws sometimes ask about an element never added |
| `SET_REMOVE_CLEARS_EVERYTHING` | survives | **killed** | random draws add two *different* elements, so clearing both is visible |
| `HASHMAP_REMOVE_CLEARS_MAP` | survives | **killed** | same: two different keys |
| `GRAPH_HASEDGE_ALWAYS_TRUE` | survives | **killed** | random vertex names ask about an unjoined pair |
| `MATH_POWER_ONE_TOO_FEW` | survives | **killed** | random large bases overflow differently with one fewer multiplication, flipping `powerValue > 0`. A real kill, but incidental. |
| `LIST_INDEXOF_COUNTS_FROM_ONE` | killed | **survives** | a random `Integer` almost never equals a value already in the list, so `indexOf` answers `-1` either way |
| `LRU_PUT_HIDES_EVICTION` | killed | **survives** | the random keys rarely fill the cache with three *distinct* keys, so the eviction this mutant hides seldom happens |

The first five are what random inputs buy you. The fixed checked-in values
always reused one element or key; random draws vary them. The last two are
what random inputs cost: a value that must *exactly* match earlier state
(`indexOf` on a stored value, three distinct keys) is something a solver
finds deliberately and a random draw only finds by luck. Pooling `Integer`
values used with `List.contains`/`indexOf` would likely recover
`LIST_INDEXOF_COUNTS_FROM_ONE`.

The `expect:` lines in `mutations/*.mutants` have **not** been edited. They
still record the SPF-era expectations, so the table above is the diff.

## Results: generated mutants (`run-operator-suites.sh`)

OPERATOR_SUITES_PLACEHOLDER

## The mutation-set format

One file per library in `genatc-mutation/mutations/`, named after it
(`stack.mutants`). Each entry is a named block of `key: value` fields:

```
mutant STACK_PUSH_AT_BOTTOM {
    description: push inserts at the bottom of the list instead of the top
    operator:    argument-replacement
    in:          push
    find:        S.add(elem);
    replace:     S.add(0, elem);
    expect:      survives
    note:        Stack.spec constrains size and top, never the order of S. …
}
```

| Field | Meaning |
| --- | --- |
| `description` | Plain-English statement of the fault. Required. |
| `operator` | One of the 17 operators below. Required; the parser rejects anything else. |
| `in` | The method whose body is searched. |
| `file` | Which source of a multi-class library to mutate. Defaults to `Helper.java`. |
| `find` | The single line to replace (whitespace-trimmed match). Must match exactly once inside `in`, otherwise the mutant is `INVALID`. |
| `replace` | Its replacement; empty deletes the line. |
| `expect` | `killed` (default) or `survives`. |
| `note` | Why a declared survivor survives. Required with `expect: survives`. |

The operators (`MutationOperator.java`, classical Mothra set): AOR, AOI, ROR,
COR, COI, UOI, ABS, CRP, CTC, LBR, RVR, SDL, STR, ARP, MCR, SEI, VRO.

Classification:

- **KILLED**: any generated assertion (SEQUENCE / PRE / POST) fails, the
  library throws, or a test hits its 2 s timeout.
- **SURVIVED**: the whole generated suite still passes against the mutant.
- **INVALID**: the mutant could not be seeded, the mutated library doesn't
  compile, or the library has no passing baseline suite. Invalid mutants are
  excluded from the score.
