package in.ac.iiitb.plproject.spf;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.symbolic.TypeSupport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Step 5: one solved {@link PathVector} per path -&gt; one {@link InputVector},
 * mapping each {@code c<i>_<param>} (or, pooled, {@code c<i>_<param>$idx})
 * back to its {@link CallBlock}/{@link ParamSlot} and converting the raw
 * solver value to a Java literal of the declared type (rule 7): {@code long}
 * gets an {@code L} suffix, {@code String} is quoted and escaped, a boxed
 * type that the solver left unconstrained can still legally be {@code null}
 * for a nullable slot, etc. A slot the solver never mentions gets a small,
 * distinct, non-zero, deterministic literal instead of defaulting to 0 or ""
 * (rule 8); a pooled slot resolves its solved INDEX against its shared
 * literal pool and is recorded as such rather than presented as solved
 * (rule 9).
 */
public final class ValueExtractor {

    private ValueExtractor() {}

    public static InputVector extract(TestPlan plan, PathVector path) {
        Map<String, String> literals = new LinkedHashMap<String, String>();
        Set<String> unconstrained = new LinkedHashSet<String>();
        Set<String> pooled = new LinkedHashSet<String>();
        int ordinal = 0;

        for (CallBlock block : plan.getCallBlocks()) {
            for (ParamSlot slot : block.getParams()) {
                if (slot.isPooled()) {
                    pooled.add(slot.getSlotName());
                    String idxRaw = path.getVars().get(slot.getPoolIndexName());
                    int idx = 0;
                    if (idxRaw != null) {
                        try {
                            idx = (int) Math.round(Double.parseDouble(idxRaw));
                        } catch (NumberFormatException ignored) {
                            idx = 0;
                        }
                    } else {
                        unconstrained.add(slot.getSlotName());
                    }
                    List<String> pool = slot.getPool();
                    if (idx < 0 || idx >= pool.size()) idx = 0;
                    literals.put(slot.getSlotName(), TypeSupport.literal(slot.getType(), pool.get(idx)));
                } else {
                    String raw = path.getVars().get(slot.getSlotName());
                    if (raw == null) {
                        unconstrained.add(slot.getSlotName());
                        literals.put(slot.getSlotName(),
                                TypeSupport.unconstrainedLiteral(slot.getType(), ordinal++));
                    } else {
                        literals.put(slot.getSlotName(), toJavaLiteral(slot.getType(), raw));
                    }
                }
            }
        }
        return new InputVector(path.getPathId(), literals, unconstrained, pooled);
    }

    public static List<InputVector> extractAll(TestPlan plan, List<PathVector> paths) {
        List<InputVector> out = new ArrayList<InputVector>();
        for (PathVector p : paths) {
            if (p.getEvent() == PathVector.Event.PATH && p.isSat()) {
                out.add(extract(plan, p));
            }
        }
        return out;
    }

    private static String toJavaLiteral(String type, String rawSolverValue) {
        String t = type.trim();
        String v = rawSolverValue.trim();
        switch (t) {
            case "boolean": case "Boolean":
                if (v.equals("1")) return "true";
                if (v.equals("0")) return "false";
                return String.valueOf(Boolean.parseBoolean(v));
            case "long": case "Long":
                return parseLongLike(v) + "L";
            case "char": case "Character":
                return "'" + (char) parseLongLike(v) + "'";
            case "double": case "Double":
                return v.contains(".") ? v : v + ".0";
            default:
                // int/Integer/short/Short/byte/Byte all print as a plain decimal literal.
                return String.valueOf(parseLongLike(v));
        }
    }

    private static long parseLongLike(String v) {
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return (long) Double.parseDouble(v);
        }
    }
}
