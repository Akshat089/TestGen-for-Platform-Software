package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints one genCTC round's t' as the Java program SPF runs: the ready
 * prefix of t' and nothing after it. Concrete inputs are literals, the
 * remaining inputs are symbolic ({@code Debug.makeSymbolic*}, or a pick from
 * candidate values with a symbolic index for a pooled one), each assume is
 * {@code Verify.ignoreIf(!(c))} and each ready call is a real call to
 * {@code Helper}. The program ends at the first not-ready call.
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

    /** Name of the driver's static field the picks of the current path are appended to. */
    public static final String PICKS_FIELD = "picks";
    /** Separates a pick's name from its value, and one pick from the next. */
    public static final char PICK_NAME_END = '\u001f';
    public static final char PICK_END = '\u001e';

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
        out.line("    static String " + PICKS_FIELD + " = \"\";");
        out.line("");
        out.line("    public static void main(String[] args) {");
        out.line("        boolean ok;");
        for (Stmt s : prefix.statements) printStatement(out, s);
        if (prefix.firstNotReady != null) {
            out.line("        // first not-ready statement: " + prefix.firstNotReady + " — the program ends here");
        }
        out.line("    }");
        out.line("");
        out.line("    /**");
        out.line("     * One of the candidates — the pool, then the members of the given collections —");
        out.line("     * chosen by a symbolic index, the preferred one explored first.");
        out.line("     */");
        out.line("    private static Object pick(String name, int preferred, Class<?> type, Object[] pool,");
        out.line("                               Object... collections) {");
        out.line("        java.util.List<Object> candidates = new java.util.ArrayList<Object>(java.util.Arrays.asList(pool));");
        out.line("        for (Object c : collections) {");
        out.line("            Iterable<?> members = c instanceof java.util.Map ? ((java.util.Map<?, ?>) c).keySet()");
        out.line("                    : c instanceof Iterable ? (Iterable<?>) c : java.util.Collections.emptyList();");
        out.line("            for (Object m : members) if (type.isInstance(m) && !candidates.contains(m)) candidates.add(m);");
        out.line("        }");
        out.line("        int index = Debug.makeSymbolicInteger(name);");
        out.line("        int n = candidates.size();");
        out.line("        for (int k = 0; k < n; k++) {");
        out.line("            int j = (preferred + k) % n;");
        out.line("            if (index == j) {");
        out.line("                Object v = candidates.get(j);");
        out.line("                " + PICKS_FIELD + " = " + PICKS_FIELD + " + name + \"" + escape(PICK_NAME_END)
                + "\" + v + \"" + escape(PICK_END) + "\";");
        out.line("                return v;");
        out.line("            }");
        out.line("        }");
        out.line("        Verify.ignoreIf(true);");
        out.line("        return null;");
        out.line("    }");
        out.line("}");
        return new Printed(out.sb.toString(), out.lines, out.statements);
    }

    private static void printStatement(Out out, Stmt s) {
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
            out.line("        try { ok = (" + a.clause.getJavaExpr() + "); } catch (RuntimeException e) { ok = false; }"
                    + " Verify.ignoreIf(!ok);");
        } else if (s instanceof Stmt.Assert) {
            out.line("        // assert (not a constraint; checked in the final test): "
                    + ((Stmt.Assert) s).clause.getOriginalText());
        }
    }

    /** The declaration of an input that is still symbolic. */
    static String symbolicInput(Stmt.Input in) {
        ParamSlot slot = in.slot;
        String type = in.type.trim();
        String v = in.var;
        if (slot.isPooled()) {
            List<String> pool = new ArrayList<String>();
            for (String raw : slot.getPool()) pool.add(TypeSupport.literal(type, raw));
            StringBuilder sb = new StringBuilder();
            sb.append(type).append(' ').append(v).append(" = (").append(TypeSupport.boxed(type)).append(") pick(\"")
              .append(v).append("\", ").append(in.ordinal % pool.size()).append(", ")
              .append(TypeSupport.boxed(type)).append(".class, new Object[] {").append(String.join(", ", pool))
              .append('}');
            for (String collection : slot.getMembership()) sb.append(", ").append(TestGen.stateVar(collection));
            return sb.append(");").toString();
        }
        String prim = TypeSupport.unboxed(type);
        String sym = TypeSupport.symbolicFactory(prim) + "(\"" + v + "\")";
        if (TypeSupport.isBoxed(type)) return type + " " + v + " = new " + type + "(" + sym + ");";
        return type + " " + v + " = " + sym + ";";
    }

    private static String escape(char c) {
        return String.format("\\u%04x", (int) c);
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
