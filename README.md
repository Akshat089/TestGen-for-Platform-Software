# TestGen: specification-based, black-box test generation for Java libraries

TestGen generates JUnit tests for a Java library from two things you write:

- a **contract** (`.spec`): a JML-style precondition and postcondition for each operation;
- a **test string** (`.tests`): the order of calls to test, for example `push -> push -> pop -> peek`, with no values.

TestGen chooses the input values itself, using Symbolic PathFinder (SPF) to solve the preconditions. It runs the real library only with concrete values, so it never needs the library's source code. If the calls cannot happen in that order (for example `push -> pop -> pop`), it rejects the test string and names the call that could not run. Otherwise it writes one JUnit test in which every precondition is a `PRE` check and every postcondition clause is a `POST` check.

To measure how good the tests are, the project includes mutation testing: bugs are planted in copies of each library, and we check how many the generated tests catch.

Authors: Akshat Betala, Areen Vaghasiya, Siddharth Palod (IIIT Bangalore).

## How it works, in short

1. **genATC** reads the contract and the test string and generates the **abstract test case** (ATC). Each call has an input command per argument, an `assume` (precondition), the call and `assert`s (postcondition).
2. **genCTC** turns it into the **concrete test case** (CTC), round by round:
   - fill in the values found so far;
   - run the part of the test whose calls have concrete arguments on SPF, with those calls running on the real library;
   - solve the collected preconditions for the next inputs.
   
   If they cannot be solved, the test string is rejected.
3. The concrete test case is written as a plain JUnit test, which does not need SPF.

See [ARCHITECTURE.md](ARCHITECTURE.md) for the full pipeline and the file behind each step, and [spf.txt](spf.txt) for the SPF problems solved along the way.

## Example

`specs/Stack.spec` (part):

```
state { List<String> S; int size; String top; }

spec push {
    signature: void push(String elem);
    requires:  elem != null;
    ensures:   size == \old(size) + 1 && top == elem;
}
spec pop {
    signature: String pop();
    requires:  size > 0;
    ensures:   \result == poppedElem && size == \old(size) - 1;
}
```

`specs/Stack.tests`:

```
test StackDryRun {
    sequence: push -> push -> pop -> peek;
}
```

Generated test (part):

```java
@Test(timeout = 2000)
public void stackDryRun() {
    Helper.reset();
    int old0_size = Helper.size;
    String c0_elem = "s7";
    assertTrue("PRE push: elem != null", c0_elem != null);
    Helper.push(c0_elem);
    assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
    assertTrue("POST push: top == elem", Helper.top == c0_elem);
    ...
}
```

The test string `push -> pop -> pop` gives no test, only:

```
INCORRECT SEQUENCE: call 2 (pop): size > 0
```

## Requirements

- JDK 11 (a full JDK, since the tools compile generated code)
- Maven 3
- git, plus Gradle and Ant through the setup script, to build JPF and SPF once

## Setup (once)

```bash
./scripts/setup-spf.sh          # builds jpf-core and jpf-symbc at pinned commits into ~/.genatc-spf
mvn -o install -DskipTests      # builds TestGen and the mutation module
```

Use `./scripts/setup-spf.sh --spf-home <dir>` or set `GENATC_SPF_HOME` to keep SPF somewhere else.

## Running

All commands are from the repository root unless noted.

| Task | Command |
| --- | --- |
| Generate and check all 14 libraries against the golden copies | `./verify-generated.sh` |
| Accept the current output as the new golden copy | `./verify-generated.sh --update-golden` |
| Generate the test for one library | `cd pl-platform-testing && mvn -o -q exec:java -Dexec.args="stack"` |
| Same, from the jars | `cd pl-platform-testing && java -cp "target/pl-platform-testing-1.0.0.jar:../genatc-mutation/target/lib/*" in.ac.iiitb.plproject.atc.GenatcMain stack` |
| Unit tests | `cd pl-platform-testing && mvn -o test` |
| Hand-written mutants (137) | `./genatc-mutation/run-mutations.sh [stack queue ...]` |
| Generated mutants (1434) and 260 test strings per library (slow, over an hour for all) | `./genatc-mutation/run-operator-suites.sh [stack queue ...]` |

The "from the jars" command needs the dependency folder once: `mvn -pl genatc-mutation dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib`.

The output for a library is in `pl-platform-testing/outputs/<key>/`:
- `ATC.txt`: the abstract test case, every genCTC round and the concrete test case;
- `in/ac/iiitb/plproject/atc/generated/GeneratedATCsTest.java`: the JUnit test.

To try your own test string, edit (or copy) `specs/<Lib>.tests` and run the library. Restore the checked-in file afterwards, because the golden copies and mutation results are based on it.

SPF settings can be changed with `-Dgenatc.spf.timeoutSeconds` (default 60), `-Dgenatc.spf.depthLimit` (300), `-Dgenatc.spf.minInt` / `maxInt` (−1000 / 1000) and `-Dgenatc.spf.solver` (z3).

## Results so far

On the 14 libraries, with one checked-in test string each:
- all 14 tests are generated and pass on the correct library;
- **hand-written bugs:** 103 of 137 caught (75.2%);
- **generated bugs:** 55.6% caught by the checked-in test alone, and 62.9% when the 260 extra test strings per library are added;
- **extra test strings:** 1680 of the 3640 were correctly rejected as impossible orders.

Details are in [MUTATION_TESTING.md](MUTATION_TESTING.md) and [operator.md](operator.md).

## Limitations

- **Small contract language.** There are no quantifiers and no exact formulas (such as power or factorial), so some bugs cannot be described and therefore cannot be caught.
- **Strings are integer ids.** String operations (`length`, `charAt`, …) on an input inside a precondition are not supported.
- **Simple input values.** The solver picks the simplest values that satisfy the preconditions, so some bugs that depend on the data are missed.
- **Shape of the libraries.** A library must keep its state in static fields named as in the contract, and provide `reset()`.

## Repository layout

| Path | Contents |
| --- | --- |
| `specs/` | Contracts and test strings |
| `libraries/` | The 14 libraries under test |
| `pl-platform-testing/` | TestGen (Maven module), its unit tests and golden copies |
| `genatc-mutation/` | Mutation testing: hand-written mutants, generated suites and run scripts |
| `scripts/setup-spf.sh` | One-time JPF/SPF build |
| `verify-generated.sh` | End-to-end check |