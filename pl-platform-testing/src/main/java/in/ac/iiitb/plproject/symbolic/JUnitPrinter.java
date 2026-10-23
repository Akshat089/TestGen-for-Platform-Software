package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * The concrete test case genCTC returned -&gt; {@code GeneratedATCsTest.java},
 * ONE {@code @Test} method per sequence, printing exactly the statements of
 * the final t in order and nothing else: the reset, each assignment, each
 * real call, each {@code assume} as a {@code "PRE <op>: <clause>"} assertion
 * and each {@code assert} as a {@code "POST <op>: <clause>"} assertion.
 * Imports nothing from {@code gov.nasa.jpf}.
 */
public final class JUnitPrinter {

    private JUnitPrinter() {}

    /** Per-test time limit, so a looping library fails the test instead of hanging it. */
    public static final int TEST_TIMEOUT_MILLIS = 2000;

    /** One sequence and the concrete test case genCTC returned for it. */
    public static final class Sequence {
        public final TestPlan plan;
        public final List<Stmt> concrete;

        public Sequence(TestPlan plan, List<Stmt> concrete) {
            this.plan = plan;
            this.concrete = concrete;
        }
    }

    public static String print(TestPlan plan, List<Stmt> concrete) {
        List<Sequence> one = new ArrayList<Sequence>();
        one.add(new Sequence(plan, concrete));
        return print(TestPlan.JUNIT_CLASS, one);
    }

    public static String print(String className, List<Sequence> sequences) {
        TestPlan first = sequences.get(0).plan;
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(TestPlan.GENERATED_PACKAGE).append(";\n\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import static org.junit.Assert.assertTrue;\n\n");
        sb.append("/**\n * Generated from ").append(first.getSpecFile()).append(" + ")
          .append(first.getTestsFile()).append(".\n")
          .append(" * One @Test method per sequence: the concrete test case genCTC returned,\n")
          .append(" * statement for statement; every assertion is one clause of the contract.\n */\n");
        sb.append("public class ").append(className).append(" {\n");

        java.util.Set<String> used = new java.util.HashSet<String>();
        for (int i = 0; i < sequences.size(); i++) {
            printSequenceMethod(sb, sequences.get(i), methodName(sequences.get(i).plan, i + 1, used));
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static String methodName(TestPlan plan, int n, java.util.Set<String> used) {
        String base = plan.getLibraryKey() != null
                ? plan.getLibraryKey().replaceAll("[^A-Za-z0-9_]", "_") : "sequence" + n;
        if (!Character.isJavaIdentifierStart(base.charAt(0))) base = "s" + base;
        base = Character.toLowerCase(base.charAt(0)) + base.substring(1);
        String name = base;
        for (int k = 2; !used.add(name); k++) name = base + "_" + k;
        return name;
    }

    private static void printSequenceMethod(StringBuilder sb, Sequence seq, String name) {
        StringBuilder calls = new StringBuilder();
        for (in.ac.iiitb.plproject.ir.CallBlock b : seq.plan.getCallBlocks()) {
            if (calls.length() > 0) calls.append(" -> ");
            calls.append(b.getOperationName());
        }
        sb.append("\n    // sequence: ").append(calls).append('\n');
        sb.append("    @Test(timeout = ").append(TEST_TIMEOUT_MILLIS).append(")\n");
        sb.append("    public void ").append(name).append("() {\n");
        for (Stmt s : seq.concrete) {
            if (s instanceof Stmt.Reset) {
                sb.append("        Helper.reset();\n");
            } else if (s instanceof Stmt.Comment) {
                sb.append("\n        ").append(s).append('\n');
            } else if (s instanceof Stmt.Assign) {
                sb.append("        ").append(assignment((Stmt.Assign) s)).append('\n');
            } else if (s instanceof Stmt.Assume) {
                Stmt.Assume a = (Stmt.Assume) s;
                sb.append("        assertTrue(")
                  .append(javaString("PRE " + a.block.getOperationName() + ": " + a.clause.getOriginalText()))
                  .append(", ").append(a.clause.getJavaExpr()).append(");\n");
            } else if (s instanceof Stmt.Assert) {
                Stmt.Assert a = (Stmt.Assert) s;
                sb.append("        assertTrue(")
                  .append(javaString("POST " + a.block.getOperationName() + ": " + a.clause.getOriginalText()))
                  .append(", ").append(a.clause.getJavaExpr()).append(");\n");
            } else {
                throw new IllegalStateException("not a concrete test case: it still has " + s);
            }
        }
        sb.append("    }\n");
    }

    /** {@code T x = e;}, or {@code Helper.op(args);} for a call with no stored result. */
    static String assignment(Stmt.Assign a) {
        String rhs = java(a.expr);
        if (a.var == null) return rhs + ";";
        return a.type + " " + a.var + " = " + rhs + ";";
    }

    /** An ATC expression as Java: an API call becomes a real call to {@code Helper}. */
    static String java(Expr e) {
        if (e instanceof Expr.Call) {
            Expr.Call c = (Expr.Call) e;
            StringBuilder sb = new StringBuilder(c.api ? "Helper." : "").append(c.function).append('(');
            for (int i = 0; i < c.args.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(java(c.args.get(i)));
            }
            return sb.append(')').toString();
        }
        return e.toString();
    }

    private static String javaString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '"') sb.append('\\').append(c);
            else if (c == '\n') sb.append("\\n");
            else sb.append(c);
        }
        return sb.append('"').toString();
    }
}
