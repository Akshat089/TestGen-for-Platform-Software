package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/StringSet.spec + ../specs/StringSet.tests.
 * One @Test method per sequence, with the inputs SPF solved from the contract;
 * every assertion traces to one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: add -> add -> contains -> remove
    @Test(timeout = 2000)
    public void stringSetDryRun() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }
}
