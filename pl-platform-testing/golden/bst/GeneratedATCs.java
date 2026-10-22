package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/BinarySearchTree.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_Keys = new int[3];
        int m0_size = 0;
        int m0_minKey = 0;

        // call 0: insert
        int c0_key = index("c0_key");
        // requires: !Keys.contains(key)
        Verify.ignoreIf(!(!((m0_Keys[c0_key] == 1))));
        int[] m1_Keys = { flag("m1_Keys_0"), flag("m1_Keys_1"), flag("m1_Keys_2") };
        Verify.ignoreIf(!(c0_key == 0 || m1_Keys[0] == m0_Keys[0]));
        Verify.ignoreIf(!(c0_key == 1 || m1_Keys[1] == m0_Keys[1]));
        Verify.ignoreIf(!(c0_key == 2 || m1_Keys[2] == m0_Keys[2]));
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        // ensures: Keys.contains(key)
        Verify.ignoreIf(!((m1_Keys[c0_key] == 1)));
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m1_size == (m0_size + 1))));

        // call 1: insert
        int c1_key = index("c1_key");
        // requires: !Keys.contains(key)
        Verify.ignoreIf(!(!((m1_Keys[c1_key] == 1))));
        int[] m2_Keys = { flag("m2_Keys_0"), flag("m2_Keys_1"), flag("m2_Keys_2") };
        Verify.ignoreIf(!(c1_key == 0 || m2_Keys[0] == m1_Keys[0]));
        Verify.ignoreIf(!(c1_key == 1 || m2_Keys[1] == m1_Keys[1]));
        Verify.ignoreIf(!(c1_key == 2 || m2_Keys[2] == m1_Keys[2]));
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        // ensures: Keys.contains(key)
        Verify.ignoreIf(!((m2_Keys[c1_key] == 1)));
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m2_size == (m1_size + 1))));

        // call 2: insert
        int c2_key = index("c2_key");
        // requires: !Keys.contains(key)
        Verify.ignoreIf(!(!((m2_Keys[c2_key] == 1))));
        int[] m3_Keys = { flag("m3_Keys_0"), flag("m3_Keys_1"), flag("m3_Keys_2") };
        Verify.ignoreIf(!(c2_key == 0 || m3_Keys[0] == m2_Keys[0]));
        Verify.ignoreIf(!(c2_key == 1 || m3_Keys[1] == m2_Keys[1]));
        Verify.ignoreIf(!(c2_key == 2 || m3_Keys[2] == m2_Keys[2]));
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: Keys.contains(key)
        Verify.ignoreIf(!((m3_Keys[c2_key] == 1)));
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m3_size == (m2_size + 1))));

        // call 3: contains
        int c3_key = index("c3_key");
        // requires: size > 0
        Verify.ignoreIf(!((m3_size > 0)));
        boolean c3_result = Debug.makeSymbolicBoolean("c3_result");
        int[] m4_Keys = { flag("m4_Keys_0"), flag("m4_Keys_1"), flag("m4_Keys_2") };
        Verify.ignoreIf(!(c3_key == 0 || m4_Keys[0] == m3_Keys[0]));
        Verify.ignoreIf(!(c3_key == 1 || m4_Keys[1] == m3_Keys[1]));
        Verify.ignoreIf(!(c3_key == 2 || m4_Keys[2] == m3_Keys[2]));
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        // ensures: \result == found  (only names the result)
        // ensures: found == Keys.contains(key)
        Verify.ignoreIf(!((c3_result == (m4_Keys[c3_key] == 1))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m4_size == m3_size)));

        // call 4: min
        // requires: size > 0
        Verify.ignoreIf(!((m4_size > 0)));
        int c4_result = Debug.makeSymbolicInteger("c4_result");
        int m5_size = Debug.makeSymbolicInteger("m5_size");
        int m5_minKey = Debug.makeSymbolicInteger("m5_minKey");
        // ensures: \result == smallest  (only names the result)
        // ensures: smallest == minKey
        Verify.ignoreIf(!((c4_result == m5_minKey)));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m5_size == m4_size)));

        // call 5: delete
        int c5_key = index("c5_key");
        // requires: Keys.contains(key)
        Verify.ignoreIf(!((m4_Keys[c5_key] == 1)));
        int[] m6_Keys = { flag("m6_Keys_0"), flag("m6_Keys_1"), flag("m6_Keys_2") };
        Verify.ignoreIf(!(c5_key == 0 || m6_Keys[0] == m4_Keys[0]));
        Verify.ignoreIf(!(c5_key == 1 || m6_Keys[1] == m4_Keys[1]));
        Verify.ignoreIf(!(c5_key == 2 || m6_Keys[2] == m4_Keys[2]));
        int m6_size = Debug.makeSymbolicInteger("m6_size");
        // ensures: !Keys.contains(key)
        Verify.ignoreIf(!(!((m6_Keys[c5_key] == 1))));
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m6_size == (m5_size - 1))));
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
