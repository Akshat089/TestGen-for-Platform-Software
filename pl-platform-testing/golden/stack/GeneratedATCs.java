package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    private static final String[] POOL_String = {"s1", "s2", "s3"};

    public static void main(String[] args) {
        Helper.reset();

        // call 0: push
        int c0_elem$idx = Debug.makeSymbolicInteger("c0_elem$idx");
        Verify.ignoreIf(!(c0_elem$idx >= 0 && c0_elem$idx < 3));
        String c0_elem = POOL_String[c0_elem$idx];
        Verify.ignoreIf(!(c0_elem != null));
        int old0_size = Helper.size;
        Helper.push(c0_elem);

        // call 1: push
        int c1_elem$idx = Debug.makeSymbolicInteger("c1_elem$idx");
        Verify.ignoreIf(!(c1_elem$idx >= 0 && c1_elem$idx < 3));
        String c1_elem = POOL_String[c1_elem$idx];
        Verify.ignoreIf(!(c1_elem != null));
        int old1_size = Helper.size;
        Helper.push(c1_elem);

        // call 2: pop
        Verify.ignoreIf(!(Helper.size > 0));
        int old2_size = Helper.size;
        String c2_result = Helper.pop();

        // call 3: peek
        Verify.ignoreIf(!(Helper.size > 0));
        int old3_size = Helper.size;
        String c3_result = Helper.peek();
    }
}
