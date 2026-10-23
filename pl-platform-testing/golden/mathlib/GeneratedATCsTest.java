package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/MathLib.spec + ../specs/MathLib.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: abs -> gcd -> power -> factorial
    @Test(timeout = 2000)
    public void mathLibDryRun() {
        Helper.reset();

        // call 0: abs
        int old0_calls = Helper.calls;
        int c0_x = 7;
        assertTrue("PRE abs: x >= -2147483647", c0_x >= -2147483647);
        int c0_result = Helper.abs(c0_x);
        assertTrue("POST abs: absValue >= 0", c0_result >= 0);
        assertTrue("POST abs: lastResult == absValue", Helper.lastResult == c0_result);
        assertTrue("POST abs: calls == \\old(calls) + 1", Helper.calls == old0_calls + 1);

        // call 1: gcd
        int old1_calls = Helper.calls;
        int c1_a = 1;
        int c1_b = 1;
        assertTrue("PRE gcd: a > 0", c1_a > 0);
        assertTrue("PRE gcd: b > 0", c1_b > 0);
        int c1_result = Helper.gcd(c1_a, c1_b);
        assertTrue("POST gcd: divisor > 0", c1_result > 0);
        assertTrue("POST gcd: a % divisor == 0", c1_a % c1_result == 0);
        assertTrue("POST gcd: b % divisor == 0", c1_b % c1_result == 0);
        assertTrue("POST gcd: lastResult == divisor", Helper.lastResult == c1_result);
        assertTrue("POST gcd: calls == \\old(calls) + 1", Helper.calls == old1_calls + 1);

        // call 2: power
        int old2_calls = Helper.calls;
        int c2_base = 1;
        int c2_exponent = 0;
        assertTrue("PRE power: base > 0", c2_base > 0);
        assertTrue("PRE power: exponent >= 0", c2_exponent >= 0);
        assertTrue("PRE power: exponent <= 30", c2_exponent <= 30);
        int c2_result = Helper.power(c2_base, c2_exponent);
        assertTrue("POST power: powerValue > 0", c2_result > 0);
        assertTrue("POST power: lastResult == powerValue", Helper.lastResult == c2_result);
        assertTrue("POST power: calls == \\old(calls) + 1", Helper.calls == old2_calls + 1);

        // call 3: factorial
        int old3_calls = Helper.calls;
        int c3_n = 0;
        assertTrue("PRE factorial: n >= 0", c3_n >= 0);
        assertTrue("PRE factorial: n <= 12", c3_n <= 12);
        int c3_result = Helper.factorial(c3_n);
        assertTrue("POST factorial: factValue >= 1", c3_result >= 1);
        assertTrue("POST factorial: lastResult == factValue", Helper.lastResult == c3_result);
        assertTrue("POST factorial: calls == \\old(calls) + 1", Helper.calls == old3_calls + 1);
    }
}
