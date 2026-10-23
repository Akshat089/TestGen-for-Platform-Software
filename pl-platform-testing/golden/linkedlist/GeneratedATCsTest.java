package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/LinkedList.spec + ../specs/LinkedList.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: addFirst -> addLast -> removeFirst -> indexOf
    @Test(timeout = 2000)
    public void linkedListDryRun() {
        Helper.reset();

        // call 0: addFirst
        int old0_size = Helper.size;
        Integer c0_value = 7;
        assertTrue("PRE addFirst: value != null", c0_value != null);
        Helper.addFirst(c0_value);
        assertTrue("POST addFirst: head == value", Helper.head == c0_value);
        assertTrue("POST addFirst: L.contains(value)", Helper.L.contains(c0_value));
        assertTrue("POST addFirst: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: addLast
        int old1_size = Helper.size;
        Integer c1_value = 9;
        assertTrue("PRE addLast: value != null", c1_value != null);
        Helper.addLast(c1_value);
        assertTrue("POST addLast: tail == value", Helper.tail == c1_value);
        assertTrue("POST addLast: L.contains(value)", Helper.L.contains(c1_value));
        assertTrue("POST addLast: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: removeFirst
        Integer old2_head = Helper.head;
        int old2_size = Helper.size;
        assertTrue("PRE removeFirst: size > 0", Helper.size > 0);
        Integer c2_result = Helper.removeFirst();
        assertTrue("POST removeFirst: removedValue == \\old(head)", c2_result == old2_head);
        assertTrue("POST removeFirst: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: indexOf
        Integer c3_value = 11;
        assertTrue("PRE indexOf: size > 0", Helper.size > 0);
        int c3_result = Helper.indexOf(c3_value);
        assertTrue("POST indexOf: position >= -1", c3_result >= -1);
        assertTrue("POST indexOf: position < size", c3_result < Helper.size);
    }
}
