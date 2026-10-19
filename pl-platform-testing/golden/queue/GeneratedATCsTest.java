package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/Queue.spec + ../specs/Queue.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s1";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s2";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: enqueue
        String c0_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.enqueue(c0_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c0_elem));

        // call 1: enqueue
        String c1_elem = "s3";
        assertTrue("PRE enqueue: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.enqueue(c1_elem);
        assertTrue("POST enqueue: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST enqueue: Q.contains(elem)", Helper.Q.contains(c1_elem));

        // call 2: dequeue
        assertTrue("PRE dequeue: size > 0", Helper.size > 0);
        String old2_head = Helper.head;
        int old2_size = Helper.size;
        String c2_result = Helper.dequeue();
        assertTrue("POST dequeue: \\result == dequeuedElem", c2_result == c2_result);
        assertTrue("POST dequeue: dequeuedElem == \\old(head)", c2_result == old2_head);
        assertTrue("POST dequeue: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: front
        assertTrue("PRE front: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.front();
        assertTrue("POST front: \\result == frontElem", c3_result == c3_result);
        assertTrue("POST front: frontElem == head", c3_result == Helper.head);
        assertTrue("POST front: size == \\old(size)", Helper.size == old3_size);

        // call 4: isEmpty
        assertTrue("PRE isEmpty: size >= 0", Helper.size >= 0);
        int old4_size = Helper.size;
        boolean c4_result = Helper.isEmpty();
        assertTrue("POST isEmpty: \\result == emptyFlag", c4_result == c4_result);
        assertTrue("POST isEmpty: emptyFlag == false", c4_result == false);
        assertTrue("POST isEmpty: size == \\old(size)", Helper.size == old4_size);
    }
}
