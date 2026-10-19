package in.ac.iiitb.plproject.atc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The fourteen libraries GENATC knows about, replacing the old
 * {@code LibraryDryRunExamples}. Three are marked unsupported rather than
 * silently mis-generated: {@code submit -> getResult -> cancelTask} (and the
 * two libraries shaped like it) depend on one call's return value — an
 * opaque, library-chosen id — being passed as a later call's argument, which
 * rule 1 forbids and which the generator cannot satisfy by pooling (rule 12
 * forbids reading the library to learn its id-allocation scheme, and nothing
 * in the {@code .spec} text says what the id will be).
 */
public final class LibraryRegistry {

    public static class Entry {
        public final String key;
        public final String title;
        public final String specPath;
        public final String testsPath;
        public final List<String> librarySourcePaths;
        public final String outputDir;
        /** Null when supported; a one-line reason when it is not (rule: "ask before changing a contract"). */
        public final String unsupportedReason;

        Entry(String key, String title, String specPath, String testsPath,
              List<String> librarySourcePaths, String outputDir, String unsupportedReason) {
            this.key = key;
            this.title = title;
            this.specPath = specPath;
            this.testsPath = testsPath;
            this.librarySourcePaths = Collections.unmodifiableList(new ArrayList<String>(librarySourcePaths));
            this.outputDir = outputDir;
            this.unsupportedReason = unsupportedReason;
        }

        public boolean isSupported() { return unsupportedReason == null; }
        public String librarySourcePath() { return librarySourcePaths.get(0); }
    }

    private static final String ROOT = "..";

    private static Entry supported(String key, String title, String spec, String tests, String lib) {
        return new Entry(key, title, ROOT + "/specs/" + spec, ROOT + "/specs/" + tests,
                Collections.singletonList(ROOT + "/libraries/" + lib), "outputs/" + key, null);
    }

    private static Entry supportedMulti(String key, String title, String spec, String tests, String... libs) {
        return new Entry(key, title, ROOT + "/specs/" + spec, ROOT + "/specs/" + tests,
                prefixedAll(libs), "outputs/" + key, null);
    }

    private static List<String> prefixedAll(String[] libs) {
        List<String> out = new ArrayList<String>();
        for (String lib : libs) out.add(ROOT + "/libraries/" + lib);
        return out;
    }

    private static Entry unsupported(String key, String title, String spec, String tests, String lib,
                                     String reason) {
        return new Entry(key, title, ROOT + "/specs/" + spec, ROOT + "/specs/" + tests,
                Collections.singletonList(ROOT + "/libraries/" + lib), "outputs/" + key, reason);
    }

    private static Entry unsupportedMulti(String key, String title, String spec, String tests,
                                          String reason, String... libs) {
        return new Entry(key, title, ROOT + "/specs/" + spec, ROOT + "/specs/" + tests,
                prefixedAll(libs), "outputs/" + key, reason);
    }

    public static final Entry STACK = supported("stack",
            "Stack<String> — independent captures, no propagation",
            "Stack.spec", "Stack.tests", "stack/Helper.java");

    public static final Entry QUEUE = supported("queue",
            "Queue<String> — FIFO order pinned down, and a boolean capture",
            "Queue.spec", "Queue.tests", "queue/Helper.java");

    public static final Entry MATHLIB = supported("mathlib",
            "MathLib — pure functions specified by a property of the answer",
            "MathLib.spec", "MathLib.tests", "mathlib/Helper.java");

    public static final Entry ARRAYLIB = supported("arraylib",
            "IntArray — primitive int captures over indexed state",
            "IntArray.spec", "IntArray.tests", "arraylib/Helper.java");

    public static final Entry LINKEDLIST = supported("linkedlist",
            "SinglyLinkedList<Integer> — both ends of one structure",
            "LinkedList.spec", "LinkedList.tests", "linkedlist/Helper.java");

    public static final Entry HEAP = supported("heap",
            "MinHeap<Integer> — a value named before the call and after it",
            "MinHeap.spec", "MinHeap.tests", "heap/Helper.java");

    public static final Entry SET = supported("set",
            "StringSet — an idempotent call, and a boolean SERVER_OUTPUT",
            "StringSet.spec", "StringSet.tests", "set/Helper.java");

    public static final Entry HASHMAP = supported("hashmap",
            "HashMap<String,Integer> — nullable result, key is a pooled String, no propagation needed",
            "HashMapLib.spec", "HashMapLib.tests", "hashmap/Helper.java");

    public static final Entry BST = supported("bst",
            "BinarySearchTree — int key pooled (Set<Integer> lookup breaks the solver)",
            "BinarySearchTree.spec", "BinarySearchTree.tests", "bst/Helper.java");

    public static final Entry GRAPH = supported("graph",
            "Graph — two parameters against one piece of state",
            "Graph.spec", "Graph.tests", "graph/Helper.java");

    public static final Entry LRUCACHE = supported("lrucache",
            "LruCache — restore's precondition only needs an unused pooled key, not literally the evicted one",
            "LruCache.spec", "LruCache.tests", "lrucache/Helper.java");

    public static final Entry TASKQUEUE = unsupported("taskqueue",
            "TaskQueue — submit -> getResult -> cancelTask",
            "TaskQueue.spec", "TaskQueue.tests", "taskqueue/Helper.java",
            "getResult/cancelTask need the exact taskId submit() returned — an opaque,"
          + " library-chosen id, not something a pool or the solver can predict without"
          + " reading the library (forbidden) or threading a return value (forbidden by rule 1)");

    public static final Entry TICKETSERVICE = unsupported("ticketservice",
            "TicketService — findAndHoldSeats -> reserveSeats",
            "TicketService.spec", "TicketService.tests", "ticketservice/Helper.java",
            "reserveSeats needs the exact seatHoldId findAndHoldSeats() returned, for the"
          + " same reason as TaskQueue's taskId");

    public static final Entry ORDERSERVICE = unsupportedMulti("orderservice",
            "OrderService — placeOrder -> ship (façade over Catalogue/Ledger/Audit)",
            "OrderService.spec", "OrderService.tests",
            "ship needs the exact orderId placeOrder() returned, for the same reason as"
          + " TaskQueue's taskId",
            "orderservice/Helper.java", "orderservice/Catalogue.java",
            "orderservice/Ledger.java", "orderservice/Audit.java");

    public static final List<Entry> ALL = Collections.unmodifiableList(Arrays.asList(
            STACK, QUEUE, MATHLIB, ARRAYLIB, LINKEDLIST, HEAP, SET, HASHMAP, BST, GRAPH, LRUCACHE,
            TASKQUEUE, TICKETSERVICE, ORDERSERVICE));

    /**
     * The same library, but run from a different copy of its source(s) and/or a
     * different test string into a different output directory — how the
     * mutation runner replays a library against a seeded fault without
     * disturbing the checked-in outputs. Only ever called on an already-{@link
     * Entry#isSupported() supported} base entry.
     */
    public static Entry variantOf(Entry base, List<String> librarySourcePaths, String testsPath,
                                  String outputDir) {
        return new Entry(base.key, base.title, base.specPath, testsPath, librarySourcePaths,
                outputDir, null);
    }

    public static Entry byKey(String key) {
        for (Entry e : ALL) if (e.key.equalsIgnoreCase(key)) return e;
        return null;
    }

    public static String keys() {
        StringBuilder sb = new StringBuilder();
        for (Entry e : ALL) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.key);
        }
        return sb.toString();
    }

    private LibraryRegistry() {}
}
