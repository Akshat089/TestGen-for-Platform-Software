package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/HashMapLib.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_M$has = new int[3];
        int[] m0_M$val = new int[3];
        int m0_size = 0;

        // call 0: put
        int c0_key = index("c0_key");
        int c0_value = Debug.makeSymbolicInteger("c0_value");
        // requires: key != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int c0_result = Debug.makeSymbolicInteger("c0_result");
        int c0_result$null = flag("c0_result$null");
        int[] m1_M$has = { flag("m1_M$has_0"), flag("m1_M$has_1"), flag("m1_M$has_2") };
        int[] m1_M$val = { Debug.makeSymbolicInteger("m1_M$val_0"), Debug.makeSymbolicInteger("m1_M$val_1"), Debug.makeSymbolicInteger("m1_M$val_2") };
        Verify.ignoreIf(!(c0_key == 0 || m1_M$has[0] == m0_M$has[0]));
        Verify.ignoreIf(!(c0_key == 1 || m1_M$has[1] == m0_M$has[1]));
        Verify.ignoreIf(!(c0_key == 2 || m1_M$has[2] == m0_M$has[2]));
        Verify.ignoreIf(!(c0_key == 0 || m1_M$val[0] == m0_M$val[0]));
        Verify.ignoreIf(!(c0_key == 1 || m1_M$val[1] == m0_M$val[1]));
        Verify.ignoreIf(!(c0_key == 2 || m1_M$val[2] == m0_M$val[2]));
        // ensures: \result == oldVal  (only names the result)
        // ensures: M.get(key) == value
        Verify.ignoreIf(!(((1 - m1_M$has[c0_key]) == 0 && m1_M$val[c0_key] == c0_value)));

        // call 1: put
        int c1_key = index("c1_key");
        int c1_value = Debug.makeSymbolicInteger("c1_value");
        // requires: key != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int c1_result = Debug.makeSymbolicInteger("c1_result");
        int c1_result$null = flag("c1_result$null");
        int[] m2_M$has = { flag("m2_M$has_0"), flag("m2_M$has_1"), flag("m2_M$has_2") };
        int[] m2_M$val = { Debug.makeSymbolicInteger("m2_M$val_0"), Debug.makeSymbolicInteger("m2_M$val_1"), Debug.makeSymbolicInteger("m2_M$val_2") };
        Verify.ignoreIf(!(c1_key == 0 || m2_M$has[0] == m1_M$has[0]));
        Verify.ignoreIf(!(c1_key == 1 || m2_M$has[1] == m1_M$has[1]));
        Verify.ignoreIf(!(c1_key == 2 || m2_M$has[2] == m1_M$has[2]));
        Verify.ignoreIf(!(c1_key == 0 || m2_M$val[0] == m1_M$val[0]));
        Verify.ignoreIf(!(c1_key == 1 || m2_M$val[1] == m1_M$val[1]));
        Verify.ignoreIf(!(c1_key == 2 || m2_M$val[2] == m1_M$val[2]));
        // ensures: \result == oldVal  (only names the result)
        // ensures: M.get(key) == value
        Verify.ignoreIf(!(((1 - m2_M$has[c1_key]) == 0 && m2_M$val[c1_key] == c1_value)));

        // call 2: getOldValue
        int c2_key = index("c2_key");
        // requires: M.containsKey(key)
        Verify.ignoreIf(!((m2_M$has[c2_key] == 1)));
        int c2_result = Debug.makeSymbolicInteger("c2_result");
        int c2_result$null = flag("c2_result$null");
        int[] m3_M$has = { flag("m3_M$has_0"), flag("m3_M$has_1"), flag("m3_M$has_2") };
        int[] m3_M$val = { Debug.makeSymbolicInteger("m3_M$val_0"), Debug.makeSymbolicInteger("m3_M$val_1"), Debug.makeSymbolicInteger("m3_M$val_2") };
        Verify.ignoreIf(!(c2_key == 0 || m3_M$has[0] == m2_M$has[0]));
        Verify.ignoreIf(!(c2_key == 1 || m3_M$has[1] == m2_M$has[1]));
        Verify.ignoreIf(!(c2_key == 2 || m3_M$has[2] == m2_M$has[2]));
        Verify.ignoreIf(!(c2_key == 0 || m3_M$val[0] == m2_M$val[0]));
        Verify.ignoreIf(!(c2_key == 1 || m3_M$val[1] == m2_M$val[1]));
        Verify.ignoreIf(!(c2_key == 2 || m3_M$val[2] == m2_M$val[2]));
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == curVal  (only names the result)
        // ensures: curVal == M.get(key)
        Verify.ignoreIf(!(((c2_result$null == 1 && (1 - m3_M$has[c2_key]) == 1) || (c2_result$null == 0 && (1 - m3_M$has[c2_key]) == 0 && c2_result == m3_M$val[c2_key]))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m3_size == m0_size)));

        // call 3: remove
        int c3_key = index("c3_key");
        // requires: M.containsKey(key)
        Verify.ignoreIf(!((m3_M$has[c3_key] == 1)));
        int[] m4_M$has = { flag("m4_M$has_0"), flag("m4_M$has_1"), flag("m4_M$has_2") };
        int[] m4_M$val = { Debug.makeSymbolicInteger("m4_M$val_0"), Debug.makeSymbolicInteger("m4_M$val_1"), Debug.makeSymbolicInteger("m4_M$val_2") };
        Verify.ignoreIf(!(c3_key == 0 || m4_M$has[0] == m3_M$has[0]));
        Verify.ignoreIf(!(c3_key == 1 || m4_M$has[1] == m3_M$has[1]));
        Verify.ignoreIf(!(c3_key == 2 || m4_M$has[2] == m3_M$has[2]));
        Verify.ignoreIf(!(c3_key == 0 || m4_M$val[0] == m3_M$val[0]));
        Verify.ignoreIf(!(c3_key == 1 || m4_M$val[1] == m3_M$val[1]));
        Verify.ignoreIf(!(c3_key == 2 || m4_M$val[2] == m3_M$val[2]));
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        // ensures: !M.containsKey(key)
        Verify.ignoreIf(!(!((m4_M$has[c3_key] == 1))));
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m4_size == (m3_size - 1))));
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
