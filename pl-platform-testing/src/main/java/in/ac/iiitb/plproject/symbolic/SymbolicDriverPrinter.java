package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Prints one genCTC round's t' as the Java program SPF runs: the ready
 * prefix of t' and nothing after it. Concrete inputs are literals, the
 * remaining inputs are symbolic integers ({@code Debug.makeSymbolic*}; a
 * String is an integer id plus a null flag, see {@link IntegerModel}), each
 * assume is {@code Verify.ignoreIf(!(c))} with {@code c} rewritten into
 * integer reasoning over the concrete state, and each ready call is a real
 * call to {@code Helper}. The program ends at the first not-ready call.
 *
 * <p>Assumes are {@code Verify.ignoreIf}, not {@code Debug.assume}: in
 * jpf-symbc 3bedf0b {@code Debug.assume} only prunes when its argument still
 * carries a symbolic attribute, and a Java comparison has already been
 * branched into a concrete boolean by then. An assume whose evaluation throws
 * (say, a null map entry) counts as false.
 *
 * <p>Every assume and every call sits on its own line, so the line SPF last
 * pruned at, or threw at, names the statement ({@link Printed#at}).
 */
public final class SymbolicDriverPrinter {

    private SymbolicDriverPrinter() {}

    public static final class Printed {
        public final String source;
        private final List<Integer> lines;
        private final List<Stmt> statements;

        Printed(String source, List<Integer> lines, List<Stmt> statements) {
            this.source = source;
            this.lines = lines;
            this.statements = statements;
        }

        /** The statement printed on {@code line} of the driver, or null. */
        public Stmt at(int line) {
            for (int i = 0; i < lines.size(); i++) if (lines.get(i) == line) return statements.get(i);
            return null;
        }
    }

    public static Printed print(TestPlan plan, TestGen.Prefix prefix, int round) {
        Out out = new Out();
        out.line("package " + TestPlan.GENERATED_PACKAGE + ";");
        out.line("");
        out.line("import gov.nasa.jpf.symbc.Debug;");
        out.line("import gov.nasa.jpf.vm.Verify;");
        out.line("");
        out.line("/**");
        out.line(" * genCTC round " + round + " for " + plan.getSpecFile() + " + " + plan.getTestsFile() + ":");
        out.line(" * t' up to its first not-ready call"
                + (prefix.firstNotReady == null ? " (every call is ready)." : ", " + prefix.firstNotReady + "."));
        out.line(" * Ready calls run on the real library with concrete arguments.");
        out.line(" */");
        out.line("public class " + TestPlan.SYMBOLIC_CLASS + " {");
        out.line("");
        out.line("    public static void main(String[] args) {");
        out.line("        boolean ok;");
        Map<String, String> symbolic = new LinkedHashMap<String, String>();
        for (Stmt.Input in : prefix.inputs) symbolic.put(in.var, in.type.trim());
        for (Stmt s : prefix.statements) printStatement(out, s, symbolic, plan.getStateVars());
        if (prefix.firstNotReady != null) {
            out.line("        // first not-ready statement: " + prefix.firstNotReady + " — the program ends here");
        }
        out.line("    }");
        out.line("");
        for (String line : IntegerModel.RUNTIME.split("\n", -1)) out.line(line);
        out.line("}");
        return new Printed(out.sb.toString(), out.lines, out.statements);
    }

    private static void printStatement(Out out, Stmt s, Map<String, String> symbolic,
                                       Map<String, String> stateTypes) {
        if (s instanceof Stmt.Reset) {
            out.line("        // " + s.toString().substring(3));
            out.mark(s);
            out.line("        Helper.reset();");
        } else if (s instanceof Stmt.Comment) {
            out.line("");
            out.line("        " + s);
        } else if (s instanceof Stmt.Input) {
            out.mark(s);
            out.line("        " + symbolicInput((Stmt.Input) s));
        } else if (s instanceof Stmt.Assign) {
            Stmt.Assign a = (Stmt.Assign) s;
            out.mark(s);
            out.line("        " + JUnitPrinter.assignment(a));
        } else if (s instanceof Stmt.Assume) {
            Stmt.Assume a = (Stmt.Assume) s;
            out.line("        // assume: " + a.clause.getOriginalText());
            out.mark(s);
            out.line("        try { " + IntegerModel.assume(a.clause.getJavaExpr(), symbolic, stateTypes)
                    + " } catch (RuntimeException e) { ok = false; } Verify.ignoreIf(!ok);");
        } else if (s instanceof Stmt.Assert) {
            out.line("        // assert (not a constraint; checked in the final test): "
                    + ((Stmt.Assert) s).clause.getOriginalText());
        }
    }

    /**
     * The declaration of an input that is still symbolic: a symbolic integer,
     * or for a String its id and null flag. No String object exists for it.
     */
    static String symbolicInput(Stmt.Input in) {
        String type = in.type.trim();
        String v = in.var;
        if (IntegerModel.isString(type)) {
            String id = v + IntegerModel.ID, nonNull = v + IntegerModel.NON_NULL;
            return "int " + id + " = Debug.makeSymbolicInteger(\"" + id + "\"); boolean " + nonNull
                    + " = Debug.makeSymbolicBoolean(\"" + nonNull + "\");";
        }
        String prim = TypeSupport.unboxed(type);
        String sym = TypeSupport.symbolicFactory(prim) + "(\"" + v + "\")";
        if (TypeSupport.isBoxed(type)) return type + " " + v + " = new " + type + "(" + sym + ");";
        return type + " " + v + " = " + sym + ";";
    }

    private static final class Out {
        final StringBuilder sb = new StringBuilder();
        final List<Integer> lines = new ArrayList<Integer>();
        final List<Stmt> statements = new ArrayList<Stmt>();
        int lineNo = 1;

        void line(String text) {
            sb.append(text).append('\n');
            lineNo++;
        }

        /** Records that the NEXT printed line is statement {@code s}. */
        void mark(Stmt s) {
            lines.add(lineNo);
            statements.add(s);
        }
    }
}
