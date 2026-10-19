package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    private static final String[] POOL_String = {"s1", "s2", "s3"};

    public static void main(String[] args) {
        Helper.reset();

        // call 0: add
        int c0_elem$idx = Debug.makeSymbolicInteger("c0_elem$idx");
        Verify.ignoreIf(!(c0_elem$idx >= 0 && c0_elem$idx < 3));
        String c0_elem = POOL_String[c0_elem$idx];
        Verify.ignoreIf(!(c0_elem != null));
        boolean c0_result = Helper.add(c0_elem);

        // call 1: add
        int c1_elem$idx = Debug.makeSymbolicInteger("c1_elem$idx");
        Verify.ignoreIf(!(c1_elem$idx >= 0 && c1_elem$idx < 3));
        String c1_elem = POOL_String[c1_elem$idx];
        Verify.ignoreIf(!(c1_elem != null));
        boolean c1_result = Helper.add(c1_elem);

        // call 2: contains
        int c2_elem$idx = Debug.makeSymbolicInteger("c2_elem$idx");
        Verify.ignoreIf(!(c2_elem$idx >= 0 && c2_elem$idx < 3));
        String c2_elem = POOL_String[c2_elem$idx];
        Verify.ignoreIf(!(c2_elem != null));
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);

        // call 3: remove
        int c3_elem$idx = Debug.makeSymbolicInteger("c3_elem$idx");
        Verify.ignoreIf(!(c3_elem$idx >= 0 && c3_elem$idx < 3));
        String c3_elem = POOL_String[c3_elem$idx];
        Verify.ignoreIf(!(Helper.E.contains(c3_elem)));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
    }
}
