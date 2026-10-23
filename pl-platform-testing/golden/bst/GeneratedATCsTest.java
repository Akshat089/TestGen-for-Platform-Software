package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/BinarySearchTree.spec + ../specs/BinarySearchTree.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: insert -> insert -> insert -> contains -> min -> delete
    @Test(timeout = 2000)
    public void binarySearchTreeDryRun() {
        Helper.reset();

        // call 0: insert
        int old0_size = Helper.size;
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int old1_size = Helper.size;
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int old2_size = Helper.size;
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int old3_size = Helper.size;
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        int old4_size = Helper.size;
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int c4_result = Helper.min();
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int old5_size = Helper.size;
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }
}
