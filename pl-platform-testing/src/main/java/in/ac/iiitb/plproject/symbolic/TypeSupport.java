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
 * have neither a symbolic nor a concrete handling and is rejected outright by
 * the input checks (rule 13f) rather than silently approximated.
 *
 * <p>{@code String} is never made symbolic here: jpf-symbc's string decision
 * procedure is a separate, fragile configuration this project does not enable,
 * and (independent of the type) ANY value — String or otherwise — that a spec
 * uses as the key/element of a {@code Set}/{@code Map} {@code contains}/
 * {@code containsKey}/{@code get} lookup breaks both z3 and choco the same
 * way: confirmed by direct experiment, {@code java.util.HashMap}'s bit-spread
 * in {@code hashCode()} puts a bitwise shift into the path condition, and
 * neither solver accepts it ("Choco does not support bitwise SHIFT" /
 * "Error Z3"). {@link in.ac.iiitb.plproject.ir.IrBuilder} asks THIS class only
 * the type-level question; it decides the hash-key case itself by scanning
 * the spec for that usage.
 */
public final class TypeSupport {

    private TypeSupport() {}

    /** How many distinct literals a pooled parameter's shared pool holds. */
    public static final int POOL_SIZE = 3;

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

    /** True for a type this generator pools instead of solving (currently: String). */
    public static boolean isPooledType(String type) {
        return "String".equals(trim(type));
    }

    /** True for a type with neither symbolic nor concrete handling (rule 13f). */
    public static boolean isUnsupportedType(String type) {
        return !isSymbolicallySolvable(type) && !isPooledType(type);
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
                return "\"u" + (ordinal + 1) + "\"";
            default:
                return String.valueOf(7 + 2 * ordinal);
        }
    }

    private static String trim(String type) {
        return type == null ? null : type.trim();
    }

    /** Formats a raw pool value ({@code "s1"}, {@code "101"}) as Java source of {@code type}. */
    public static String literal(String type, String raw) {
        if ("String".equals(trim(type))) {
            return "\"" + raw.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        if ("long".equals(trim(type)) || "Long".equals(trim(type))) {
            return raw + "L";
        }
        return raw;
    }

    /** The array element type to declare a shared literal pool with, for {@code type}. */
    public static String poolArrayElementType(String type) {
        return "String".equals(trim(type)) ? "String" : trim(type);
    }
}
