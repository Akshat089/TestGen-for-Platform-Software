package in.ac.iiitb.plproject.symbolic;

/**
 * Executes the ready prefix of t' symbolically and solves its path
 * constraint: one value for each input command in the prefix, in order, or
 * {@link Solution.Kind#NO_SOLUTION}.
 */
public interface SymexEngine {
    Solution solve(TestGen.Prefix prefix);
}
