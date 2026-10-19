package in.ac.iiitb.plproject.check;

import java.util.Collections;
import java.util.List;

/**
 * Raised when the input checks (rule 13) reject a {@code .spec}/{@code .tests}
 * pair before anything is generated. Carries every problem found, each already
 * formatted as {@code file:line: message}, so a caller can print the whole list
 * rather than stopping at the first.
 */
public class GenatcInputException extends RuntimeException {

    private final List<String> problems;

    public GenatcInputException(List<String> problems) {
        super(problems.size() + " input check" + (problems.size() == 1 ? "" : "s")
                + " failed:\n  " + String.join("\n  ", problems));
        this.problems = Collections.unmodifiableList(problems);
    }

    public List<String> getProblems() { return problems; }
}
