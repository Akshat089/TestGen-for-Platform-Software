package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.Snapshot;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.spf.InputVector;

import java.util.List;

/**
 * Step 6: {@link TestPlan} + one {@link InputVector} per feasible path -&gt;
 * {@code GeneratedATCsTest.java}, one {@code @Test} method per path named
 * {@code path1}, {@code path2}, ... (rule 11).
 *
 * <p>Per call block, prints a precondition check, the snapshot locals, the
 * call with LITERAL arguments (storing the result if any), and one assertion
 * per postcondition clause — using the exact same {@link Clause#getJavaExpr()}
 * text {@link SymbolicDriverPrinter} used, so the two can never disagree about
 * what is being tested (rule 5): only HOW {@code c<i>_<param>} gets its value
 * differs (a literal local here, {@code Debug.makeSymbolic*} there). A
 * precondition failure and a postcondition failure are reported with
 * different message prefixes (rule 11) so a test failure immediately says
 * which kind it is. Imports nothing from {@code gov.nasa.jpf} (rule 11): by
 * the time this class exists every value is a plain Java literal.
 */
public final class JUnitPrinter {

    private JUnitPrinter() {}

    public static String print(TestPlan plan, List<InputVector> vectors) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(TestPlan.GENERATED_PACKAGE).append(";\n\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import static org.junit.Assert.assertTrue;\n\n");
        sb.append("/**\n * Generated from ").append(plan.getSpecFile()).append(" + ")
          .append(plan.getTestsFile()).append(".\n")
          .append(" * One @Test method per path SPF found through the sequence;\n")
          .append(" * every assertion traces to one postcondition clause of the contract.\n")
          .append(" * No SPF on this class's classpath (rule 11).\n */\n");
        sb.append("public class ").append(TestPlan.JUNIT_CLASS).append(" {\n");

        for (int i = 0; i < vectors.size(); i++) {
            printPathMethod(sb, plan, vectors.get(i), i + 1);
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static void printPathMethod(StringBuilder sb, TestPlan plan, InputVector vector, int n) {
        sb.append("\n    // SPF path #").append(vector.getPathId()).append('\n');
        sb.append("    @Test\n");
        sb.append("    public void path").append(n).append("() {\n");
        if (plan.isHasState()) {
            sb.append("        Helper.reset();\n");
        }
        for (CallBlock block : plan.getCallBlocks()) {
            sb.append("\n        // call ").append(block.getIndex()).append(": ")
              .append(block.getOperationName()).append('\n');
            printParamLiterals(sb, block, vector);
            printPreconditionChecks(sb, block);
            printSnapshots(sb, block);
            printCall(sb, block);
            printPostconditionAssertions(sb, block);
        }
        sb.append("    }\n");
    }

    private static void printParamLiterals(StringBuilder sb, CallBlock block, InputVector vector) {
        for (ParamSlot p : block.getParams()) {
            sb.append("        ").append(p.getType()).append(' ').append(p.getSlotName())
              .append(" = ").append(vector.literalFor(p.getSlotName())).append(";\n");
        }
    }

    private static void printPreconditionChecks(StringBuilder sb, CallBlock block) {
        for (Clause c : block.getPreconditions()) {
            sb.append("        assertTrue(")
              .append(javaString("PRE " + block.getOperationName() + ": " + c.getOriginalText()))
              .append(", ").append(c.getJavaExpr()).append(");\n");
        }
    }

    private static void printSnapshots(StringBuilder sb, CallBlock block) {
        for (Snapshot s : block.getSnapshots()) {
            sb.append("        ").append(s.getType()).append(' ').append(s.getLocalName())
              .append(" = Helper.").append(s.getStateVarName()).append(";\n");
        }
    }

    private static void printCall(StringBuilder sb, CallBlock block) {
        sb.append("        ");
        if (block.isStoresResult()) {
            sb.append(block.getReturnType()).append(' ').append(block.getResultVarName()).append(" = ");
        }
        sb.append("Helper.").append(block.getOperationName()).append('(')
          .append(block.argumentList()).append(");\n");
    }

    private static void printPostconditionAssertions(StringBuilder sb, CallBlock block) {
        for (Clause c : block.getPostconditions()) {
            sb.append("        assertTrue(")
              .append(javaString("POST " + block.getOperationName() + ": " + c.getOriginalText()))
              .append(", ").append(c.getJavaExpr()).append(");\n");
        }
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
