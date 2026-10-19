package in.ac.iiitb.plproject.parser.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A test string: an ORDERED LIST OF OPERATION NAMES, nothing else.
 *
 * <p>Earlier revisions let a {@code .tests} file also pin a concrete value to
 * every parameter via an {@code inputs { ... }} block, turning the sequence
 * into one hand-picked "singular case". The new design has no such thing —
 * every argument of every call is chosen by SPF — so a sequence carries no
 * values at all (rule 4). An {@code inputs { ... }} block in an existing file
 * is still parsed so old files don't fail outright, but it is ignored and
 * {@link in.ac.iiitb.plproject.parser.TestStringParser} prints exactly one
 * warning when it does.
 */
public class TestStringAst {

    private final List<String> calls;
    private final String name;
    private String sourceFile;
    /** 1-based line of the {@code sequence:} clause, or -1 if built in code. */
    private int sequenceLineNumber = -1;
    /** 1-based line of an ignored {@code inputs { ... }} block, or -1 if absent. */
    private int ignoredInputsLineNumber = -1;

    public TestStringAst(List<String> calls) {
        this(null, calls);
    }

    public TestStringAst(String name, List<String> calls) {
        this.calls = (calls != null) ? new ArrayList<String>(calls) : new ArrayList<String>();
        this.name = name;
    }

    public List<String> getCalls() {
        return calls;
    }

    /** Name from the test string file, or null when the sequence was built in code. */
    public String getName() {
        return name;
    }

    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    public String getSourceFile() { return sourceFile; }

    public void setSequenceLineNumber(int line) { this.sequenceLineNumber = line; }
    public int getSequenceLineNumber() { return sequenceLineNumber; }

    public void setIgnoredInputsLineNumber(int line) { this.ignoredInputsLineNumber = line; }
    /** Line of a now-ignored {@code inputs { ... }} block, or -1 if this file had none. */
    public int getIgnoredInputsLineNumber() { return ignoredInputsLineNumber; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name != null) sb.append(name).append(": ");
        sb.append(String.join(" -> ", calls));
        return sb.toString();
    }
}
