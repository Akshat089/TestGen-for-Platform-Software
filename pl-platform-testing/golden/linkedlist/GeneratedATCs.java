package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    public static void main(String[] args) {
        Helper.reset();

        // call 0: addFirst
        Integer c0_value = Debug.makeSymbolicInteger("c0_value");
        Debug.assume(c0_value != null);
        int old0_size = Helper.size;
        Helper.addFirst(c0_value);

        // call 1: addLast
        Integer c1_value = Debug.makeSymbolicInteger("c1_value");
        Debug.assume(c1_value != null);
        int old1_size = Helper.size;
        Helper.addLast(c1_value);

        // call 2: removeFirst
        Verify.ignoreIf(!(Helper.size > 0));
        Integer old2_head = Helper.head;
        int old2_size = Helper.size;
        Integer c2_result = Helper.removeFirst();

        // call 3: indexOf
        Integer c3_value = Debug.makeSymbolicInteger("c3_value");
        Verify.ignoreIf(!(Helper.size > 0));
        int c3_result = Helper.indexOf(c3_value);
    }
}
