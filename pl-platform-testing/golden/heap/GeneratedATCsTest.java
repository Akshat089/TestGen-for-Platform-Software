package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/MinHeap.spec + ../specs/MinHeap.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 0;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 0;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = 1;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 0;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 0;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = 0;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 0;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 0;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = -1;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 0;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = -1;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = 1;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 1;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 0;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = 0;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 1;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 0;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = -1;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: \\result == smallest", c3_result == c3_result);
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: \\result == removedMin", c4_result == c4_result);
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }
}
