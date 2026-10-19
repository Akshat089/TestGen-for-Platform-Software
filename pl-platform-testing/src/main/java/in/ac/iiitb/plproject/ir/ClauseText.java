package in.ac.iiitb.plproject.ir;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Raw-text operations on one {@code requires:}/{@code ensures:} string.
 *
 * <p>Rule 5 asks for the IR to do its rewriting in two fixed steps — qualify
 * state variable names as {@code Helper.<name>}, then replace each
 * {@code \old(v)} with its snapshot local — and to keep every clause's
 * ORIGINAL text alongside the rewritten Java. Everything here works directly
 * on the clause's source substring rather than reconstructing text from an
 * expression tree, which is what lets "kept with its original text" and
 * "identical in both outputs" both be literally true: there is only ever one
 * string, split and substituted, never re-printed from a parsed form.
 */
public final class ClauseText {

    private ClauseText() {}

    private static final Pattern OLD_CALL = Pattern.compile(
            "\\\\old\\s*\\(\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*\\)");
    private static final Pattern RESULT_TOKEN = Pattern.compile(Pattern.quote("\\result"));
    private static final Pattern IDENT = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    /** Splits {@code text} into top-level {@code &&}-separated conjuncts, each trimmed. */
    public static List<String> splitTopLevelAnd(String text) {
        List<String> parts = new ArrayList<String>();
        if (text == null) return parts;
        int depth = 0;
        int start = 0;
        String t = text;
        int i = 0;
        while (i < t.length()) {
            char c = t.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && c == '&' && i + 1 < t.length() && t.charAt(i + 1) == '&') {
                String part = t.substring(start, i).trim();
                if (!part.isEmpty()) parts.add(part);
                i += 2;
                start = i;
                continue;
            }
            i++;
        }
        String last = t.substring(start).trim();
        if (!last.isEmpty()) parts.add(last);
        return parts;
    }

    /** Every bare identifier in {@code text} that is not a member/method name after a dot. */
    public static Set<String> freeIdentifiers(String text) {
        Set<String> out = new LinkedHashSet<String>();
        Matcher m = IDENT.matcher(text);
        while (m.find()) {
            if (m.start() > 0 && text.charAt(m.start() - 1) == '.') continue;
            out.add(m.group());
        }
        return out;
    }

    /** True when {@code text} contains the literal token {@code \result}. */
    public static boolean containsResultToken(String text) {
        return RESULT_TOKEN.matcher(text).find();
    }

    /** True when {@code text} contains at least one {@code \old(...)} occurrence. */
    public static boolean containsOld(String text) {
        return OLD_CALL.matcher(text).find();
    }

    /** Every name {@code v} appearing as {@code \old(v)} in {@code text}. */
    public static Set<String> oldNames(String text) {
        Set<String> out = new LinkedHashSet<String>();
        Matcher m = OLD_CALL.matcher(text);
        while (m.find()) out.add(m.group(1));
        return out;
    }

    /** The outcome of rewriting one clause's raw text. */
    public static class Rewritten {
        public final String java;
        /** Declared parameter names of the enclosing call that this clause actually references. */
        public final Set<String> referencedParams;
        /** State variable names this clause reads through {@code \old(...)}. */
        public final Set<String> oldStateVars;

        Rewritten(String java, Set<String> referencedParams, Set<String> oldStateVars) {
            this.java = java;
            this.referencedParams = referencedParams;
            this.oldStateVars = oldStateVars;
        }
    }

    /**
     * Rewrites one clause's raw text into printable Java, per rule 5: first
     * qualify state variable names as {@code Helper.<name>}, then replace each
     * {@code \old(v)} with its snapshot local. {@code \result} (and its alias,
     * when the postcondition binds one) becomes {@code resultVar}.
     *
     * @param text        the clause's original substring (one conjunct)
     * @param paramSlots  declared parameter name -&gt; printable slot reference
     *                    (symbolic variable name, or pool-resolved literal access)
     * @param stateVars   declared state variable names for this spec
     * @param resultAlias the name {@code \result} was bound to in this
     *                    postcondition ({@code \result == name}), or null
     * @param resultVar   printable reference to this call's stored result, or
     *                    null when the call has none to store
     * @param snapshotNameOf maps a state variable name to its snapshot local
     *                    name for THIS call (e.g. {@code size -> old0_size})
     */
    public static Rewritten rewrite(String text,
                                     java.util.Map<String, String> paramSlots,
                                     Set<String> stateVars,
                                     String resultAlias,
                                     String resultVar,
                                     java.util.function.Function<String, String> snapshotNameOf) {
        Set<String> referencedParams = new LinkedHashSet<String>();
        Set<String> oldStateVars = new LinkedHashSet<String>();

        // Step 1 is "qualify state variable names as Helper.<name>", which has to
        // happen to the parts of the text that are NOT inside \old(...) — an
        // \old(v) occurrence names v as a plain identifier precisely so step 2 can
        // find it, so we resolve \old(...) first (collecting its state reads),
        // replacing each with its snapshot local, and qualify everything that is
        // left afterwards. The visible result is the same either way; doing \old
        // first avoids ever trying to double-qualify inside it.
        Matcher oldMatcher = OLD_CALL.matcher(text);
        StringBuffer afterOld = new StringBuffer();
        while (oldMatcher.find()) {
            String varName = oldMatcher.group(1);
            oldStateVars.add(varName);
            String snapshot = snapshotNameOf.apply(varName);
            oldMatcher.appendReplacement(afterOld, Matcher.quoteReplacement(snapshot));
        }
        oldMatcher.appendTail(afterOld);
        String text1 = afterOld.toString();

        // \result (and its alias name, if this postcondition binds one) become
        // the call's stored result reference.
        String text2 = RESULT_TOKEN.matcher(text1).replaceAll(
                resultVar != null ? Matcher.quoteReplacement(resultVar) : "null");

        // Generic word pass: qualify state vars, substitute parameter slots and
        // the \result alias, by exact identifier match, skipping member/method
        // names (an identifier immediately preceded by '.').
        Matcher idMatcher = IDENT.matcher(text2);
        StringBuffer out = new StringBuffer();
        while (idMatcher.find()) {
            String token = idMatcher.group();
            boolean isMember = idMatcher.start() > 0 && text2.charAt(idMatcher.start() - 1) == '.';
            String replacement = null;
            if (!isMember) {
                if (resultAlias != null && token.equals(resultAlias) && resultVar != null) {
                    replacement = resultVar;
                } else if (paramSlots.containsKey(token)) {
                    replacement = paramSlots.get(token);
                    referencedParams.add(token);
                } else if (stateVars.contains(token)) {
                    replacement = "Helper." + token;
                }
            }
            idMatcher.appendReplacement(out, Matcher.quoteReplacement(replacement != null ? replacement : token));
        }
        idMatcher.appendTail(out);

        return new Rewritten(out.toString(), referencedParams, oldStateVars);
    }
}
