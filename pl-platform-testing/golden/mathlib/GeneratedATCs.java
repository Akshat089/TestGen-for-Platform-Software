package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/MathLib.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int m0_calls = 0;
        int m0_lastResult = 0;

        // call 0: abs
        int c0_x = Debug.makeSymbolicInteger("c0_x");
        // requires: x >= -2147483647
        Verify.ignoreIf(!((c0_x >= (-2147483647))));
        int c0_result = Debug.makeSymbolicInteger("c0_result");
        int m1_calls = Debug.makeSymbolicInteger("m1_calls");
        int m1_lastResult = Debug.makeSymbolicInteger("m1_lastResult");
        // ensures: \result == absValue  (only names the result)
        // ensures: absValue >= 0
        Verify.ignoreIf(!((c0_result >= 0)));
        // ensures: lastResult == absValue
        Verify.ignoreIf(!((m1_lastResult == c0_result)));
        // ensures: calls == \old(calls) + 1
        Verify.ignoreIf(!((m1_calls == (m0_calls + 1))));

        // call 1: gcd
        int c1_a = Debug.makeSymbolicInteger("c1_a");
        int c1_b = Debug.makeSymbolicInteger("c1_b");
        // requires: a > 0
        Verify.ignoreIf(!((c1_a > 0)));
        // requires: b > 0
        Verify.ignoreIf(!((c1_b > 0)));
        int c1_result = Debug.makeSymbolicInteger("c1_result");
        int m2_calls = Debug.makeSymbolicInteger("m2_calls");
        int m2_lastResult = Debug.makeSymbolicInteger("m2_lastResult");
        // ensures: \result == divisor  (only names the result)
        // ensures: divisor > 0
        Verify.ignoreIf(!((c1_result > 0)));
        // CHECK-ONLY ensures: a % divisor == 0  ('%' by a symbolic divisor (jpf-symbc's IDIV/IREM checks the divisor's concrete value, not the path condition))
        // CHECK-ONLY ensures: b % divisor == 0  ('%' by a symbolic divisor (jpf-symbc's IDIV/IREM checks the divisor's concrete value, not the path condition))
        // ensures: lastResult == divisor
        Verify.ignoreIf(!((m2_lastResult == c1_result)));
        // ensures: calls == \old(calls) + 1
        Verify.ignoreIf(!((m2_calls == (m1_calls + 1))));

        // call 2: power
        int c2_base = Debug.makeSymbolicInteger("c2_base");
        int c2_exponent = Debug.makeSymbolicInteger("c2_exponent");
        // requires: base > 0
        Verify.ignoreIf(!((c2_base > 0)));
        // requires: exponent >= 0
        Verify.ignoreIf(!((c2_exponent >= 0)));
        // requires: exponent <= 30
        Verify.ignoreIf(!((c2_exponent <= 30)));
        int c2_result = Debug.makeSymbolicInteger("c2_result");
        int m3_calls = Debug.makeSymbolicInteger("m3_calls");
        int m3_lastResult = Debug.makeSymbolicInteger("m3_lastResult");
        // ensures: \result == powerValue  (only names the result)
        // ensures: powerValue > 0
        Verify.ignoreIf(!((c2_result > 0)));
        // ensures: lastResult == powerValue
        Verify.ignoreIf(!((m3_lastResult == c2_result)));
        // ensures: calls == \old(calls) + 1
        Verify.ignoreIf(!((m3_calls == (m2_calls + 1))));

        // call 3: factorial
        int c3_n = Debug.makeSymbolicInteger("c3_n");
        // requires: n >= 0
        Verify.ignoreIf(!((c3_n >= 0)));
        // requires: n <= 12
        Verify.ignoreIf(!((c3_n <= 12)));
        int c3_result = Debug.makeSymbolicInteger("c3_result");
        int m4_calls = Debug.makeSymbolicInteger("m4_calls");
        int m4_lastResult = Debug.makeSymbolicInteger("m4_lastResult");
        // ensures: \result == factValue  (only names the result)
        // ensures: factValue >= 1
        Verify.ignoreIf(!((c3_result >= 1)));
        // ensures: lastResult == factValue
        Verify.ignoreIf(!((m4_lastResult == c3_result)));
        // ensures: calls == \old(calls) + 1
        Verify.ignoreIf(!((m4_calls == (m3_calls + 1))));
    }

    /** A symbolic 0/1 flag. */
    private static int flag(String name) {
        int v = Debug.makeSymbolicInteger(name);
        Verify.ignoreIf(v < 0 || v > 1);
        return v;
    }

    /** A symbolic index into the 3-value pool. */
    private static int index(String name) {
        int v = Debug.makeSymbolicInteger(name);
        Verify.ignoreIf(v < 0 || v > 2);
        return v;
    }
}
