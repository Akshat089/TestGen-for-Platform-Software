package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/LruCache.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_C$has = new int[3];
        int[] m0_C$val = new int[3];
        int[] m0_Recency = new int[3];
        int m0_mostRecent = 0;
        int m0_mostRecent$null = 1;
        int m0_capacity = 2;
        int m0_size = 0;

        // call 0: put
        int c0_key = index("c0_key");
        int c0_value = index("c0_value");
        // requires: key != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: capacity > 0
        Verify.ignoreIf(!((m0_capacity > 0)));
        int c0_result = index("c0_result");
        int c0_result$null = flag("c0_result$null");
        int[] m1_C$has = { flag("m1_C$has_0"), flag("m1_C$has_1"), flag("m1_C$has_2") };
        int[] m1_C$val = { index("m1_C$val_0"), index("m1_C$val_1"), index("m1_C$val_2") };
        Verify.ignoreIf(!(c0_key == 0 || m1_C$has[0] == m0_C$has[0]));
        Verify.ignoreIf(!(c0_key == 1 || m1_C$has[1] == m0_C$has[1]));
        Verify.ignoreIf(!(c0_key == 2 || m1_C$has[2] == m0_C$has[2]));
        Verify.ignoreIf(!(c0_key == 0 || m1_C$val[0] == m0_C$val[0]));
        Verify.ignoreIf(!(c0_key == 1 || m1_C$val[1] == m0_C$val[1]));
        Verify.ignoreIf(!(c0_key == 2 || m1_C$val[2] == m0_C$val[2]));
        int m1_mostRecent = index("m1_mostRecent");
        int m1_mostRecent$null = flag("m1_mostRecent$null");
        int m1_capacity = Debug.makeSymbolicInteger("m1_capacity");
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        // ensures: \result == evictedKey  (only names the result)
        // ensures: C.get(key) == value
        Verify.ignoreIf(!(((1 - m1_C$has[c0_key]) == 0 && m1_C$val[c0_key] == c0_value)));
        // ensures: mostRecent == key
        Verify.ignoreIf(!((m1_mostRecent$null == 0 && m1_mostRecent == c0_key)));
        // ensures: size <= capacity
        Verify.ignoreIf(!((m1_size <= m1_capacity)));

        // call 1: put
        int c1_key = index("c1_key");
        int c1_value = index("c1_value");
        // requires: key != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: capacity > 0
        Verify.ignoreIf(!((m1_capacity > 0)));
        int c1_result = index("c1_result");
        int c1_result$null = flag("c1_result$null");
        int[] m2_C$has = { flag("m2_C$has_0"), flag("m2_C$has_1"), flag("m2_C$has_2") };
        int[] m2_C$val = { index("m2_C$val_0"), index("m2_C$val_1"), index("m2_C$val_2") };
        Verify.ignoreIf(!(c1_key == 0 || m2_C$has[0] == m1_C$has[0]));
        Verify.ignoreIf(!(c1_key == 1 || m2_C$has[1] == m1_C$has[1]));
        Verify.ignoreIf(!(c1_key == 2 || m2_C$has[2] == m1_C$has[2]));
        Verify.ignoreIf(!(c1_key == 0 || m2_C$val[0] == m1_C$val[0]));
        Verify.ignoreIf(!(c1_key == 1 || m2_C$val[1] == m1_C$val[1]));
        Verify.ignoreIf(!(c1_key == 2 || m2_C$val[2] == m1_C$val[2]));
        int m2_mostRecent = index("m2_mostRecent");
        int m2_mostRecent$null = flag("m2_mostRecent$null");
        int m2_capacity = Debug.makeSymbolicInteger("m2_capacity");
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        // ensures: \result == evictedKey  (only names the result)
        // ensures: C.get(key) == value
        Verify.ignoreIf(!(((1 - m2_C$has[c1_key]) == 0 && m2_C$val[c1_key] == c1_value)));
        // ensures: mostRecent == key
        Verify.ignoreIf(!((m2_mostRecent$null == 0 && m2_mostRecent == c1_key)));
        // ensures: size <= capacity
        Verify.ignoreIf(!((m2_size <= m2_capacity)));

        // call 2: put
        int c2_key = index("c2_key");
        int c2_value = index("c2_value");
        // requires: key != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: capacity > 0
        Verify.ignoreIf(!((m2_capacity > 0)));
        int c2_result = index("c2_result");
        int c2_result$null = flag("c2_result$null");
        int[] m3_C$has = { flag("m3_C$has_0"), flag("m3_C$has_1"), flag("m3_C$has_2") };
        int[] m3_C$val = { index("m3_C$val_0"), index("m3_C$val_1"), index("m3_C$val_2") };
        Verify.ignoreIf(!(c2_key == 0 || m3_C$has[0] == m2_C$has[0]));
        Verify.ignoreIf(!(c2_key == 1 || m3_C$has[1] == m2_C$has[1]));
        Verify.ignoreIf(!(c2_key == 2 || m3_C$has[2] == m2_C$has[2]));
        Verify.ignoreIf(!(c2_key == 0 || m3_C$val[0] == m2_C$val[0]));
        Verify.ignoreIf(!(c2_key == 1 || m3_C$val[1] == m2_C$val[1]));
        Verify.ignoreIf(!(c2_key == 2 || m3_C$val[2] == m2_C$val[2]));
        int m3_mostRecent = index("m3_mostRecent");
        int m3_mostRecent$null = flag("m3_mostRecent$null");
        int m3_capacity = Debug.makeSymbolicInteger("m3_capacity");
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == evictedKey  (only names the result)
        // ensures: C.get(key) == value
        Verify.ignoreIf(!(((1 - m3_C$has[c2_key]) == 0 && m3_C$val[c2_key] == c2_value)));
        // ensures: mostRecent == key
        Verify.ignoreIf(!((m3_mostRecent$null == 0 && m3_mostRecent == c2_key)));
        // ensures: size <= capacity
        Verify.ignoreIf(!((m3_size <= m3_capacity)));

        // call 3: get
        int c3_key = index("c3_key");
        // requires: C.containsKey(key)
        Verify.ignoreIf(!((m3_C$has[c3_key] == 1)));
        int c3_result = index("c3_result");
        int c3_result$null = flag("c3_result$null");
        int[] m4_C$has = { flag("m4_C$has_0"), flag("m4_C$has_1"), flag("m4_C$has_2") };
        int[] m4_C$val = { index("m4_C$val_0"), index("m4_C$val_1"), index("m4_C$val_2") };
        Verify.ignoreIf(!(c3_key == 0 || m4_C$has[0] == m3_C$has[0]));
        Verify.ignoreIf(!(c3_key == 1 || m4_C$has[1] == m3_C$has[1]));
        Verify.ignoreIf(!(c3_key == 2 || m4_C$has[2] == m3_C$has[2]));
        Verify.ignoreIf(!(c3_key == 0 || m4_C$val[0] == m3_C$val[0]));
        Verify.ignoreIf(!(c3_key == 1 || m4_C$val[1] == m3_C$val[1]));
        Verify.ignoreIf(!(c3_key == 2 || m4_C$val[2] == m3_C$val[2]));
        int m4_mostRecent = index("m4_mostRecent");
        int m4_mostRecent$null = flag("m4_mostRecent$null");
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        // ensures: \result == cachedValue  (only names the result)
        // ensures: cachedValue == C.get(key)
        Verify.ignoreIf(!(((c3_result$null == 1 && (1 - m4_C$has[c3_key]) == 1) || (c3_result$null == 0 && (1 - m4_C$has[c3_key]) == 0 && c3_result == m4_C$val[c3_key]))));
        // ensures: mostRecent == key
        Verify.ignoreIf(!((m4_mostRecent$null == 0 && m4_mostRecent == c3_key)));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m4_size == m3_size)));

        // call 4: restore
        int c4_evictedKey = index("c4_evictedKey");
        int c4_value = index("c4_value");
        // requires: evictedKey != null
        Verify.ignoreIf(!(!(0 == 1)));
        // requires: !C.containsKey(evictedKey)
        Verify.ignoreIf(!(!((m4_C$has[c4_evictedKey] == 1))));
        int[] m5_C$has = { flag("m5_C$has_0"), flag("m5_C$has_1"), flag("m5_C$has_2") };
        int[] m5_C$val = { index("m5_C$val_0"), index("m5_C$val_1"), index("m5_C$val_2") };
        Verify.ignoreIf(!(c4_evictedKey == 0 || m5_C$has[0] == m4_C$has[0]));
        Verify.ignoreIf(!(c4_evictedKey == 1 || m5_C$has[1] == m4_C$has[1]));
        Verify.ignoreIf(!(c4_evictedKey == 2 || m5_C$has[2] == m4_C$has[2]));
        Verify.ignoreIf(!(c4_evictedKey == 0 || m5_C$val[0] == m4_C$val[0]));
        Verify.ignoreIf(!(c4_evictedKey == 1 || m5_C$val[1] == m4_C$val[1]));
        Verify.ignoreIf(!(c4_evictedKey == 2 || m5_C$val[2] == m4_C$val[2]));
        int m5_mostRecent = index("m5_mostRecent");
        int m5_mostRecent$null = flag("m5_mostRecent$null");
        int m5_capacity = Debug.makeSymbolicInteger("m5_capacity");
        int m5_size = Debug.makeSymbolicInteger("m5_size");
        // ensures: C.containsKey(evictedKey)
        Verify.ignoreIf(!((m5_C$has[c4_evictedKey] == 1)));
        // ensures: mostRecent == evictedKey
        Verify.ignoreIf(!((m5_mostRecent$null == 0 && m5_mostRecent == c4_evictedKey)));
        // ensures: size <= capacity
        Verify.ignoreIf(!((m5_size <= m5_capacity)));
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
