package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/Stack.spec + ../specs/Stack.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: push -> push -> pop -> peek
    @Test(timeout = 2000)
    public void stackDryRun() {
        Helper.reset();

        // call 0: push
        int old0_size = Helper.size;
        String c0_elem = "s1";
        assertTrue("PRE push: elem != null", c0_elem != null);
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        int old1_size = Helper.size;
        String c1_elem = "s2";
        assertTrue("PRE push: elem != null", c1_elem != null);
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        int old2_size = Helper.size;
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        String c2_result = Helper.pop();
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        int old3_size = Helper.size;
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        String c3_result = Helper.peek();
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }
}
