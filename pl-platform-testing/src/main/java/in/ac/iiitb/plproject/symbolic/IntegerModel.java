package in.ac.iiitb.plproject.symbolic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rewrites one assume of an SPF driver into plain integer reasoning that the
 * solver handles directly — no hashing, no symbolic strings.
 *
 * <p>Strings: every String input that is still symbolic is an integer id
 * ({@code x$id}) plus a null flag ({@code x$nonNull}). {@code x == e},
 * {@code x != e} and {@code x.equals(e)} compare ids; {@code x != null} is the
 * null flag. A concrete string's id is {@code k} for {@code "s<k>"} and a fresh
 * id outside the solver's range for any other string ({@link #RUNTIME}); a
 * solved id {@code k} is written back as {@code "s" + k}.
 *
 * <p>Collections: when an assume tests membership of a symbolic key/element, the
 * collection is fully concrete (every call so far ran with concrete arguments).
 * Its own code is never run on the symbolic value; instead the clause is
 * expanded over its current concrete contents, read through the state variable
 * the contract names:
 * <pre>
 *   M.containsKey(k)   -&gt;  k == a1 || ... || k == an      (false if empty)
 *   !M.containsKey(k)  -&gt;  k != a1 &amp;&amp; ... &amp;&amp; k != an      (true if empty)
 *   S.contains(x)      -&gt;  the same, over the set's elements
 *   C[M.get(k)]        -&gt;  (k == a1 &amp;&amp; C[M[a1]]) || ... over the keys of M
 * </pre>
 * The disjunction is printed as a loop over the concrete contents with one
 * {@code ==} per element ({@code memberKey}/{@code memberStr}, and a labelled
 * loop for {@code get}), so SPF branches once per element and the path
 * condition holds exactly those equalities. A concrete key is evaluated
 * normally. Anything else applied to a symbolic value — a string operation, or a
 * collection call that is not membership — is {@link Unsupported}.
 */
public final class IntegerModel {

    private IntegerModel() {}

    /** Suffix of a string input's integer id in the driver. */
    public static final String ID = "$id";
    /** Suffix of a string input's null flag in the driver (true: not null). */
    public static final String NON_NULL = "$nonNull";
    /**
     * {@code "s<k>"} has id {@code k} for {@code |k| < CANONICAL_BOUND}; any other
     * concrete string gets an id below {@code -CANONICAL_BOUND}, far outside the
     * solver's integer range, so a solved id never names it.
     */
    public static final int CANONICAL_BOUND = 1000000;

    /** A clause the integer model cannot express: generation stops on it. */
    public static final class Unsupported extends RuntimeException {
        public Unsupported(String message) { super(message); }
    }

    private static final Set<String> STRING_OPERATIONS = new HashSet<String>(Arrays.asList(
            "length", "charAt", "substring", "startsWith", "endsWith", "concat", "indexOf", "lastIndexOf",
            "isEmpty", "toUpperCase", "toLowerCase", "trim", "compareTo", "compareToIgnoreCase",
            "equalsIgnoreCase", "matches", "replace", "replaceAll", "split", "hashCode", "intern",
            "codePointAt", "toCharArray", "contentEquals", "regionMatches"));

    private static final Set<String> MEMBERSHIP = new HashSet<String>(Arrays.asList("contains", "containsKey"));

    /**
     * The driver statement for one assume: assigns {@code ok}. {@code symbolic}
     * maps every input still symbolic in this round to its declared type;
     * {@code stateTypes} maps each state variable to its declared type.
     */
    public static String assume(String clauseJava, Map<String, String> symbolic, Map<String, String> stateTypes) {
        Rewriter r = new Rewriter(clauseJava, symbolic, stateTypes);
        Node expr = r.rewrite(r.hoist(new Parser(clauseJava).parseAll()));
        if (r.loops.isEmpty()) return "ok = " + strip(expr.print()) + ";";
        StringBuilder sb = new StringBuilder("ok = false; scan: ");
        List<String> guards = new ArrayList<String>();
        for (Loop l : r.loops) {
            sb.append("for (Object ").append(l.var).append(" : elements(").append(l.map.print()).append(")) ");
            guards.add(l.guard);
        }
        guards.add(expr.print());
        sb.append("if (").append(String.join(" && ", guards)).append(") { ok = true; break scan; }");
        return sb.toString();
    }

    private static String strip(String s) {
        return s.startsWith("(") && s.endsWith(")") && balanced(s.substring(1, s.length() - 1))
                ? s.substring(1, s.length() - 1) : s;
    }

    private static boolean balanced(String s) {
        int depth = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') depth++;
            if (c == ')' && --depth < 0) return false;
        }
        return depth == 0;
    }

    /** True for a type the model represents as an id plus a null flag. */
    public static boolean isString(String type) {
        return type != null && "String".equals(type.trim());
    }

    /** Java source of a solved string: {@code null}, or {@code "s<id>"}. */
    public static String stringLiteral(boolean nonNull, long id) {
        return nonNull ? TypeSupport.literal("String", "s" + id) : "null";
    }

    /**
     * The members of the driver that evaluate a rewritten assume on the
     * concrete contents of a collection: printed once into every driver.
     */
    public static final String RUNTIME = String.join("\n",
            "    private static final java.util.List<String> named = new java.util.ArrayList<String>();",
            "",
            "    /**",
            "     * A concrete string's id: k for \"s<k>\" (|k| < " + CANONICAL_BOUND + "), otherwise " + (-CANONICAL_BOUND - 1)
                    + ", " + (-CANONICAL_BOUND - 2) + ", ...",
            "     * in the order first seen — outside the solver's range, so no solved id is ever one of them.",
            "     */",
            "    private static int id(String s) {",
            "        if (s.length() > 1 && s.charAt(0) == 's') {",
            "            try {",
            "                int k = Integer.parseInt(s.substring(1));",
            "                if (k > -" + CANONICAL_BOUND + " && k < " + CANONICAL_BOUND
                    + " && String.valueOf(k).equals(s.substring(1))) return k;",
            "            } catch (NumberFormatException notAnId) {",
            "                // not \"s<k>\"",
            "            }",
            "        }",
            "        int i = named.indexOf(s);",
            "        if (i < 0) {",
            "            named.add(s);",
            "            i = named.size() - 1;",
            "        }",
            "        return -" + CANONICAL_BOUND + " - 1 - i;",
            "    }",
            "",
            "    /** The concrete keys of a map, or the concrete elements of any other collection. */",
            "    private static Iterable<?> elements(Object c) {",
            "        return c instanceof java.util.Map ? ((java.util.Map<?, ?>) c).keySet() : (Iterable<?>) c;",
            "    }",
            "",
            "    /** x == a for a string input x (id, null flag) and a concrete value a. */",
            "    private static boolean isStr(int id, boolean nonNull, Object a) {",
            "        if (a == null) return !nonNull;",
            "        return a instanceof String && nonNull && id == id((String) a);",
            "    }",
            "",
            "    /** x == y for two string inputs. */",
            "    private static boolean sameStr(int id1, boolean nonNull1, int id2, boolean nonNull2) {",
            "        if (!nonNull1 || !nonNull2) return nonNull1 == nonNull2;",
            "        return id1 == id2;",
            "    }",
            "",
            "    private static boolean isKey(int k, Object a) {",
            "        return a instanceof Number && k == ((Number) a).intValue();",
            "    }",
            "",
            "    private static boolean isKey(long k, Object a) {",
            "        return a instanceof Number && k == ((Number) a).longValue();",
            "    }",
            "",
            "    /** k == a1 || ... || k == an over the concrete contents of c. */",
            "    private static boolean memberKey(int k, Object c) {",
            "        for (Object a : elements(c)) if (isKey(k, a)) return true;",
            "        return false;",
            "    }",
            "",
            "    private static boolean memberKey(long k, Object c) {",
            "        for (Object a : elements(c)) if (isKey(k, a)) return true;",
            "        return false;",
            "    }",
            "",
            "    /** x == a1 || ... || x == an for a string input x over the concrete contents of c. */",
            "    private static boolean memberStr(int id, boolean nonNull, Object c) {",
            "        for (Object a : elements(c)) if (isStr(id, nonNull, a)) return true;",
            "        return false;",
            "    }",
            "");

    // ─────────────────────────────────────────────────────────────────────────
    // the rewrite
    // ─────────────────────────────────────────────────────────────────────────

    /** One {@code M.get(k)} with a symbolic key, expanded as a loop over the keys of M. */
    private static final class Loop {
        final String var;
        final Node map;
        final String guard;
        final String key;
        Loop(String var, Node map, String key, String guard) {
            this.var = var;
            this.map = map;
            this.key = key;
            this.guard = guard;
        }
    }

    private static final class Rewriter {
        final String clause;
        final Map<String, String> symbolic;
        final Map<String, String> stateTypes;
        final List<Loop> loops = new ArrayList<Loop>();

        Rewriter(String clause, Map<String, String> symbolic, Map<String, String> stateTypes) {
            this.clause = clause;
            this.symbolic = symbolic;
            this.stateTypes = stateTypes;
        }

        Unsupported unsupported(String what) {
            return new Unsupported("unsupported " + what + " in " + clause);
        }

        Unsupported stringOperation(String op) {
            return new Unsupported("unsupported string operation " + op + " in " + clause + "; needs a string solver");
        }

        boolean isSymString(Node n) {
            return n instanceof Leaf && isString(symbolic.get(((Leaf) n).text));
        }

        boolean mentionsSymbolic(Node n) {
            if (n instanceof Leaf) return symbolic.containsKey(((Leaf) n).text);
            for (Node c : n.children()) if (c != null && mentionsSymbolic(c)) return true;
            return false;
        }

        /** The declared type of a concrete expression, when it is a state variable or a lookup in one. */
        String typeOf(Node n) {
            if (n instanceof Field && ((Field) n).target instanceof Leaf
                    && "Helper".equals(((Leaf) ((Field) n).target).text)) {
                String t = stateTypes.get(((Field) n).name);
                return t == null ? null : t.trim();
            }
            if (n instanceof Leaf) {
                String t = symbolic.get(((Leaf) n).text);
                return t == null ? null : t.trim();
            }
            if (n instanceof Call && "get".equals(((Call) n).name) && ((Call) n).target != null) {
                List<String> args = typeArguments(typeOf(((Call) n).target));
                return args.isEmpty() ? null : args.get(args.size() - 1);
            }
            return null;
        }

        /**
         * Replaces every {@code M.get(k)} whose key is symbolic by a lookup of a
         * concrete key {@code k$i} of M, recording the loop and its guard
         * {@code k == k$i}. Inner lookups first, so an outer one may use them.
         */
        Node hoist(Node n) {
            if (n instanceof Leaf) return n;
            if (n instanceof Field) return new Field(hoist(((Field) n).target), ((Field) n).name);
            if (n instanceof Index) return new Index(hoist(((Index) n).target), hoist(((Index) n).index));
            if (n instanceof Unary) return new Unary(((Unary) n).op, hoist(((Unary) n).operand));
            if (n instanceof Binary) {
                Binary b = (Binary) n;
                return new Binary(b.op, hoist(b.left), hoist(b.right));
            }
            Call c = (Call) n;
            Node target = c.target == null ? null : hoist(c.target);
            List<Node> args = new ArrayList<Node>();
            for (Node a : c.args) args.add(hoist(a));
            if ("get".equals(c.name) && target != null && args.size() == 1 && mentionsSymbolic(args.get(0))
                    && !mentionsSymbolic(target)) {
                String type = typeOf(target);
                if (type == null || !type.startsWith("Map")) {
                    throw unsupported("lookup " + new Call(target, c.name, args).print()
                            + " of a symbolic value in a collection that is not a Map");
                }
                Node key = args.get(0);
                for (Loop l : loops) {
                    if (l.map.print().equals(target.print()) && l.key.equals(key.print())) {
                        return new Call(target, "get", leaf(l.var));
                    }
                }
                String var = "k$" + loops.size();
                loops.add(new Loop(var, target, key.print(), equalsConcrete(key, new Leaf(var))));
                return new Call(target, "get", leaf(var));
            }
            return new Call(target, c.name, args);
        }

        /** {@code x == a} for a symbolic key/element x and the concrete value held in {@code a}. */
        String equalsConcrete(Node x, Node a) {
            if (isSymString(x)) {
                String v = ((Leaf) x).text;
                return "isStr(" + v + ID + ", " + v + NON_NULL + ", " + a.print() + ")";
            }
            if (mentionsSymbolic(x) && isIntegral(x)) return "isKey(" + rewrite(x).print() + ", " + a.print() + ")";
            throw unsupported("key " + x.print());
        }

        boolean isIntegral(Node x) {
            if (x instanceof Leaf) {
                String t = symbolic.get(((Leaf) x).text);
                if (t == null) return true;
                String p = TypeSupport.unboxed(t);
                return p.equals("int") || p.equals("long") || p.equals("short") || p.equals("byte");
            }
            for (Node c : x.children()) if (c != null && !isIntegral(c)) return false;
            return true;
        }

        Node rewrite(Node n) {
            if (n instanceof Leaf) {
                if (isSymString(n)) {
                    throw unsupported("use of string input " + ((Leaf) n).text
                            + " outside ==, !=, equals, a null check or membership");
                }
                return n;
            }
            if (n instanceof Field) {
                Field f = (Field) n;
                if (isSymString(f.target)) throw stringOperation(f.name);
                return new Field(rewrite(f.target), f.name);
            }
            if (n instanceof Index) return new Index(rewrite(((Index) n).target), rewrite(((Index) n).index));
            if (n instanceof Unary) return new Unary(((Unary) n).op, rewrite(((Unary) n).operand));
            if (n instanceof Binary) {
                Binary b = (Binary) n;
                boolean ls = isSymString(b.left), rs = isSymString(b.right);
                if (ls || rs) {
                    if (!b.op.equals("==") && !b.op.equals("!=")) throw stringOperation(b.op);
                    Node eq = stringEquals(ls ? b.left : b.right, ls ? b.right : b.left);
                    if (b.op.equals("==")) return eq;
                    return eq instanceof Unary && ((Unary) eq).op.equals("!") ? ((Unary) eq).operand : new Unary("!", eq);
                }
                return new Binary(b.op, rewrite(b.left), rewrite(b.right));
            }
            Call c = (Call) n;
            if (c.target != null && isSymString(c.target)) {
                if (c.name.equals("equals") && c.args.size() == 1) return stringEquals(c.target, c.args.get(0));
                throw stringOperation(c.name);
            }
            if (c.name.equals("equals") && c.args.size() == 1 && isSymString(c.args.get(0)) && c.target != null) {
                return stringEquals(c.args.get(0), c.target);
            }
            boolean symbolicArg = false, stringArg = false;
            for (Node a : c.args) {
                symbolicArg |= mentionsSymbolic(a);
                stringArg |= isSymString(a);
            }
            if (!symbolicArg && (c.target == null || !mentionsSymbolic(c.target))) return c;
            if (c.target == null || mentionsSymbolic(c.target)) throw unsupported("call " + c.print());
            if ("String".equals(typeOf(c.target)) || (stringArg && STRING_OPERATIONS.contains(c.name))) {
                throw stringOperation(c.name);
            }
            if (MEMBERSHIP.contains(c.name) && c.args.size() == 1) {
                Node x = c.args.get(0);
                if (isSymString(x)) {
                    String v = ((Leaf) x).text;
                    return new Leaf("memberStr(" + v + ID + ", " + v + NON_NULL + ", " + c.target.print() + ")");
                }
                if (isIntegral(x)) {
                    return new Leaf("memberKey(" + rewrite(x).print() + ", " + c.target.print() + ")");
                }
            }
            throw unsupported("membership clause " + c.print() + " (only contains/containsKey/get on a symbolic"
                    + " integer or string can be expanded)");
        }

        /** {@code x == o} for a symbolic string x. */
        Node stringEquals(Node x, Node o) {
            String v = ((Leaf) x).text;
            if (o instanceof Leaf && "null".equals(((Leaf) o).text)) return new Unary("!", new Leaf(v + NON_NULL));
            if (isSymString(o)) {
                String w = ((Leaf) o).text;
                return new Leaf("sameStr(" + v + ID + ", " + v + NON_NULL + ", " + w + ID + ", " + w + NON_NULL + ")");
            }
            if (mentionsSymbolic(o)) throw unsupported("comparison of string input " + v + " with " + o.print());
            return new Leaf("isStr(" + v + ID + ", " + v + NON_NULL + ", " + rewrite(o).print() + ")");
        }
    }

    private static List<Node> leaf(String text) {
        List<Node> out = new ArrayList<Node>();
        out.add(new Leaf(text));
        return out;
    }

    /** {@code Map<String,List<String>>} -&gt; {@code [String, List<String>]}. */
    static List<String> typeArguments(String type) {
        List<String> out = new ArrayList<String>();
        if (type == null) return out;
        int open = type.indexOf('<');
        if (open < 0 || !type.endsWith(">")) return out;
        String inner = type.substring(open + 1, type.length() - 1);
        int depth = 0, start = 0;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '<') depth++;
            else if (c == '>') depth--;
            else if (c == ',' && depth == 0) {
                out.add(inner.substring(start, i).trim());
                start = i + 1;
            }
        }
        out.add(inner.substring(start).trim());
        return out;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // a small expression tree for the clauses of a contract
    // ─────────────────────────────────────────────────────────────────────────

    private abstract static class Node {
        abstract String print();
        List<Node> children() { return new ArrayList<Node>(); }
    }

    /** An identifier or a literal. */
    private static final class Leaf extends Node {
        final String text;
        Leaf(String text) { this.text = text; }
        @Override String print() { return text; }
    }

    private static final class Field extends Node {
        final Node target;
        final String name;
        Field(Node target, String name) { this.target = target; this.name = name; }
        @Override String print() { return target.print() + "." + name; }
        @Override List<Node> children() { return leafList(target); }
    }

    private static final class Call extends Node {
        final Node target;
        final String name;
        final List<Node> args;
        Call(Node target, String name, List<Node> args) { this.target = target; this.name = name; this.args = args; }
        @Override String print() {
            List<String> a = new ArrayList<String>();
            for (Node n : args) a.add(strip(n.print()));
            return (target == null ? "" : target.print() + ".") + name + "(" + String.join(", ", a) + ")";
        }
        @Override List<Node> children() {
            List<Node> out = new ArrayList<Node>(args);
            out.add(target);
            return out;
        }
    }

    private static final class Index extends Node {
        final Node target;
        final Node index;
        Index(Node target, Node index) { this.target = target; this.index = index; }
        @Override String print() { return target.print() + "[" + strip(index.print()) + "]"; }
        @Override List<Node> children() { return Arrays.asList(target, index); }
    }

    private static final class Unary extends Node {
        final String op;
        final Node operand;
        Unary(String op, Node operand) { this.op = op; this.operand = operand; }
        @Override String print() { return op + operand.print(); }
        @Override List<Node> children() { return leafList(operand); }
    }

    private static final class Binary extends Node {
        final String op;
        final Node left;
        final Node right;
        Binary(String op, Node left, Node right) { this.op = op; this.left = left; this.right = right; }
        @Override String print() { return "(" + left.print() + " " + op + " " + right.print() + ")"; }
        @Override List<Node> children() { return Arrays.asList(left, right); }
    }

    private static List<Node> leafList(Node n) {
        List<Node> out = new ArrayList<Node>();
        out.add(n);
        return out;
    }

    /** Recursive descent over Java's operator precedence, for the subset a contract clause uses. */
    private static final class Parser {
        private static final String[][] LEVELS = {
            { "||" }, { "&&" }, { "==", "!=" }, { "<=", ">=", "<", ">" }, { "+", "-" }, { "*", "/", "%" },
        };

        final String text;
        final List<String> tokens = new ArrayList<String>();
        int pos;

        Parser(String text) {
            this.text = text;
            tokenize();
        }

        Node parseAll() {
            Node n = binary(0);
            if (pos != tokens.size()) throw new Unsupported("cannot parse clause " + text + " at '" + tokens.get(pos) + "'");
            return n;
        }

        private Node binary(int level) {
            if (level == LEVELS.length) return unary();
            Node left = binary(level + 1);
            while (pos < tokens.size() && Arrays.asList(LEVELS[level]).contains(tokens.get(pos))) {
                String op = tokens.get(pos++);
                left = new Binary(op, left, binary(level + 1));
            }
            return left;
        }

        private Node unary() {
            String t = peek();
            if ("!".equals(t) || "-".equals(t) || "+".equals(t)) {
                pos++;
                return new Unary(t, unary());
            }
            return postfix(primary());
        }

        private Node primary() {
            String t = next();
            if ("(".equals(t)) {
                Node inner = binary(0);
                expect(")");
                return inner;
            }
            if (Character.isJavaIdentifierStart(t.charAt(0)) && peekIs("(")) {
                return new Call(null, t, arguments());
            }
            return new Leaf(t);
        }

        private Node postfix(Node n) {
            while (true) {
                if (peekIs(".")) {
                    pos++;
                    String name = next();
                    n = peekIs("(") ? new Call(n, name, arguments()) : new Field(n, name);
                } else if (peekIs("[")) {
                    pos++;
                    Node index = binary(0);
                    expect("]");
                    n = new Index(n, index);
                } else {
                    return n;
                }
            }
        }

        private List<Node> arguments() {
            expect("(");
            List<Node> args = new ArrayList<Node>();
            if (peekIs(")")) {
                pos++;
                return args;
            }
            while (true) {
                args.add(binary(0));
                if (peekIs(",")) { pos++; continue; }
                expect(")");
                return args;
            }
        }

        private String peek() { return pos < tokens.size() ? tokens.get(pos) : null; }
        private boolean peekIs(String s) { return s.equals(peek()); }

        private String next() {
            if (pos >= tokens.size()) throw new Unsupported("cannot parse clause " + text + ": it ends too early");
            return tokens.get(pos++);
        }

        private void expect(String s) {
            if (!s.equals(next())) throw new Unsupported("cannot parse clause " + text + ": expected '" + s + "'");
        }

        private void tokenize() {
            int i = 0;
            while (i < text.length()) {
                char c = text.charAt(i);
                if (Character.isWhitespace(c)) { i++; continue; }
                int start = i;
                if (Character.isJavaIdentifierStart(c)) {
                    while (i < text.length() && Character.isJavaIdentifierPart(text.charAt(i))) i++;
                } else if (Character.isDigit(c)) {
                    while (i < text.length() && (Character.isLetterOrDigit(text.charAt(i)) || text.charAt(i) == '.')) i++;
                } else if (c == '"' || c == '\'') {
                    i++;
                    while (i < text.length() && text.charAt(i) != c) i += text.charAt(i) == '\\' ? 2 : 1;
                    i++;
                } else if (i + 1 < text.length() && Arrays.asList("||", "&&", "==", "!=", "<=", ">=")
                        .contains(text.substring(i, i + 2))) {
                    i += 2;
                } else if ("!<>+-*/%().,[]".indexOf(c) >= 0) {
                    i++;
                } else {
                    throw new Unsupported("cannot parse clause " + text + " at '" + c + "'");
                }
                tokens.add(text.substring(start, Math.min(i, text.length())));
            }
        }
    }
}
