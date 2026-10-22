package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/IntArray.spec + ../specs/IntArray.tests.
 * One @Test method per sequence, with the inputs SPF solved from the contract;
 * every assertion traces to one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: set -> set -> get -> sum -> indexOf
    @Test(timeout = 2000)
    public void intArrayDryRun() {
        Helper.reset();

        // call 0: set
        int c0_index = 3;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 7;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }
}
