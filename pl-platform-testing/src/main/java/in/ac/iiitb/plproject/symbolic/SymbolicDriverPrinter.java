package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 3: {@link TestPlan} -&gt; the symbolic driver SPF executes, built from
 * the CONTRACT ONLY. It never calls {@code Helper} and imports nothing from
 * the library; SPF's classpath does not contain the library at all.
 *
 * <p>The driver keeps its own model copy of every state variable, set to its
 * starting value, and for each call in order: (a) declares a symbolic value
 * per parameter, (b) assumes the precondition over the model, (c) declares a
 * fresh symbolic result and a fresh value for every state variable the
 * postcondition names, (d) assumes the postcondition, with {@code \old(v)}
 * read from the previous model value. A state variable the postcondition does
 * not name keeps its value (frame rule). Each assumption is printed as
 * {@code Verify.ignoreIf(!(...))}: jpf-symbc's {@code Debug.assume} only
 * prunes when its argument still carries a symbolic attribute, and a Java
 * comparison has already been branched into a concrete boolean by the time it
 * is passed (see the {@code assume} peer in
 * {@code JPF_gov_nasa_jpf_symbc_Debug}), so it would silently prune nothing.
 *
 * <p>Every assumption sits on its own line so that the line SPF last pruned
 * at names the clause that cannot hold ({@link LineMap}).
 */
public final class SymbolicDriverPrinter {

    private SymbolicDriverPrinter() {}

    /** Which clause each printed assumption line belongs to. */
    public static final class LineMap {
        public static final class Entry {
            public final int line;
            public final int callIndex;
            /** "requires", "ensures" or "declare". */
            public final String kind;
            public final String clauseText;

            Entry(int line, int callIndex, String kind, String clauseText) {
                this.line = line;
                this.callIndex = callIndex;
                this.kind = kind;
                this.clauseText = clauseText;
            }
        }

        private final List<Entry> entries = new ArrayList<Entry>();

        public List<Entry> getEntries() { return entries; }

        /** The entry for {@code line}, or null when it is not an assumption line of the driver. */
        public Entry at(int line) {
            for (Entry e : entries) if (e.line == line) return e;
            return null;
        }
    }

    /** The driver source together with its line map. */
    public static final class Printed {
        public final String source;
        public final LineMap lines;

        Printed(String source, LineMap lines) {
            this.source = source;
            this.lines = lines;
        }
    }

    public static String print(TestPlan plan) {
        return printWithLines(plan).source;
    }

    public static Printed printWithLines(TestPlan plan) {
        Out out = new Out();
        out.line("package " + TestPlan.GENERATED_PACKAGE + ";");
        out.line("");
        out.line("import gov.nasa.jpf.symbc.Debug;");
        out.line("import gov.nasa.jpf.vm.Verify;");
        out.line("");
        out.line("/**");
        out.line(" * Symbolic driver for SPF, built from " + plan.getSpecFile() + " only.");
        out.line(" * It models the library state itself and never calls the library.");
        out.line(" */");
        out.line("public class " + TestPlan.SYMBOLIC_CLASS + " {");
        out.line("");
        out.line("    public static void main(String[] args) {");
        if (!plan.getStartDecls().isEmpty()) {
            out.line("        // starting state");
            for (String d : plan.getStartDecls()) out.line("        " + d);
        }
        for (CallBlock block : plan.getCallBlocks()) {
            out.line("");
            out.line("        // call " + block.getIndex() + ": " + block.getOperationName());
            for (String d : block.getParamDecls()) {
                out.mark(block.getIndex(), "declare", d);
                out.line("        " + d);
            }
            for (Clause c : block.getPreconditions()) printAssumption(out, block, c, "requires");
            for (String d : block.getFreshDecls()) {
                out.mark(block.getIndex(), "declare", d);
                out.line("        " + d);
            }
            for (Clause c : block.getPostconditions()) printAssumption(out, block, c, "ensures");
        }
        out.line("    }");
        out.line("");
        out.line("    /** A symbolic 0/1 flag. */");
        out.line("    private static int flag(String name) {");
        out.line("        int v = Debug.makeSymbolicInteger(name);");
        out.line("        Verify.ignoreIf(v < 0 || v > 1);");
        out.line("        return v;");
        out.line("    }");
        out.line("");
        out.line("    /** A symbolic index into the " + TypeSupport.POOL_SIZE + "-value pool. */");
        out.line("    private static int index(String name) {");
        out.line("        int v = Debug.makeSymbolicInteger(name);");
        out.line("        Verify.ignoreIf(v < 0 || v > " + (TypeSupport.POOL_SIZE - 1) + ");");
        out.line("        return v;");
        out.line("    }");
        out.line("}");
        return new Printed(out.sb.toString(), out.map);
    }

    private static void printAssumption(Out out, CallBlock block, Clause c, String kind) {
        if (c.isTautological()) {
            out.line("        // " + kind + ": " + c.getOriginalText() + "  (only names the result)");
        } else if (c.isCheckOnly()) {
            out.line("        // CHECK-ONLY " + kind + ": " + c.getOriginalText() + "  (" + c.getCheckOnlyReason() + ")");
        } else {
            out.line("        // " + kind + ": " + c.getOriginalText());
            out.mark(block.getIndex(), kind, c.getOriginalText());
            out.line("        Verify.ignoreIf(!(" + c.getModelExpr() + "));");
        }
    }

    private static final class Out {
        final StringBuilder sb = new StringBuilder();
        final LineMap map = new LineMap();
        int lineNo = 1;

        void line(String text) {
            sb.append(text).append('\n');
            lineNo++;
        }

        /** Records that the NEXT printed line belongs to this call and clause. */
        void mark(int callIndex, String kind, String clauseText) {
            map.entries.add(new LineMap.Entry(lineNo, callIndex, kind, clauseText));
        }
    }
}
