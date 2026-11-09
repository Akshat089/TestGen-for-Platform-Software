package in.ac.iiitb.plproject.symbolic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * What the configured solver (z3/choco, via jpf-symbc's {@code Debug}) can and
 * cannot make symbolic — rule 9.
 *
 * <p>Every parameter type across the fourteen libraries' {@code .spec} files
 * is a numeric/boolean primitive or wrapper, or {@code String}. {@code Debug}
 * has no symbolic factory for {@code float} at all, so a float parameter would
 * have no symbolic handling and is rejected outright by the input checks
 * (rule 13f) rather than silently approximated.
 *
 * <p>{@code String} is never a symbolic string: jpf-symbc's string decision
 * procedure is a separate, fragile configuration this project does not enable.
 * A String input is an integer id plus a null flag instead
 * ({@link IntegerModel}), and a membership test on a symbolic key — String or
 * number — is expanded into equalities over the collection's concrete
 * contents, because running {@code java.util.HashMap}'s {@code hashCode()}
 * bit-spread on a symbolic value puts a shift into the path condition that
 * neither solver accepts ("Choco does not support bitwise SHIFT" / "Error Z3").
 */
public final class TypeSupport {

    private TypeSupport() {}

    private static final Map<String, String> SYMBOLIC_FACTORY = new HashMap<String, String>();
    static {
        SYMBOLIC_FACTORY.put("int", "Debug.makeSymbolicInteger");
        SYMBOLIC_FACTORY.put("Integer", "Debug.makeSymbolicInteger");
        SYMBOLIC_FACTORY.put("long", "Debug.makeSymbolicLong");
        SYMBOLIC_FACTORY.put("Long", "Debug.makeSymbolicLong");
        SYMBOLIC_FACTORY.put("short", "Debug.makeSymbolicShort");
        SYMBOLIC_FACTORY.put("Short", "Debug.makeSymbolicShort");
        SYMBOLIC_FACTORY.put("byte", "Debug.makeSymbolicByte");
        SYMBOLIC_FACTORY.put("Byte", "Debug.makeSymbolicByte");
        SYMBOLIC_FACTORY.put("double", "Debug.makeSymbolicReal");
        SYMBOLIC_FACTORY.put("Double", "Debug.makeSymbolicReal");
        SYMBOLIC_FACTORY.put("boolean", "Debug.makeSymbolicBoolean");
        SYMBOLIC_FACTORY.put("Boolean", "Debug.makeSymbolicBoolean");
        SYMBOLIC_FACTORY.put("char", "Debug.makeSymbolicChar");
        SYMBOLIC_FACTORY.put("Character", "Debug.makeSymbolicChar");
    }

    private static final Set<String> BOXED = new HashSet<String>();
    static {
        BOXED.add("Integer"); BOXED.add("Long"); BOXED.add("Short"); BOXED.add("Byte");
        BOXED.add("Double"); BOXED.add("Boolean"); BOXED.add("Character");
    }

    /** Numeric/boolean primitives and their boxed forms Debug can make symbolic. */
    public static boolean isNumericOrBooleanType(String type) {
        return type != null && SYMBOLIC_FACTORY.containsKey(type.trim());
    }

    /** True for a type SPF's configured solver can genuinely make symbolic. */
    public static boolean isSymbolicallySolvable(String type) {
        return isNumericOrBooleanType(type);
    }

    /** True for a type with no symbolic handling (rule 13f): neither a number/boolean nor a String id. */
    public static boolean isUnsupportedType(String type) {
        return !isSymbolicallySolvable(type) && !IntegerModel.isString(type);
    }

    public static boolean isBoxed(String type) {
        return type != null && BOXED.contains(type.trim());
    }

    private static final Map<String, String> PRIMITIVE_TO_BOXED = new HashMap<String, String>();
    static {
        PRIMITIVE_TO_BOXED.put("int", "Integer");
        PRIMITIVE_TO_BOXED.put("long", "Long");
        PRIMITIVE_TO_BOXED.put("short", "Short");
        PRIMITIVE_TO_BOXED.put("byte", "Byte");
        PRIMITIVE_TO_BOXED.put("double", "Double");
        PRIMITIVE_TO_BOXED.put("boolean", "Boolean");
        PRIMITIVE_TO_BOXED.put("char", "Character");
    }

    /**
     * {@code type} itself, plus its boxed form if it's a primitive — a generic
     * type argument such as {@code Set<Integer>} is always written boxed even
     * when the parameter using that same logical type is declared as the
     * primitive {@code int}, so a check against "every type used as a Set/Map
     * key" has to look at both spellings.
     */
    public static java.util.Set<String> withBoxedEquivalent(String type) {
        String t = trim(type);
        java.util.Set<String> out = new java.util.LinkedHashSet<String>();
        out.add(t);
        String boxed = PRIMITIVE_TO_BOXED.get(t);
        if (boxed != null) out.add(boxed);
        return out;
    }

    private static final Set<String> PRIMITIVES = new HashSet<String>(java.util.Arrays.asList(
            "int", "long", "short", "byte", "double", "boolean", "char"));

    /** True for a Java primitive type the model holds as one symbolic value. */
    public static boolean isPrimitive(String type) {
        return type != null && PRIMITIVES.contains(type.trim());
    }

    /** The primitive behind a boxed type ({@code Integer -> int}); the type itself otherwise. */
    public static String unboxed(String type) {
        String t = trim(type);
        for (Map.Entry<String, String> e : PRIMITIVE_TO_BOXED.entrySet()) {
            if (e.getValue().equals(t)) return e.getKey();
        }
        return t;
    }

    /** Java's default value for a primitive type, as source text. */
    public static String defaultValue(String type) {
        switch (unboxed(type)) {
            case "boolean": return "false";
            case "double": return "0.0";
            case "long": return "0L";
            case "char": return "'\\0'";
            default: return "0";
        }
    }

    /** The {@code Debug.makeSymbolic*} factory call for a numeric/boolean type. */
    public static String symbolicFactory(String type) {
        String f = SYMBOLIC_FACTORY.get(trim(type));
        if (f == null) throw new IllegalArgumentException("not symbolically solvable: " + type);
        return f;
    }

    /** A small, distinct, non-zero, deterministic literal for an unconstrained value (rule 8). */
    public static String unconstrainedLiteral(String type, int ordinal) {
        String t = trim(type);
        switch (t) {
            case "boolean": case "Boolean":
                return (ordinal % 2 == 0) ? "true" : "false";
            case "double": case "Double":
                return (7 + 2 * ordinal) + ".0";
            case "long": case "Long":
                return (7 + 2 * ordinal) + "L";
            case "char": case "Character":
                return "'" + (char) ('a' + (ordinal % 26)) + "'";
            case "String":
                return literal(t, "s" + (7 + 2 * ordinal));
            default:
                return String.valueOf(7 + 2 * ordinal);
        }
    }

    private static String trim(String type) {
        return type == null ? null : type.trim();
    }

    /** Formats a raw value ({@code s7}, {@code 101}) as Java source of {@code type}. */
    public static String literal(String type, String raw) {
        if ("String".equals(trim(type))) {
            return "\"" + raw.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        if ("long".equals(trim(type)) || "Long".equals(trim(type))) {
            return raw + "L";
        }
        return raw;
    }

    /** The boxed form of a primitive ({@code int -> Integer}); the type itself otherwise. */
    public static String boxed(String type) {
        String t = trim(type);
        String b = PRIMITIVE_TO_BOXED.get(t);
        return b != null ? b : t;
    }

    /** A solver value ({@code "3"}, {@code "1"} for true, {@code "2.5"}) as a Java literal of {@code type}. */
    public static String solverLiteral(String type, String rawSolverValue) {
        String v = rawSolverValue.trim();
        switch (trim(type)) {
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
