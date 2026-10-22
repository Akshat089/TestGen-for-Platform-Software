package in.ac.iiitb.plproject.mutation;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runs in a CHILD JVM whose classpath holds one library build (correct or
 * mutated) followed by already-compiled cached test classes. Runs the named
 * test classes in order and prints one line per class:
 *
 * <pre>
 *   RESULT	&lt;class&gt;	PASS
 *   RESULT	&lt;class&gt;	FAIL	PRE|POST|EXCEPTION|TIMEOUT	&lt;message&gt;
 * </pre>
 *
 * With {@code --until-fail} it stops at the first failing class. It always
 * ends with {@code System.exit}, so a thread a timed-out test left looping
 * cannot keep the JVM alive.
 */
public final class MutantTestMain {

    private MutantTestMain() {}

    public static void main(String[] args) {
        boolean untilFail = false;
        int status = 0;
        for (String arg : args) {
            if (arg.equals("--until-fail")) {
                untilFail = true;
                continue;
            }
            String line;
            try {
                Class<?> cls = Class.forName(arg);
                Result result = new JUnitCore().run(Request.aClass(cls));
                if (result.wasSuccessful() && result.getRunCount() > 0) {
                    line = "RESULT\t" + arg + "\tPASS";
                } else if (result.getRunCount() == 0) {
                    line = "RESULT\t" + arg + "\tFAIL\tEXCEPTION\tno test ran";
                } else {
                    Failure f = result.getFailures().get(0);
                    line = "RESULT\t" + arg + "\tFAIL\t" + kindOf(f.getException()) + "\t" + oneLine(describe(f));
                }
            } catch (Throwable t) {
                line = "RESULT\t" + arg + "\tFAIL\tEXCEPTION\t" + oneLine(t.toString());
            }
            System.out.println(line);
            System.out.flush();
            if (untilFail && line.contains("\tFAIL\t")) break;
        }
        System.exit(status);
    }

    /** PRE/POST for a contract assertion, TIMEOUT for JUnit's time limit, EXCEPTION for anything thrown. */
    static String kindOf(Throwable t) {
        if (t instanceof AssertionError && t.getMessage() != null) {
            if (t.getMessage().startsWith("PRE ")) return "PRE";
            if (t.getMessage().startsWith("POST ")) return "POST";
        }
        if (t != null && t.getClass().getName().equals("org.junit.runners.model.TestTimedOutException")) {
            return "TIMEOUT";
        }
        return "EXCEPTION";
    }

    private static String describe(Failure f) {
        Throwable t = f.getException();
        if (t instanceof AssertionError && t.getMessage() != null) return t.getMessage();
        return t == null ? f.getMessage() : t.toString();
    }

    private static String oneLine(String s) {
        if (s == null) return "";
        return s.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
    }
}
