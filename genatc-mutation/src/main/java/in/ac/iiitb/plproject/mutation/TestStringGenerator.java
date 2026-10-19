package in.ac.iiitb.plproject.mutation;

import in.ac.iiitb.plproject.check.InputChecker;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Builds a family of test strings — ORDER ONLY, no {@code inputs} block — for
 * one library.
 *
 * <h3>Why</h3>
 *
 * Every library in this project ships exactly one test string, hand-written, and
 * a great many mutants survive for a reason that has nothing to do with the
 * specification: <em>the test string never asks</em>. {@code specs/Stack.tests}
 * pushes twice and then pops, so by the time {@code peek} runs there is one
 * element left and "the top" and "the bottom" are the same element; a library
 * that peeked at the wrong end passes. Reordering the sequence is what kills
 * that fault, with no change to the spec at all — this generator produces a few
 * hundred such reorderings per library instead of one, turning that claim into a
 * measurement.
 *
 * <p><b>Adapted for the no-propagation design</b> (rule 1/4): a test string is
 * now just an ordered list of operation names — SPF chooses every argument, not
 * this generator — so building one is just drawing/editing a sequence and
 * running it past {@link InputChecker#checkSequence} (does every call name an
 * operation the spec actually declares?). The literal-pool/CLIENT_INPUT-binding
 * machinery the old propagation-era version needed is gone with it.
 *
 * <p>Generation is seeded, so the same library always produces the same family.
 */
public class TestStringGenerator {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 7;

    /**
     * How often a sequence is built by editing the checked-in one rather than
     * drawn from scratch — see the class comment on why most of the family is
     * made this way.
     */
    private static final double SEED_DERIVED_SHARE = 0.7;

    private final JmlSpecAst spec;
    private final List<String> functions;
    private final List<String> seedSequence;
    private final Random random;

    /**
     * @param spec      the library's specification
     * @param seedTests the checked-in test string, mined only for its call order
     * @param seed      the RNG seed, so a family is reproducible
     */
    public TestStringGenerator(JmlSpecAst spec, TestStringAst seedTests, long seed) {
        this.spec = spec;
        this.random = new Random(seed);
        this.functions = new ArrayList<String>();
        for (JmlFunctionSpec function : spec.getSpecs()) {
            this.functions.add(function.getName());
        }
        this.seedSequence = seedTests == null
                ? new ArrayList<String>()
                : new ArrayList<String>(seedTests.getCalls());
    }

    /**
     * Generates up to {@code count} distinct, valid (every call names a real
     * operation) test strings.
     */
    public List<String> generate(String namePrefix, int count) {
        List<String> generated = new ArrayList<String>();
        Set<String> seen = new LinkedHashSet<String>();
        int attempts = 0;
        int maxAttempts = count * 60;

        while (generated.size() < count && attempts++ < maxAttempts) {
            List<String> sequence = drawSequence();
            String signature = String.join(",", sequence);
            if (!seen.add(signature)) continue;

            String text = "test " + namePrefix + (generated.size() + 1) + " {\n"
                        + "    sequence: " + String.join(" -> ", sequence) + ";\n"
                        + "}\n";

            TestStringAst parsed;
            try {
                parsed = TestStringParser.parse(text);
            } catch (RuntimeException notATestString) {
                continue;
            }
            if (!InputChecker.checkSequence(spec, parsed, null).isEmpty()) continue;

            generated.add(text);
        }
        return generated;
    }

    private List<String> drawSequence() {
        if (!seedSequence.isEmpty() && random.nextDouble() < SEED_DERIVED_SHARE) {
            return editSeedSequence();
        }
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
        List<String> sequence = new ArrayList<String>();
        for (int index = 0; index < length; index++) {
            sequence.add(functions.get(random.nextInt(functions.size())));
        }
        return sequence;
    }

    /**
     * The checked-in sequence with one to three small edits: swap, repeat, drop,
     * or add a call — the four ways a reviewer describes a different test.
     */
    private List<String> editSeedSequence() {
        List<String> sequence = new ArrayList<String>(seedSequence);
        int edits = 1 + random.nextInt(3);

        for (int edit = 0; edit < edits && !sequence.isEmpty(); edit++) {
            int at = random.nextInt(sequence.size());
            switch (random.nextInt(4)) {
                case 0:
                    if (sequence.size() > 1) {
                        int other = random.nextInt(sequence.size());
                        String held = sequence.get(at);
                        sequence.set(at, sequence.get(other));
                        sequence.set(other, held);
                    }
                    break;
                case 1:
                    if (sequence.size() < MAX_LENGTH) sequence.add(at, sequence.get(at));
                    break;
                case 2:
                    if (sequence.size() > MIN_LENGTH) sequence.remove(at);
                    break;
                default:
                    if (sequence.size() < MAX_LENGTH) {
                        sequence.add(at, functions.get(random.nextInt(functions.size())));
                    }
                    break;
            }
        }
        return sequence;
    }

    /** The functions this library declares, in spec order. */
    public List<String> getFunctions() {
        return Collections.unmodifiableList(functions);
    }
}
