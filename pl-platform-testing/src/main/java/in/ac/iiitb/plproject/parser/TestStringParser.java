package in.ac.iiitb.plproject.parser;

import in.ac.iiitb.plproject.parser.ast.TestStringAst;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a {@code .tests} file into a {@link TestStringAst}: an ordered list
 * of operation names, nothing else (rule 4).
 *
 * <h3>Format</h3>
 *
 * <pre>
 * test StackDryRun {
 *     sequence: push -&gt; push -&gt; pop -&gt; peek;
 * }
 * </pre>
 *
 * <p>{@code sequence} is the test string itself: the blocks, in order. Blocks
 * may be separated by {@code ->}, {@code ,} or whitespace.
 *
 * <p>A bare sequence with no wrapper is also accepted, for a quick one-liner:
 * <pre>push -&gt; push -&gt; pop -&gt; peek</pre>
 *
 * <p>An older-style file may still carry an {@code inputs { ... }} block that
 * pinned concrete values to every parameter. That block is parsed just enough
 * to be skipped over without failing the file, and {@link #parse} prints one
 * warning naming its line — it is never used to build the {@link TestStringAst}.
 */
public class TestStringParser {

    private static final Pattern TEST_BLOCK = Pattern.compile(
            "test\\s+(\\w+)\\s*\\{(.*)\\}", Pattern.DOTALL);

    private static final Pattern SEQUENCE_CLAUSE = Pattern.compile(
            "sequence\\s*:\\s*([^;]+);", Pattern.DOTALL);

    private static final Pattern INPUTS_BLOCK = Pattern.compile(
            "inputs\\s*\\{([^}]*)\\}", Pattern.DOTALL);

    /** Raised when the file cannot be read as a test string at all. */
    public static class TestStringSyntaxException extends RuntimeException {
        public TestStringSyntaxException(String message) {
            super(message);
        }
    }

    public static TestStringAst parse(String text) {
        return parse(text, null);
    }

    /** Same parse, but every diagnostic names {@code sourceFile}. */
    public static TestStringAst parse(String text, String sourceFile) {
        String stripped = stripComments(text);
        String label = sourceFile != null ? sourceFile : "<test string>";

        Matcher block = TEST_BLOCK.matcher(stripped);
        if (!block.find()) {
            // Bare form: the whole file is just the sequence.
            List<String> calls = parseSequence(stripped);
            if (calls.isEmpty()) {
                throw new TestStringSyntaxException(label
                      + ": no test string found: expected `test <Name> { sequence: a -> b; }`"
                      + " or a bare sequence such as `push -> push -> pop`");
            }
            TestStringAst ast = new TestStringAst(null, calls);
            ast.setSourceFile(sourceFile);
            warnIfInputsBlockPresent(stripped, label, ast);
            return ast;
        }

        String name = block.group(1);
        String body = block.group(2);

        Matcher sequence = SEQUENCE_CLAUSE.matcher(body);
        if (!sequence.find()) {
            throw new TestStringSyntaxException(label + ": test " + name
                    + " has no `sequence:` clause");
        }
        List<String> calls = parseSequence(sequence.group(1));
        if (calls.isEmpty()) {
            throw new TestStringSyntaxException(label + ": test " + name
                    + " has an empty sequence");
        }

        TestStringAst ast = new TestStringAst(name, calls);
        ast.setSourceFile(sourceFile);
        ast.setSequenceLineNumber(SpecToAstConverter.lineOf(stripped, block.start() + sequence.start()));
        warnIfInputsBlockPresent(body, label, ast);
        return ast;
    }

    private static void warnIfInputsBlockPresent(String body, String label, TestStringAst ast) {
        Matcher inputsBlock = INPUTS_BLOCK.matcher(body);
        if (!inputsBlock.find()) return;

        int line = SpecToAstConverter.lineOf(body, inputsBlock.start());
        ast.setIgnoredInputsLineNumber(line);
        System.out.println("[WARN] " + label + ":" + line
                + ": `inputs { ... }` block is ignored — sequences carry no values"
                + " in this design; every argument is chosen by SPF.");
    }

    private static List<String> parseSequence(String text) {
        List<String> calls = new ArrayList<String>();
        for (String token : text.split("->|,|\\s+")) {
            String call = token.trim();
            if (!call.isEmpty()) calls.add(call);
        }
        return calls;
    }

    /** Drops block and line comments, matching the .spec file conventions. */
    static String stripComments(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", " ")
                   .replaceAll("(?m)//[^\n]*", " ");
    }
}
