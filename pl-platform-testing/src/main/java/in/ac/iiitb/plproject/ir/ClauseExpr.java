package in.ac.iiitb.plproject.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A parsed requires/ensures clause: the small Java-like expression language
 * the {@code .spec} files use ({@code ||}, {@code &&}, {@code !}, comparisons,
 * {@code + - * / %}, method calls such as {@code E.contains(x)}, array
 * indexing, {@code \old(v)}, {@code \result}, literals). Only
 * {@link ModelTranslator} reads it; the JUnit side keeps working on the
 * clause's raw text ({@link ClauseText}).
 */
public abstract class ClauseExpr {

    /** Raised for text this parser does not accept; the clause becomes check-only. */
    public static class ParseException extends RuntimeException {
        public ParseException(String message) { super(message); }
    }

    public static final class Literal extends ClauseExpr {
        /** "int", "double", "boolean", "String" or "null". */
        public final String kind;
        public final String text;
        Literal(String kind, String text) { this.kind = kind; this.text = text; }
    }

    public static final class Name extends ClauseExpr {
        public final String name;
        Name(String name) { this.name = name; }
    }

    public static final class Result extends ClauseExpr {}

    public static final class Old extends ClauseExpr {
        public final String name;
        Old(String name) { this.name = name; }
    }

    public static final class Unary extends ClauseExpr {
        public final String op;
        public final ClauseExpr operand;
        Unary(String op, ClauseExpr operand) { this.op = op; this.operand = operand; }
    }

    public static final class Binary extends ClauseExpr {
        public final String op;
        public final ClauseExpr left;
        public final ClauseExpr right;
        Binary(String op, ClauseExpr left, ClauseExpr right) {
            this.op = op; this.left = left; this.right = right;
        }
    }

    public static final class Call extends ClauseExpr {
        public final ClauseExpr target;
        public final String method;
        public final List<ClauseExpr> args;
        Call(ClauseExpr target, String method, List<ClauseExpr> args) {
            this.target = target; this.method = method;
            this.args = Collections.unmodifiableList(args);
        }
    }

    public static final class Index extends ClauseExpr {
        public final ClauseExpr array;
        public final ClauseExpr index;
        Index(ClauseExpr array, ClauseExpr index) { this.array = array; this.index = index; }
    }

    public static ClauseExpr parse(String text) {
        Parser p = new Parser(text);
        ClauseExpr e = p.or();
        p.skipWs();
        if (p.pos != text.length()) {
            throw new ParseException("unexpected '" + text.substring(p.pos) + "'");
        }
        return e;
    }

    private static final class Parser {
        final String s;
        int pos;

        Parser(String s) { this.s = s; }

        void skipWs() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++;
        }

        boolean eat(String token) {
            skipWs();
            if (s.startsWith(token, pos)) {
                // "<" must not swallow the first char of "<=", "=" of "==" etc.
                pos += token.length();
                return true;
            }
            return false;
        }

        boolean peekIs(String token) {
            skipWs();
            return s.startsWith(token, pos);
        }

        ClauseExpr or() {
            ClauseExpr e = and();
            while (eat("||")) e = new Binary("||", e, and());
            return e;
        }

        ClauseExpr and() {
            ClauseExpr e = equality();
            while (eat("&&")) e = new Binary("&&", e, equality());
            return e;
        }

        ClauseExpr equality() {
            ClauseExpr e = relational();
            while (true) {
                if (eat("==")) e = new Binary("==", e, relational());
                else if (eat("!=")) e = new Binary("!=", e, relational());
                else return e;
            }
        }

        ClauseExpr relational() {
            ClauseExpr e = additive();
            if (eat("<=")) return new Binary("<=", e, additive());
            if (eat(">=")) return new Binary(">=", e, additive());
            if (eat("<")) return new Binary("<", e, additive());
            if (eat(">")) return new Binary(">", e, additive());
            return e;
        }

        ClauseExpr additive() {
            ClauseExpr e = multiplicative();
            while (true) {
                if (eat("+")) e = new Binary("+", e, multiplicative());
                else if (peekIs("-") ) { pos++; e = new Binary("-", e, multiplicative()); }
                else return e;
            }
        }

        ClauseExpr multiplicative() {
            ClauseExpr e = unary();
            while (true) {
                if (eat("*")) e = new Binary("*", e, unary());
                else if (eat("/")) e = new Binary("/", e, unary());
                else if (eat("%")) e = new Binary("%", e, unary());
                else return e;
            }
        }

        ClauseExpr unary() {
            if (peekIs("!") && !peekIs("!=")) { pos++; return new Unary("!", unary()); }
            if (eat("-")) return new Unary("-", unary());
            return postfix();
        }

        ClauseExpr postfix() {
            ClauseExpr e = primary();
            while (true) {
                if (eat(".")) {
                    String method = ident();
                    if (!eat("(")) throw new ParseException("field access ." + method + " is not supported");
                    List<ClauseExpr> args = new ArrayList<ClauseExpr>();
                    if (!eat(")")) {
                        do { args.add(or()); } while (eat(","));
                        if (!eat(")")) throw new ParseException("missing ')' after arguments of " + method);
                    }
                    e = new Call(e, method, args);
                } else if (eat("[")) {
                    ClauseExpr idx = or();
                    if (!eat("]")) throw new ParseException("missing ']'");
                    e = new Index(e, idx);
                } else {
                    return e;
                }
            }
        }

        ClauseExpr primary() {
            skipWs();
            if (pos >= s.length()) throw new ParseException("unexpected end of clause");
            char c = s.charAt(pos);
            if (c == '(') {
                pos++;
                ClauseExpr e = or();
                if (!eat(")")) throw new ParseException("missing ')'");
                return e;
            }
            if (c == '\\') {
                pos++;
                String kw = ident();
                if (kw.equals("result")) return new Result();
                if (kw.equals("old")) {
                    if (!eat("(")) throw new ParseException("\\old needs '('");
                    String name = ident();
                    if (!eat(")")) throw new ParseException("\\old(" + name + " needs ')'");
                    return new Old(name);
                }
                throw new ParseException("unknown keyword \\" + kw);
            }
            if (c == '"') {
                int end = s.indexOf('"', pos + 1);
                if (end < 0) throw new ParseException("unterminated string");
                String text = s.substring(pos + 1, end);
                pos = end + 1;
                return new Literal("String", text);
            }
            if (Character.isDigit(c)) {
                int start = pos;
                while (pos < s.length() && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '.')) pos++;
                String num = s.substring(start, pos);
                if (pos < s.length() && (s.charAt(pos) == 'L' || s.charAt(pos) == 'l')) pos++;
                return new Literal(num.contains(".") ? "double" : "int", num);
            }
            String id = ident();
            if (id.equals("true") || id.equals("false")) return new Literal("boolean", id);
            if (id.equals("null")) return new Literal("null", "null");
            return new Name(id);
        }

        String ident() {
            skipWs();
            int start = pos;
            while (pos < s.length() && (Character.isLetterOrDigit(s.charAt(pos)) || s.charAt(pos) == '_')) pos++;
            if (start == pos) {
                throw new ParseException("expected a name at '" + s.substring(start) + "'");
            }
            return s.substring(start, pos);
        }
    }
}
