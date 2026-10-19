package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    private static final String[] POOL_String = {"s1", "s2", "s3"};

    public static void main(String[] args) {
        Helper.reset();

        // call 0: addVertex
        int c0_name$idx = Debug.makeSymbolicInteger("c0_name$idx");
        Verify.ignoreIf(!(c0_name$idx >= 0 && c0_name$idx < 3));
        String c0_name = POOL_String[c0_name$idx];
        Verify.ignoreIf(!(c0_name != null));
        int old0_edgeCount = Helper.edgeCount;
        Helper.addVertex(c0_name);

        // call 1: addVertex
        int c1_name$idx = Debug.makeSymbolicInteger("c1_name$idx");
        Verify.ignoreIf(!(c1_name$idx >= 0 && c1_name$idx < 3));
        String c1_name = POOL_String[c1_name$idx];
        Verify.ignoreIf(!(c1_name != null));
        int old1_edgeCount = Helper.edgeCount;
        Helper.addVertex(c1_name);

        // call 2: addEdge
        int c2_from$idx = Debug.makeSymbolicInteger("c2_from$idx");
        Verify.ignoreIf(!(c2_from$idx >= 0 && c2_from$idx < 3));
        String c2_from = POOL_String[c2_from$idx];
        int c2_to$idx = Debug.makeSymbolicInteger("c2_to$idx");
        Verify.ignoreIf(!(c2_to$idx >= 0 && c2_to$idx < 3));
        String c2_to = POOL_String[c2_to$idx];
        Verify.ignoreIf(!(Helper.V.contains(c2_from)));
        Verify.ignoreIf(!(Helper.V.contains(c2_to)));
        int old2_edgeCount = Helper.edgeCount;
        Helper.addEdge(c2_from, c2_to);

        // call 3: degree
        int c3_name$idx = Debug.makeSymbolicInteger("c3_name$idx");
        Verify.ignoreIf(!(c3_name$idx >= 0 && c3_name$idx < 3));
        String c3_name = POOL_String[c3_name$idx];
        Verify.ignoreIf(!(Helper.V.contains(c3_name)));
        int old3_edgeCount = Helper.edgeCount;
        int c3_result = Helper.degree(c3_name);

        // call 4: hasEdge
        int c4_from$idx = Debug.makeSymbolicInteger("c4_from$idx");
        Verify.ignoreIf(!(c4_from$idx >= 0 && c4_from$idx < 3));
        String c4_from = POOL_String[c4_from$idx];
        int c4_to$idx = Debug.makeSymbolicInteger("c4_to$idx");
        Verify.ignoreIf(!(c4_to$idx >= 0 && c4_to$idx < 3));
        String c4_to = POOL_String[c4_to$idx];
        Verify.ignoreIf(!(Helper.V.contains(c4_from)));
        Verify.ignoreIf(!(Helper.V.contains(c4_to)));
        int old4_edgeCount = Helper.edgeCount;
        boolean c4_result = Helper.hasEdge(c4_from, c4_to);
    }
}
