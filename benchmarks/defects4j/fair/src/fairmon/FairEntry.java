package fairmon;

import org.junit.runner.RunWith;

/**
 * The one class MutantTestMain is given in a fair run. Its runner runs the suite's
 * real test classes (named by the fair.classes system property) under the contract
 * monitor's listener; see {@link FairRunner}.
 */
@RunWith(FairRunner.class)
public class FairEntry {
}
