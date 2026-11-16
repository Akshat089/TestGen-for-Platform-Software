package in.ac.iiitb.plproject.atc.generated;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;

/**
 * DEFECTS4J — StrBuilder (Apache Commons Lang 2.x), bugs Lang-59 and Lang-61.
 *
 * An ADAPTER over the real org.apache.commons.lang.text.StrBuilder from the
 * Defects4J checkout: a mutable string builder that keeps its characters in a
 * char[] buffer of which only the first size() are content. Removing text
 * (clear, setLength, deleteFirst, deleteAll) leaves old characters in the rest
 * of the buffer, and both bugs are about code that reads or writes the buffer
 * beyond size().
 *
 * Global state (mirrors specs/StrBuilder.spec):
 * <pre>
 *   content  : String        sb.toString(), the builder's content
 *   length   : int           sb.length()
 *   Appended : List&lt;String&gt;  every string this adapter has appended so far, in order
 * </pre>
 *
 * Appended is a record of the adapter's own calls, not something read from the
 * builder. It is there so a precondition can ask for "a string that was
 * appended before" (Appended.contains(s)) — searching for or deleting a string
 * that never went in would only ever exercise the not-found path. TestGen's
 * contract language has no string operations in preconditions, so it cannot
 * ask for "a substring of content" directly.
 *
 * appendPadRight fixes the pad character to '-': TestGen has no char inputs.
 */
public class Helper {

    public static StrBuilder sb = new StrBuilder();
    public static String content = "";
    public static int length = 0;
    public static List<String> Appended = new ArrayList<String>();

    /** new StrBuilder(capacity): start again with an empty builder of that initial capacity. */
    public static void create(int capacity) {
        sb = new StrBuilder(capacity);
        Appended = new ArrayList<String>();
        sync();
    }

    /** sb.append(s). */
    public static void append(String s) {
        sb.append(s);
        Appended.add(s);
        sync();
    }

    /** sb.appendFixedWidthPadRight(s, width, '-'): s cut or padded to exactly width characters. */
    public static void appendPadRight(String s, int width) {
        sb.appendFixedWidthPadRight(s, width, '-');
        Appended.add(s);
        sync();
    }

    /** sb.deleteFirst(s). */
    public static void deleteFirst(String s) {
        sb.deleteFirst(s);
        sync();
    }

    /** sb.deleteAll(s). */
    public static void deleteAll(String s) {
        sb.deleteAll(s);
        sync();
    }

    /** sb.indexOf(s). */
    public static int indexOf(String s) {
        return sb.indexOf(s);
    }

    /** sb.contains(s). */
    public static boolean contains(String s) {
        return sb.contains(s);
    }

    /** sb.setLength(n). */
    public static void setLength(int n) {
        sb.setLength(n);
        sync();
    }

    /** sb.clear(). */
    public static void clear() {
        sb.clear();
        sync();
    }

    /** sb.length(). Named count because length is the state variable. */
    public static int count() {
        return sb.length();
    }

    private static void sync() {
        content = sb.toString();
        length = sb.length();
    }

    public static void reset() {
        sb = new StrBuilder();
        content = "";
        length = 0;
        Appended = new ArrayList<String>();
    }
}
