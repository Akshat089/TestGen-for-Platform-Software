package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/LinkedList.spec + ../specs/LinkedList.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: addFirst
        Integer c0_value = 7;
        assertTrue("PRE addFirst: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.addFirst(c0_value);
        assertTrue("POST addFirst: head == value", Helper.head == c0_value);
        assertTrue("POST addFirst: L.contains(value)", Helper.L.contains(c0_value));
        assertTrue("POST addFirst: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: addLast
        Integer c1_value = 0;
        assertTrue("PRE addLast: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.addLast(c1_value);
        assertTrue("POST addLast: tail == value", Helper.tail == c1_value);
        assertTrue("POST addLast: L.contains(value)", Helper.L.contains(c1_value));
        assertTrue("POST addLast: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: removeFirst
        assertTrue("PRE removeFirst: size > 0", Helper.size > 0);
        Integer old2_head = Helper.head;
        int old2_size = Helper.size;
        Integer c2_result = Helper.removeFirst();
        assertTrue("POST removeFirst: \\result == removedValue", c2_result == c2_result);
        assertTrue("POST removeFirst: removedValue == \\old(head)", c2_result == old2_head);
        assertTrue("POST removeFirst: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: indexOf
        Integer c3_value = 0;
        assertTrue("PRE indexOf: size > 0", Helper.size > 0);
        int c3_result = Helper.indexOf(c3_value);
        assertTrue("POST indexOf: \\result == position", c3_result == c3_result);
        assertTrue("POST indexOf: position >= -1", c3_result >= -1);
        assertTrue("POST indexOf: position < size", c3_result < Helper.size);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: addFirst
        Integer c0_value = 7;
        assertTrue("PRE addFirst: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.addFirst(c0_value);
        assertTrue("POST addFirst: head == value", Helper.head == c0_value);
        assertTrue("POST addFirst: L.contains(value)", Helper.L.contains(c0_value));
        assertTrue("POST addFirst: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: addLast
        Integer c1_value = 0;
        assertTrue("PRE addLast: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.addLast(c1_value);
        assertTrue("POST addLast: tail == value", Helper.tail == c1_value);
        assertTrue("POST addLast: L.contains(value)", Helper.L.contains(c1_value));
        assertTrue("POST addLast: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: removeFirst
        assertTrue("PRE removeFirst: size > 0", Helper.size > 0);
        Integer old2_head = Helper.head;
        int old2_size = Helper.size;
        Integer c2_result = Helper.removeFirst();
        assertTrue("POST removeFirst: \\result == removedValue", c2_result == c2_result);
        assertTrue("POST removeFirst: removedValue == \\old(head)", c2_result == old2_head);
        assertTrue("POST removeFirst: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: indexOf
        Integer c3_value = 1;
        assertTrue("PRE indexOf: size > 0", Helper.size > 0);
        int c3_result = Helper.indexOf(c3_value);
        assertTrue("POST indexOf: \\result == position", c3_result == c3_result);
        assertTrue("POST indexOf: position >= -1", c3_result >= -1);
        assertTrue("POST indexOf: position < size", c3_result < Helper.size);
    }
}
