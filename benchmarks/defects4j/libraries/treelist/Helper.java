package in.ac.iiitb.plproject.atc.generated;

import java.util.ListIterator;

import org.apache.commons.collections.list.TreeList;

/**
 * DEFECTS4J — TreeList (Apache Commons Collections 3.x, generic trunk), bug Collections-20.
 *
 * An ADAPTER over the real org.apache.commons.collections.list.TreeList from
 * the Defects4J checkout: a List stored as a balanced tree. Collections-20 is
 * in its ListIterator, so the adapter keeps ONE list iterator in state and
 * exposes its moves (next, previous, remove) as separate calls; the test
 * string decides the order.
 *
 * Global state (mirrors specs/TreeList.spec):
 * <pre>
 *   L           : TreeList&lt;String&gt;  the real list
 *   size        : int              L.size()
 *   It          : ListIterator     the iterator iterStart() opened
 *   itNextIndex : int              It.nextIndex()
 *   lastMove    : int              1 after iterNext, -1 after iterPrevious, 0 otherwise
 * </pre>
 *
 * lastMove records which adapter call ran last, so remove's precondition can
 * say "the iterator just returned an element" (ListIterator has no public way
 * to ask). It is the adapter's own bookkeeping, not read from the library.
 */
public class Helper {

    public static TreeList<String> L = new TreeList<String>();
    public static int size = 0;
    public static ListIterator<String> It = null;
    public static int itNextIndex = 0;
    public static int lastMove = 0;

    /** L.add(e). */
    public static void add(String e) {
        L.add(e);
        size = L.size();
    }

    /** L.get(index). */
    public static String get(int index) {
        return L.get(index);
    }

    /** It = L.listIterator(index). */
    public static void iterStart(int index) {
        It = L.listIterator(index);
        itNextIndex = It.nextIndex();
        lastMove = 0;
    }

    /** It.next(). */
    public static String iterNext() {
        String e = It.next();
        itNextIndex = It.nextIndex();
        lastMove = 1;
        return e;
    }

    /** It.previous(). */
    public static String iterPrevious() {
        String e = It.previous();
        itNextIndex = It.nextIndex();
        lastMove = -1;
        return e;
    }

    /** It.remove(): removes the element the last next() or previous() returned. */
    public static void iterRemove() {
        It.remove();
        itNextIndex = It.nextIndex();
        size = L.size();
        lastMove = 0;
    }

    /** L.size(). Named count because size is the state variable. */
    public static int count() {
        return L.size();
    }

    public static void reset() {
        L = new TreeList<String>();
        size = 0;
        It = null;
        itNextIndex = 0;
        lastMove = 0;
    }
}
