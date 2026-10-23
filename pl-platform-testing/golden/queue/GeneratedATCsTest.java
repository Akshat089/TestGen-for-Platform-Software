package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/Queue.spec + ../specs/Queue.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: enqueue -> enqueue -> dequeue -> front -> isEmpty
    @Test(timeout = 2000)
    public void queueDryRun() {
        Helper.reset();

        // call 0: enqueue
        int old0_size = Helper.size;
        String c0_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        int old1_size = Helper.size;
        String c1_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        int old3_size = Helper.size;
        assertTrue("PRE front: size > 0", Helper.size > 0);
        String c3_result = Helper.front();
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        int old4_size = Helper.size;
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }
}
