package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/StringSet.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_E = new int[3];
        int m0_size = 0;

        // call 0: add
        int c0_elem = index("c0_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        boolean c0_result = Debug.makeSymbolicBoolean("c0_result");
        int[] m1_E = { flag("m1_E_0"), flag("m1_E_1"), flag("m1_E_2") };
        Verify.ignoreIf(!(c0_elem == 0 || m1_E[0] == m0_E[0]));
        Verify.ignoreIf(!(c0_elem == 1 || m1_E[1] == m0_E[1]));
        Verify.ignoreIf(!(c0_elem == 2 || m1_E[2] == m0_E[2]));
        // ensures: \result == added  (only names the result)
        // ensures: E.contains(elem)
        Verify.ignoreIf(!((m1_E[c0_elem] == 1)));

        // call 1: add
        int c1_elem = index("c1_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        boolean c1_result = Debug.makeSymbolicBoolean("c1_result");
        int[] m2_E = { flag("m2_E_0"), flag("m2_E_1"), flag("m2_E_2") };
        Verify.ignoreIf(!(c1_elem == 0 || m2_E[0] == m1_E[0]));
        Verify.ignoreIf(!(c1_elem == 1 || m2_E[1] == m1_E[1]));
        Verify.ignoreIf(!(c1_elem == 2 || m2_E[2] == m1_E[2]));
        // ensures: \result == added  (only names the result)
        // ensures: E.contains(elem)
        Verify.ignoreIf(!((m2_E[c1_elem] == 1)));

        // call 2: contains
        int c2_elem = index("c2_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        boolean c2_result = Debug.makeSymbolicBoolean("c2_result");
        int[] m3_E = { flag("m3_E_0"), flag("m3_E_1"), flag("m3_E_2") };
        Verify.ignoreIf(!(c2_elem == 0 || m3_E[0] == m2_E[0]));
        Verify.ignoreIf(!(c2_elem == 1 || m3_E[1] == m2_E[1]));
        Verify.ignoreIf(!(c2_elem == 2 || m3_E[2] == m2_E[2]));
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == present  (only names the result)
        // ensures: present == E.contains(elem)
        Verify.ignoreIf(!((c2_result == (m3_E[c2_elem] == 1))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m3_size == m0_size)));

        // call 3: remove
        int c3_elem = index("c3_elem");
        // requires: E.contains(elem)
        Verify.ignoreIf(!((m3_E[c3_elem] == 1)));
        int[] m4_E = { flag("m4_E_0"), flag("m4_E_1"), flag("m4_E_2") };
        Verify.ignoreIf(!(c3_elem == 0 || m4_E[0] == m3_E[0]));
        Verify.ignoreIf(!(c3_elem == 1 || m4_E[1] == m3_E[1]));
        Verify.ignoreIf(!(c3_elem == 2 || m4_E[2] == m3_E[2]));
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        // ensures: !E.contains(elem)
        Verify.ignoreIf(!(!((m4_E[c3_elem] == 1))));
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
