package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.Snapshot;
import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Step 3: {@link TestPlan} -&gt; the symbolic driver SPF actually executes.
 *
 * <p>Declares each parameter symbolic (or, when pooled, declares a symbolic
 * POOL INDEX and resolves it against a shared literal array — rule 9), prints
 * the precondition as {@code Debug.assume(...)} or, for an already-concrete
 * conjunct, {@code Verify.ignoreIf(!(...))} (confirmed by direct experiment:
 * {@code Debug.assume} on a non-symbolic boolean is a silent no-op — it does
 * NOT prune the path). It does not print postconditions at all (rule 5): SPF
 * only needs to know which paths are feasible, not what they're supposed to
 * prove — that is the JUnit printer's job, once the path is a concrete vector.
 */
public final class SymbolicDriverPrinter {

    private SymbolicDriverPrinter() {}

    public static String print(TestPlan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(TestPlan.GENERATED_PACKAGE).append(";\n\n");
        sb.append("import gov.nasa.jpf.symbc.Debug;\n");
        sb.append("import gov.nasa.jpf.vm.Verify;\n\n");
        sb.append("/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */\n");
        sb.append("public class ").append(TestPlan.SYMBOLIC_CLASS).append(" {\n\n");

        printPools(sb, plan);

        sb.append("    public static void main(String[] args) {\n");
        if (plan.isHasState()) {
            sb.append("        Helper.reset();\n");
        }
        for (CallBlock block : plan.getCallBlocks()) {
            sb.append("\n        // call ").append(block.getIndex()).append(": ")
              .append(block.getOperationName()).append("\n");
            printParamDeclarations(sb, block);
            printPrecondition(sb, block);
            printSnapshots(sb, block);
            printCall(sb, block);
        }
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private static void printPools(StringBuilder sb, TestPlan plan) {
        Map<String, List<String>> poolsByType = new LinkedHashMap<String, List<String>>();
        for (ParamSlot slot : plan.pooledParams()) {
            poolsByType.putIfAbsent(slot.getType().trim(), slot.getPool());
        }
        for (Map.Entry<String, List<String>> e : poolsByType.entrySet()) {
            String type = e.getKey();
            String elemType = TypeSupport.poolArrayElementType(type);
            sb.append("    private static final ").append(elemType).append("[] POOL_")
              .append(sanitize(type)).append(" = {");
            List<String> values = e.getValue();
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(TypeSupport.literal(type, values.get(i)));
            }
            sb.append("};\n");
        }
        if (!poolsByType.isEmpty()) sb.append("\n");
    }

    private static void printParamDeclarations(StringBuilder sb, CallBlock block) {
        for (ParamSlot p : block.getParams()) {
            if (p.isSymbolic()) {
                sb.append("        ").append(p.getType()).append(' ').append(p.getSlotName())
                  .append(" = ").append(TypeSupport.symbolicFactory(p.getType()))
                  .append("(\"").append(p.getSlotName()).append("\");\n");
            } else {
                String idx = p.getPoolIndexName();
                sb.append("        int ").append(idx).append(" = Debug.makeSymbolicInteger(\"")
                  .append(idx).append("\");\n");
                sb.append("        Verify.ignoreIf(!(").append(idx).append(" >= 0 && ").append(idx)
                  .append(" < ").append(p.getPool().size()).append("));\n");
                sb.append("        ").append(p.getType()).append(' ').append(p.getSlotName())
                  .append(" = POOL_").append(sanitize(p.getType())).append('[').append(idx)
                  .append("];\n");
            }
        }
    }

    private static void printPrecondition(StringBuilder sb, CallBlock block) {
        for (Clause c : block.getPreconditions()) {
            if (c.mentionsSymbolicParam()) {
                sb.append("        Debug.assume(").append(c.getJavaExpr()).append(");\n");
            } else {
                sb.append("        Verify.ignoreIf(!(").append(c.getJavaExpr()).append("));\n");
            }
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

    private static String sanitize(String type) {
        return type.trim().replaceAll("[^A-Za-z0-9_]", "_");
    }
}
