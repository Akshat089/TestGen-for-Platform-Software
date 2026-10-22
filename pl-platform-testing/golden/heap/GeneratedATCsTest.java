package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/MinHeap.spec + ../specs/MinHeap.tests.
 * One @Test method per sequence, with the inputs SPF solved from the contract;
 * every assertion traces to one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: insert -> insert -> insert -> peekMin -> extractMin
    @Test(timeout = 2000)
    public void minHeapDryRun() {
        Helper.reset();

        // call 0: insert
        Integer c0_value = 7;
        assertTrue("PRE insert: value != null", c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c0_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        Integer c1_value = 9;
        assertTrue("PRE insert: value != null", c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c1_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        Integer c2_value = 11;
        assertTrue("PRE insert: value != null", c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);
        assertTrue("POST insert: H.contains(value)", Helper.H.contains(c2_value));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: peekMin
        assertTrue("PRE peekMin: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();
        assertTrue("POST peekMin: smallest == minValue", c3_result == Helper.minValue);
        assertTrue("POST peekMin: size == \\old(size)", Helper.size == old3_size);

        // call 4: extractMin
        assertTrue("PRE extractMin: size > 0", Helper.size > 0);
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
        assertTrue("POST extractMin: removedMin == \\old(minValue)", c4_result == old4_minValue);
        assertTrue("POST extractMin: size == \\old(size) - 1", Helper.size == old4_size - 1);
    }
}
