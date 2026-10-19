package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/Stack.spec + ../specs/Stack.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s1";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s1";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s1";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s2";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s1";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s3";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s2";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s1";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s2";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s2";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s2";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s3";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s3";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s1";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s3";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s2";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: push
        String c0_elem = "s3";
        assertTrue("PRE push: elem != null", c0_elem != null);
        int old0_size = Helper.size;
        Helper.push(c0_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old0_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c0_elem);

        // call 1: push
        String c1_elem = "s3";
        assertTrue("PRE push: elem != null", c1_elem != null);
        int old1_size = Helper.size;
        Helper.push(c1_elem);
        assertTrue("POST push: size == \\old(size) + 1", Helper.size == old1_size + 1);
        assertTrue("POST push: top == elem", Helper.top == c1_elem);

        // call 2: pop
        assertTrue("PRE pop: size > 0", Helper.size > 0);
        int old2_size = Helper.size;
        String c2_result = Helper.pop();
        assertTrue("POST pop: \\result == poppedElem", c2_result == c2_result);
        assertTrue("POST pop: size == \\old(size) - 1", Helper.size == old2_size - 1);

        // call 3: peek
        assertTrue("PRE peek: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
        assertTrue("POST peek: \\result == peekedElem", c3_result == c3_result);
        assertTrue("POST peek: peekedElem == top", c3_result == Helper.top);
        assertTrue("POST peek: size == \\old(size)", Helper.size == old3_size);
    }
}
