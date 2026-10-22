package in.ac.iiitb.plproject.ir;

import in.ac.iiitb.plproject.symbolic.TypeSupport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translates a contract clause into a condition over the symbolic driver's
 * MODEL of the library state — never the library itself.
 *
 * <p>How each state variable is modelled is decided from its declared type
 * alone ({@link StateModel}):
 * <ul>
 *   <li>a numeric/boolean primitive is one symbolic value of that type;</li>
 *   <li>a {@code String} is a pool index ({@code 0..POOL_SIZE-1}) plus a null
 *       flag; a boxed number is its value plus a null flag;</li>
 *   <li>a {@code Set}/{@code List} of a pooled element type is one 0/1 flag per
 *       pool value ({@code contains}, {@code size()} = the count of set flags);</li>
 *   <li>a {@code Map} with a pooled key type is a presence flag plus a value per
 *       key ({@code containsKey}, {@code get(k) == x});</li>
 *   <li>a {@code Map} from a pooled key to a {@code List}/{@code Set} of a pooled
 *       element is a presence flag per key plus a flag per (key, element) pair
 *       ({@code Adj.get(a).contains(b)}, {@code Adj.get(a).size()});</li>
 *   <li>an {@code int[]} with a starting value is one symbolic int per cell.</li>
 * </ul>
 * Flags are ints restricted to 0/1 rather than booleans so that a size is a
 * plain sum. A clause that uses anything else — an unmodelled state variable,
 * a symbolic divisor, an unknown method — raises {@link Unmodellable} and is
 * left out of the driver: it is CHECK-ONLY, asserted in JUnit and nowhere else.
 */
public final class ModelTranslator {

    /** Raised for a clause the model cannot express; the message is the reason. */
    public static class Unmodellable extends RuntimeException {
        public Unmodellable(String reason) { super(reason); }
    }

    public enum Shape { SCALAR, REF, SET, MAP, NESTED, ARRAY, UNSUPPORTED }

    /** How one state variable is represented in the model. */
    public static final class StateModel {
        public final String name;
        public final String declaredType;
        public final Shape shape;
        /** SCALAR: the Java type of the model value. */
        public final String scalarType;
        /** REF, or a MAP's values: true when the value is a pool index (Strings). */
        public final boolean valueIsIndex;
        /** SET/LIST only: true for a List, whose size() is not the number of distinct elements. */
        public final boolean isList;
        /** ARRAY only: the number of cells, from the starting value. */
        public final int arrayLength;
        /** Starting value text, or null for Java's default. */
        public final String initializer;
        /** UNSUPPORTED only: why. */
        public final String reason;

        StateModel(String name, String declaredType, Shape shape, String scalarType, boolean valueIsIndex,
                   boolean isList, int arrayLength, String initializer, String reason) {
            this.name = name;
            this.declaredType = declaredType;
            this.shape = shape;
            this.scalarType = scalarType;
            this.valueIsIndex = valueIsIndex;
            this.isList = isList;
            this.arrayLength = arrayLength;
            this.initializer = initializer;
            this.reason = reason;
        }
    }

    private static final int POOL = TypeSupport.POOL_SIZE;
    private static final Pattern COLLECTION = Pattern.compile("(Set|List)<\\s*([^<>]+?)\\s*>");
    private static final Pattern MAP = Pattern.compile("Map<\\s*([^,<>]+?)\\s*,\\s*(.+)>");

    private ModelTranslator() {}

    /** Decides each state variable's model from its declared type and starting value. */
    public static Map<String, StateModel> modelState(Map<String, String> stateVars,
                                                     Map<String, String> initializers,
                                                     java.util.Set<String> pooledTypes) {
        Map<String, StateModel> out = new LinkedHashMap<String, StateModel>();
        for (Map.Entry<String, String> e : stateVars.entrySet()) {
            String name = e.getKey();
            String type = e.getValue().trim();
            String init = initializers.get(name);
            out.put(name, modelOf(name, type, init, pooledTypes));
        }
        return out;
    }

    private static StateModel modelOf(String name, String type, String init, java.util.Set<String> pooled) {
        if (TypeSupport.isPrimitive(type)) {
            return new StateModel(name, type, Shape.SCALAR, type, false, false, 0, init, null);
        }
        if ("String".equals(type)) {
            return new StateModel(name, type, Shape.REF, "int", true, false, 0, init, null);
        }
        if (TypeSupport.isBoxed(type)) {
            return new StateModel(name, type, Shape.REF, TypeSupport.unboxed(type), false, false, 0, init, null);
        }
        Matcher coll = COLLECTION.matcher(type);
        if (coll.matches()) {
            String elem = coll.group(2).trim();
            if (!isPooled(elem, pooled)) {
                return unsupported(name, type, init, "its elements (" + elem + ") are not drawn from a pool");
            }
            return new StateModel(name, type, Shape.SET, "int", false, coll.group(1).equals("List"), 0, init, null);
        }
        Matcher map = MAP.matcher(type);
        if (map.matches()) {
            String key = map.group(1).trim();
            String value = map.group(2).trim();
            if (!isPooled(key, pooled)) {
                return unsupported(name, type, init, "its keys (" + key + ") are not drawn from a pool");
            }
            Matcher inner = COLLECTION.matcher(value);
            if (inner.matches()) {
                if (!isPooled(inner.group(2).trim(), pooled)) {
                    return unsupported(name, type, init, "its inner elements are not drawn from a pool");
                }
                return new StateModel(name, type, Shape.NESTED, "int", false, inner.group(1).equals("List"),
                        0, init, null);
            }
            if ("String".equals(value)) {
                return new StateModel(name, type, Shape.MAP, "int", true, false, 0, init, null);
            }
            if (TypeSupport.isBoxed(value) && !"Boolean".equals(value)) {
                return new StateModel(name, type, Shape.MAP, TypeSupport.unboxed(value), false, false, 0, init, null);
            }
            return unsupported(name, type, init, "its values (" + value + ") have no model");
        }
        if (type.equals("int[]")) {
            int length = arrayLength(init);
            if (length < 0) {
                return unsupported(name, type, init, "an array needs a starting value to fix its length");
            }
            return new StateModel(name, type, Shape.ARRAY, "int", false, false, length, init, null);
        }
        return unsupported(name, type, init, "type " + type + " has no model");
    }

    private static StateModel unsupported(String name, String type, String init, String reason) {
        return new StateModel(name, type, Shape.UNSUPPORTED, null, false, false, 0, init, reason);
    }

    private static boolean isPooled(String type, java.util.Set<String> pooled) {
        for (String t : pooled) {
            if (TypeSupport.withBoxedEquivalent(t).contains(type)) return true;
        }
        return "String".equals(type);
    }

    /** {@code {0, 0, 0, 0}} -&gt; 4, {@code new int[4]} -&gt; 4, otherwise -1. */
    static int arrayLength(String init) {
        if (init == null) return -1;
        String t = init.trim();
        Matcher m = Pattern.compile("new\\s+int\\s*\\[\\s*(\\d+)\\s*\\]").matcher(t);
        if (m.matches()) return Integer.parseInt(m.group(1));
        if (t.startsWith("{") && t.endsWith("}")) {
            String body = t.substring(1, t.length() - 1).trim();
            return body.isEmpty() ? 0 : body.split(",").length;
        }
        return -1;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // printing model variables
    // ─────────────────────────────────────────────────────────────────────────

    /** The model variable (or array) holding {@code state} at {@code version}. */
    public static String var(String state, int version) {
        return "m" + version + "_" + state;
    }

    /** Lines declaring a state variable's starting value (version 0). */
    public static List<String> startDecls(StateModel s, List<String> stringPool) {
        List<String> out = new ArrayList<String>();
        String v = var(s.name, 0);
        switch (s.shape) {
            case SCALAR:
                out.add(s.scalarType + " " + v + " = " + (s.initializer != null ? s.initializer
                        : TypeSupport.defaultValue(s.scalarType)) + ";");
                break;
            case REF:
                if (s.initializer == null || s.initializer.trim().equals("null")) {
                    out.add(s.scalarType + " " + v + " = " + TypeSupport.defaultValue(s.scalarType) + ";");
                    out.add("int " + v + "$null = 1;");
                } else if (s.valueIsIndex) {
                    String lit = s.initializer.trim().replaceAll("^\"|\"$", "");
                    int idx = stringPool.indexOf(lit);
                    if (idx < 0) throw new Unmodellable("starting value " + s.initializer + " is not in the pool");
                    out.add("int " + v + " = " + idx + ";");
                    out.add("int " + v + "$null = 0;");
                } else {
                    out.add(s.scalarType + " " + v + " = " + s.initializer.trim() + ";");
                    out.add("int " + v + "$null = 0;");
                }
                break;
            case SET:
                out.add("int[] " + v + " = new int[" + POOL + "];");
                break;
            case MAP:
                out.add("int[] " + v + "$has = new int[" + POOL + "];");
                out.add(s.scalarType + "[] " + v + "$val = new " + s.scalarType + "[" + POOL + "];");
                break;
            case NESTED:
                out.add("int[] " + v + "$has = new int[" + POOL + "];");
                out.add("int[] " + v + "$in = new int[" + (POOL * POOL) + "];");
                break;
            case ARRAY:
                String init = s.initializer.trim();
                out.add("int[] " + v + " = " + (init.startsWith("{") ? init : init) + ";");
                break;
            default:
                break;
        }
        return out;
    }

    /** Lines declaring a fresh (unconstrained) value of a state variable at {@code version}. */
    public static List<String> freshDecls(StateModel s, int version) {
        List<String> out = new ArrayList<String>();
        String v = var(s.name, version);
        switch (s.shape) {
            case SCALAR:
                out.add(s.scalarType + " " + v + " = " + TypeSupport.symbolicFactory(s.scalarType)
                        + "(\"" + v + "\");");
                break;
            case REF:
                out.addAll(freshRef(v, s.scalarType, s.valueIsIndex));
                break;
            case SET:
                out.add("int[] " + v + " = " + flagArray(v, POOL) + ";");
                break;
            case MAP:
                out.add("int[] " + v + "$has = " + flagArray(v + "$has", POOL) + ";");
                out.add(s.scalarType + "[] " + v + "$val = " + valueArray(v + "$val", s.scalarType,
                        s.valueIsIndex, POOL) + ";");
                break;
            case NESTED:
                out.add("int[] " + v + "$has = " + flagArray(v + "$has", POOL) + ";");
                out.add("int[] " + v + "$in = " + flagArray(v + "$in", POOL * POOL) + ";");
                break;
            case ARRAY:
                out.add("int[] " + v + " = " + valueArray(v, "int", false, s.arrayLength) + ";");
                break;
            default:
                break;
        }
        return out;
    }

    /** A fresh nullable value: pool index or raw value, plus its 0/1 null flag. */
    public static List<String> freshRef(String v, String valueType, boolean isIndex) {
        List<String> out = new ArrayList<String>();
        if (isIndex) {
            out.add("int " + v + " = index(\"" + v + "\");");
        } else {
            out.add(valueType + " " + v + " = " + TypeSupport.symbolicFactory(valueType) + "(\"" + v + "\");");
        }
        out.add("int " + v + "$null = flag(\"" + v + "$null\");");
        return out;
    }

    private static String flagArray(String base, int n) {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(", ");
            sb.append("flag(\"").append(base).append('_').append(i).append("\")");
        }
        return sb.append(" }").toString();
    }

    private static String valueArray(String base, String type, boolean isIndex, int n) {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(", ");
            String name = base + "_" + i;
            if (isIndex) sb.append("index(\"").append(name).append("\")");
            else sb.append(TypeSupport.symbolicFactory(type)).append("(\"").append(name).append("\")");
        }
        return sb.append(" }").toString();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // the frame rule, per model entry
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * The driver lines that make a collection's model fresh after a call whose
     * postcondition names it. A Set/Map/array is modelled entry by entry (one
     * flag/value per pool key), so the frame rule applies per entry: when every
     * mention is a keyed access ({@code E.contains(x)}, {@code M.get(k)},
     * {@code A[i]}, {@code Adj.get(a).contains(b)}), only those entries become
     * fresh and every other entry keeps its value. Any other mention
     * ({@code size()}, a clause the model cannot parse) makes the whole
     * collection fresh.
     */
    public static List<String> freshCollection(StateModel s, int version, List<String> ensuresClauses,
                                               Scope postScope) {
        List<String> out = freshDecls(s, version);
        List<String[]> keys = keyedMentions(s, ensuresClauses, postScope);
        if (keys == null) return out;
        String now = var(s.name, version);
        String before = var(s.name, version - 1 < 0 ? 0 : postScope.old.get(s.name));
        switch (s.shape) {
            case SET:
                out.addAll(frame(now, before, POOL, keys, 0));
                break;
            case ARRAY:
                out.addAll(frame(now, before, s.arrayLength, keys, 0));
                break;
            case MAP:
                out.addAll(frame(now + "$has", before + "$has", POOL, keys, 0));
                out.addAll(frame(now + "$val", before + "$val", POOL, keys, 0));
                break;
            case NESTED: {
                // rows: the presence flag of every named row; cells: the named (row, element) pairs
                out.addAll(frame(now + "$has", before + "$has", POOL, keys, 0));
                List<String[]> cells = new ArrayList<String[]>();
                for (String[] k : keys) {
                    if (k.length == 2) cells.add(new String[] { "(" + k[0] + " * " + POOL + " + " + k[1] + ")" });
                }
                out.addAll(frame(now + "$in", before + "$in", POOL * POOL, cells, 0));
                break;
            }
            default:
                break;
        }
        return out;
    }

    /** {@code now[j] == before[j]} for every j no key names. */
    private static List<String> frame(String now, String before, int n, List<String[]> keys, int part) {
        List<String> out = new ArrayList<String>();
        for (int j = 0; j < n; j++) {
            StringBuilder cond = new StringBuilder();
            for (String[] k : keys) cond.append(k[part]).append(" == ").append(j).append(" || ");
            out.add("Verify.ignoreIf(!(" + cond + now + "[" + j + "] == " + before + "[" + j + "]));");
        }
        return out;
    }

    /**
     * The key index codes of every mention of {@code s} in the clauses (a pair
     * for a nested row/element access), or null when some mention is not keyed.
     */
    private static List<String[]> keyedMentions(StateModel s, List<String> clauses, Scope scope) {
        List<String[]> keys = new ArrayList<String[]>();
        for (String clause : clauses) {
            if (!ClauseText.freeIdentifiers(clause).contains(s.name)) continue;
            ClauseExpr e;
            try {
                e = ClauseExpr.parse(clause);
            } catch (ClauseExpr.ParseException ex) {
                return null;
            }
            if (!collectKeys(e, s, scope, keys)) return null;
        }
        return keys;
    }

    private static boolean collectKeys(ClauseExpr e, StateModel s, Scope scope, List<String[]> keys) {
        if (e instanceof ClauseExpr.Name) return !((ClauseExpr.Name) e).name.equals(s.name);
        if (e instanceof ClauseExpr.Unary) return collectKeys(((ClauseExpr.Unary) e).operand, s, scope, keys);
        if (e instanceof ClauseExpr.Binary) {
            ClauseExpr.Binary b = (ClauseExpr.Binary) e;
            return collectKeys(b.left, s, scope, keys) && collectKeys(b.right, s, scope, keys);
        }
        if (e instanceof ClauseExpr.Index) {
            ClauseExpr.Index ix = (ClauseExpr.Index) e;
            if (isName(ix.array, s.name) && s.shape == Shape.ARRAY) {
                String k = keyCode(ix.index, scope, false);
                if (k == null || !collectKeys(ix.index, s, scope, keys)) return false;
                keys.add(new String[] { k });
                return true;
            }
            return collectKeys(ix.array, s, scope, keys) && collectKeys(ix.index, s, scope, keys);
        }
        if (e instanceof ClauseExpr.Call) {
            ClauseExpr.Call c = (ClauseExpr.Call) e;
            for (ClauseExpr a : c.args) if (!collectKeys(a, s, scope, keys)) return false;
            if (isName(c.target, s.name)) {
                if (c.args.size() != 1) return false;
                boolean keyed = (s.shape == Shape.SET && c.method.equals("contains"))
                        || (s.shape == Shape.MAP && (c.method.equals("containsKey") || c.method.equals("get")))
                        || (s.shape == Shape.NESTED && c.method.equals("containsKey"));
                if (!keyed) return false;
                String k = keyCode(c.args.get(0), scope, true);
                if (k == null) return false;
                keys.add(new String[] { k });
                return true;
            }
            if (c.target instanceof ClauseExpr.Call && isName(((ClauseExpr.Call) c.target).target, s.name)
                    && s.shape == Shape.NESTED) {
                ClauseExpr.Call get = (ClauseExpr.Call) c.target;
                if (!get.method.equals("get") || get.args.size() != 1 || !c.method.equals("contains")
                        || c.args.size() != 1) {
                    return false;
                }
                if (!collectKeys(get.args.get(0), s, scope, keys)) return false;
                String row = keyCode(get.args.get(0), scope, true);
                String elem = keyCode(c.args.get(0), scope, true);
                if (row == null || elem == null) return false;
                keys.add(new String[] { row, elem });
                return true;
            }
            return collectKeys(c.target, s, scope, keys);
        }
        return true;
    }

    private static boolean isName(ClauseExpr e, String name) {
        return e instanceof ClauseExpr.Name && ((ClauseExpr.Name) e).name.equals(name);
    }

    /** The pool index (or, for an array, the int index) {@code e} names, or null. */
    private static String keyCode(ClauseExpr e, Scope scope, boolean poolKey) {
        try {
            Translation t = new Translation(scope, new ArrayList<String>());
            Term term = t.term(e);
            if (!t.guards.isEmpty()) return null;
            if (poolKey) return term.keyIndex;
            return term.kind == Term.Kind.NUM ? term.code : null;
        } catch (Unmodellable ex) {
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // translating one clause
    // ─────────────────────────────────────────────────────────────────────────

    /** Everything a clause's names can mean, for one clause of one call. */
    public static final class Scope {
        final Map<String, StateModel> state;
        /** state name -&gt; model version a bare name reads. */
        final Map<String, Integer> current;
        /** state name -&gt; model version {@code \old(name)} reads; null in a precondition. */
        final Map<String, Integer> old;
        final Map<String, ParamSlot> params;
        final String resultAlias;
        /** The call's fresh result, or null when it has none. */
        final Term result;
        final List<String> stringPool;

        public Scope(Map<String, StateModel> state, Map<String, Integer> current, Map<String, Integer> old,
                     Map<String, ParamSlot> params, String resultAlias, Term result, List<String> stringPool) {
            this.state = state;
            this.current = current;
            this.old = old;
            this.params = params;
            this.resultAlias = resultAlias;
            this.result = result;
            this.stringPool = stringPool;
        }
    }

    /** A translated sub-expression. */
    public static final class Term {
        enum Kind { NUM, BOOL, REF, NULL, COLL, ROW, ARRAY }

        final Kind kind;
        final String code;
        /** REF: 0/1 null-flag expression. */
        final String nullCode;
        /** A pool index for this value when it can be used as a Set/Map key, else null. */
        final String keyIndex;
        /** REF: true when {@code code} is a pool index (a String). */
        final boolean isIndex;
        /** COLL/ROW: the state variable's model and its printed variable prefix. */
        final StateModel coll;

        private Term(Kind kind, String code, String nullCode, String keyIndex, boolean isIndex, StateModel coll) {
            this.kind = kind;
            this.code = code;
            this.nullCode = nullCode;
            this.keyIndex = keyIndex;
            this.isIndex = isIndex;
            this.coll = coll;
        }

        static Term num(String code, String keyIndex) { return new Term(Kind.NUM, code, null, keyIndex, false, null); }
        static Term bool(String code) { return new Term(Kind.BOOL, code, null, null, false, null); }
        static Term ref(String code, String nullCode, boolean isIndex, String keyIndex) {
            return new Term(Kind.REF, code, nullCode, keyIndex, isIndex, null);
        }

        /** The model value of a fresh call result, with the call's result variable name. */
        public static Term resultOf(String type, String var) {
            String t = type.trim();
            if ("boolean".equals(t)) return bool(var);
            if (TypeSupport.isPrimitive(t)) return num(var, null);
            if ("String".equals(t)) return ref(var, var + "$null", true, var);
            return ref(var, var + "$null", false, null);
        }
    }

    /** Translates {@code text} into model Java, or throws {@link Unmodellable}. */
    public static String translate(String text, Scope scope) {
        ClauseExpr expr;
        try {
            expr = ClauseExpr.parse(text);
        } catch (ClauseExpr.ParseException e) {
            throw new Unmodellable("cannot parse: " + e.getMessage());
        }
        List<String> guards = new ArrayList<String>();
        Term t = new Translation(scope, guards).term(expr);
        if (t.kind != Term.Kind.BOOL) throw new Unmodellable("not a condition");
        StringBuilder sb = new StringBuilder();
        for (String g : guards) sb.append(g).append(" && ");
        sb.append(t.code);
        return sb.toString();
    }

    private static final class Translation {
        final Scope scope;
        final List<String> guards;

        Translation(Scope scope, List<String> guards) {
            this.scope = scope;
            this.guards = guards;
        }

        Term term(ClauseExpr e) {
            if (e instanceof ClauseExpr.Literal) return literal((ClauseExpr.Literal) e);
            if (e instanceof ClauseExpr.Result) return result();
            if (e instanceof ClauseExpr.Old) {
                String name = ((ClauseExpr.Old) e).name;
                if (scope.old == null || !scope.old.containsKey(name)) {
                    throw new Unmodellable("\\old(" + name + ") has no model value");
                }
                return state(name, scope.old.get(name));
            }
            if (e instanceof ClauseExpr.Name) return name(((ClauseExpr.Name) e).name);
            if (e instanceof ClauseExpr.Unary) return unary((ClauseExpr.Unary) e);
            if (e instanceof ClauseExpr.Binary) return binary((ClauseExpr.Binary) e);
            if (e instanceof ClauseExpr.Call) return call((ClauseExpr.Call) e);
            if (e instanceof ClauseExpr.Index) return index((ClauseExpr.Index) e);
            throw new Unmodellable("unknown expression");
        }

        Term literal(ClauseExpr.Literal lit) {
            switch (lit.kind) {
                case "boolean": return Term.bool(lit.text);
                case "null": return new Term(Term.Kind.NULL, "null", "1", null, false, null);
                case "String": {
                    int idx = scope.stringPool.indexOf(lit.text);
                    if (idx < 0) throw new Unmodellable("string literal \"" + lit.text + "\" is not in the pool");
                    return Term.ref(String.valueOf(idx), "0", true, String.valueOf(idx));
                }
                default: return Term.num(lit.text, null);
            }
        }

        Term result() {
            if (scope.result == null) throw new Unmodellable("\\result has no model value here");
            return scope.result;
        }

        Term name(String n) {
            if (scope.resultAlias != null && n.equals(scope.resultAlias)) return result();
            if (n.equals("result") && scope.result != null && !scope.params.containsKey(n)
                    && !scope.state.containsKey(n)) {
                return result();
            }
            ParamSlot p = scope.params.get(n);
            if (p != null) return param(p);
            if (scope.state.containsKey(n)) return state(n, scope.current.get(n));
            throw new Unmodellable("'" + n + "' has no model value");
        }

        Term param(ParamSlot p) {
            String t = p.getType().trim();
            String v = p.getSlotName();
            if (p.isPooled()) {
                if ("String".equals(t)) return Term.ref(v, "0", true, v);
                // a pooled number: SPF picks the index; the value is 101, 103, 105, ...
                String value = "(" + IrBuilder.NUMERIC_POOL_BASE + " + " + IrBuilder.NUMERIC_POOL_STEP
                        + " * " + v + ")";
                if (TypeSupport.isBoxed(t)) return Term.ref(value, "0", false, v);
                return Term.num(value, v);
            }
            if ("boolean".equals(t) || "Boolean".equals(t)) return Term.bool(v);
            if (TypeSupport.isBoxed(t)) return Term.ref(v, "0", false, null);
            return Term.num(v, null);
        }

        Term state(String n, Integer version) {
            StateModel s = scope.state.get(n);
            if (s == null || version == null) throw new Unmodellable("'" + n + "' has no model value");
            String v = var(n, version);
            switch (s.shape) {
                case SCALAR:
                    return "boolean".equals(s.scalarType) ? Term.bool(v) : Term.num(v, null);
                case REF:
                    return Term.ref(v, v + "$null", s.valueIsIndex, s.valueIsIndex ? v : null);
                case SET: case MAP: case NESTED:
                    return new Term(Term.Kind.COLL, v, null, null, false, s);
                case ARRAY:
                    return new Term(Term.Kind.ARRAY, v, null, null, false, s);
                default:
                    throw new Unmodellable("state '" + n + "' is not modelled: " + s.reason);
            }
        }

        Term unary(ClauseExpr.Unary u) {
            Term t = term(u.operand);
            if (u.op.equals("!")) {
                if (t.kind != Term.Kind.BOOL) throw new Unmodellable("'!' on a non-condition");
                return Term.bool("!(" + t.code + ")");
            }
            return Term.num("(-" + numeric(t) + ")", null);
        }

        Term binary(ClauseExpr.Binary b) {
            String op = b.op;
            if (op.equals("&&") || op.equals("||")) {
                Term l = term(b.left);
                Term r = term(b.right);
                if (l.kind != Term.Kind.BOOL || r.kind != Term.Kind.BOOL) {
                    throw new Unmodellable("'" + op + "' on a non-condition");
                }
                return Term.bool("(" + l.code + " " + op + " " + r.code + ")");
            }
            if (op.equals("==") || op.equals("!=")) {
                String eq = equality(term(b.left), term(b.right));
                return Term.bool(op.equals("==") ? eq : "!" + eq);
            }
            Term l = term(b.left);
            Term r = term(b.right);
            if (l.kind == Term.Kind.REF && l.isIndex || r.kind == Term.Kind.REF && r.isIndex) {
                throw new Unmodellable("'" + op + "' on a pooled String");
            }
            String lc = numeric(l);
            String rc = numeric(r);
            switch (op) {
                case "<": case "<=": case ">": case ">=":
                    return Term.bool("(" + lc + " " + op + " " + rc + ")");
                case "/": case "%":
                    if (!(b.right instanceof ClauseExpr.Literal) || r.code.equals("0")) {
                        throw new Unmodellable("'" + op + "' by a symbolic divisor (jpf-symbc's IDIV/IREM"
                                + " checks the divisor's concrete value, not the path condition)");
                    }
                    return Term.num("(" + lc + " " + op + " " + rc + ")", null);
                default:
                    return Term.num("(" + lc + " " + op + " " + rc + ")", null);
            }
        }

        /** The numeric model value of {@code t}; a boxed value must be non-null to be read. */
        String numeric(Term t) {
            switch (t.kind) {
                case NUM: return t.code;
                case REF:
                    if (t.isIndex) throw new Unmodellable("arithmetic on a pooled String");
                    guard(t.nullCode + " == 0");
                    return t.code;
                default:
                    throw new Unmodellable("not a number");
            }
        }

        void guard(String g) {
            if (g.equals("0 == 0")) return;
            guards.add("(" + g + ")");
        }

        String equality(Term l, Term r) {
            if (l.kind == Term.Kind.NULL && r.kind == Term.Kind.NULL) return "(true)";
            if (l.kind == Term.Kind.NULL || r.kind == Term.Kind.NULL) {
                Term other = l.kind == Term.Kind.NULL ? r : l;
                if (other.kind != Term.Kind.REF) throw new Unmodellable("only a reference can be null");
                return "(" + other.nullCode + " == 1)";
            }
            if (l.kind == Term.Kind.BOOL || r.kind == Term.Kind.BOOL) {
                if (l.kind != r.kind) throw new Unmodellable("comparing a condition with a value");
                return "(" + l.code + " == " + r.code + ")";
            }
            if (l.kind == Term.Kind.REF && r.kind == Term.Kind.REF) {
                if (l.isIndex != r.isIndex) throw new Unmodellable("comparing a String with a number");
                if (l.nullCode.equals("0") && r.nullCode.equals("0")) {
                    return "(" + l.code + " == " + r.code + ")";
                }
                if (l.nullCode.equals("0") || r.nullCode.equals("0")) {
                    String n = l.nullCode.equals("0") ? r.nullCode : l.nullCode;
                    return "(" + n + " == 0 && " + l.code + " == " + r.code + ")";
                }
                return "((" + l.nullCode + " == 1 && " + r.nullCode + " == 1) || (" + l.nullCode + " == 0 && "
                        + r.nullCode + " == 0 && " + l.code + " == " + r.code + "))";
            }
            if (l.kind == Term.Kind.REF && l.isIndex || r.kind == Term.Kind.REF && r.isIndex) {
                throw new Unmodellable("comparing a String with a number");
            }
            return "(" + numeric(l) + " == " + numeric(r) + ")";
        }

        String key(Term t) {
            if (t.keyIndex == null) throw new Unmodellable("key is not drawn from the pool");
            if (t.kind == Term.Kind.REF) guard(t.nullCode + " == 0");
            return t.keyIndex;
        }

        Term call(ClauseExpr.Call c) {
            Term target = term(c.target);
            String m = c.method;
            int argc = c.args.size();
            if (target.kind == Term.Kind.COLL) {
                StateModel s = target.coll;
                String v = target.code;
                switch (s.shape) {
                    case SET:
                        if (m.equals("contains") && argc == 1) {
                            return Term.bool("(" + v + "[" + key(term(c.args.get(0))) + "] == 1)");
                        }
                        if ((m.equals("size") || m.equals("isEmpty")) && argc == 0 && !s.isList) {
                            String sum = sum(v, 0, POOL);
                            return m.equals("size") ? Term.num(sum, null) : Term.bool("(" + sum + " == 0)");
                        }
                        break;
                    case MAP:
                        if (m.equals("containsKey") && argc == 1) {
                            return Term.bool("(" + v + "$has[" + key(term(c.args.get(0))) + "] == 1)");
                        }
                        if (m.equals("get") && argc == 1) {
                            String k = key(term(c.args.get(0)));
                            return Term.ref(v + "$val[" + k + "]", "(1 - " + v + "$has[" + k + "])",
                                    s.valueIsIndex, s.valueIsIndex ? v + "$val[" + k + "]" : null);
                        }
                        if (m.equals("size") && argc == 0) return Term.num(sum(v + "$has", 0, POOL), null);
                        break;
                    case NESTED:
                        if (m.equals("containsKey") && argc == 1) {
                            return Term.bool("(" + v + "$has[" + key(term(c.args.get(0))) + "] == 1)");
                        }
                        if (m.equals("get") && argc == 1) {
                            String k = key(term(c.args.get(0)));
                            return new Term(Term.Kind.ROW, v, null, k, false, s);
                        }
                        break;
                    default:
                        break;
                }
            } else if (target.kind == Term.Kind.ROW) {
                String v = target.code;
                String row = target.keyIndex;
                guard(v + "$has[" + row + "] == 1");
                if (m.equals("contains") && argc == 1) {
                    String k = key(term(c.args.get(0)));
                    return Term.bool("(" + v + "$in[" + row + " * " + POOL + " + " + k + "] == 1)");
                }
                if (m.equals("size") && argc == 0 && !target.coll.isList) {
                    StringBuilder sb = new StringBuilder("(");
                    for (int j = 0; j < POOL; j++) {
                        if (j > 0) sb.append(" + ");
                        sb.append(v).append("$in[").append(row).append(" * ").append(POOL).append(" + ")
                          .append(j).append(']');
                    }
                    return Term.num(sb.append(')').toString(), null);
                }
            }
            throw new Unmodellable("." + m + "(...) has no model"
                    + (target.coll != null && target.coll.isList && m.equals("size")
                       ? " (a List may hold duplicates)" : ""));
        }

        Term index(ClauseExpr.Index ix) {
            Term arr = term(ix.array);
            if (arr.kind != Term.Kind.ARRAY) throw new Unmodellable("indexing a non-array");
            Term i = term(ix.index);
            return Term.num(arr.code + "[" + numeric(i) + "]", null);
        }

        static String sum(String array, int from, int n) {
            StringBuilder sb = new StringBuilder("(");
            for (int j = from; j < from + n; j++) {
                if (j > from) sb.append(" + ");
                sb.append(array).append('[').append(j).append(']');
            }
            return sb.append(')').toString();
        }
    }
}
