package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/HashMapLib.spec + ../specs/HashMapLib.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: put -> put -> getOldValue -> remove
    @Test(timeout = 2000)
    public void hashMapDryRun() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 9;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 13;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        int old2_size = Helper.size;
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        int old3_size = Helper.size;
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }
}
