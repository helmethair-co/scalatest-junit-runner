package co.helmethair.scalatest.runtime;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestFailedInitDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestSuiteDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestTestDescriptor;
import co.helmethair.scalatest.reporter.JUnitReporter;
import co.helmethair.scalatest.scala.ScalaConversions;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.scalatest.*;
import org.scalatest.events.Ordinal;
import scala.Option;

import java.util.Collections;
import java.util.Comparator;

public class Executor {

    private static final ConfigMap emptyConfigMap = ConfigMap$.MODULE$.empty();
    private static final scala.collection.immutable.Set<String> chosenStyles = ScalaConversions.asScalaSet(Collections.<String>emptySet()).toSet();
    private boolean skipAfterFail;

    public Executor() {
        skipAfterFail = false;
    }
    public Executor(boolean skipAfterFail) {
        this.skipAfterFail = skipAfterFail;
    }

    public void executeTest(TestDescriptor test, JUnitReporter reporter) {
        try {
            if (skipAfterFail && reporter.getSkipWithCause() != null) {
                reporter.getJunitListener().executionFinished(test, TestExecutionResult.aborted(reporter.getSkipWithCause()));
                return;
            }
            if (test instanceof ScalatestSuiteDescriptor) {
                if (test.getChildren().isEmpty()) {
                    //if there are no children try to execute it with Scalatest native executor (no reporting)
                    ((ScalatestSuiteDescriptor) test).getScalasuite().execute(
                            null, emptyConfigMap, true, false, false, false, false);
                } else {
                    executeSuite(test, reporter);
                }
            } else if (test instanceof ScalatestTestDescriptor) {
                runScalatest(((ScalatestTestDescriptor) test), reporter);
            } else if (test instanceof ScalatestEngineDescriptor) {
                executeSuite(test, reporter);
            } else if (test instanceof ScalatestFailedInitDescriptor) {
                reporter.getJunitListener().executionStarted(test);
                reporter.getJunitListener().executionFinished(test, TestExecutionResult.failed(((ScalatestFailedInitDescriptor) test).getCause()));
            }
        } catch (Throwable e) {
            reporter.getJunitListener().executionFinished(test, TestExecutionResult.failed(e));
        }
    }

    private void executeSuite(TestDescriptor test, JUnitReporter reporter) {
        reporter.getJunitListener().executionStarted(test);
        test.getChildren().stream()
                .sorted(Comparator.comparing(TestDescriptor::getDisplayName))
                .forEach(c -> executeTest(c, reporter));
        reporter.getJunitListener().executionFinished(test, TestExecutionResult.successful());
    }

    private void runScalatest(ScalatestTestDescriptor test, JUnitReporter reporter) {
        Status status = test.getContainingSuite().getScalasuite().runTest(test.getTestName(), createArgs(reporter));
        status.waitUntilCompleted();
    }

    private Args createArgs(JUnitReporter reporter) {
        Filter filter = Filter$.MODULE$.apply(
                Filter$.MODULE$.apply$default$1(),
                Filter$.MODULE$.apply$default$2(),
                Filter$.MODULE$.apply$default$3(),
                Filter$.MODULE$.apply$default$4()
        );

        return new Args(reporter, new StopperImpl(), filter, emptyConfigMap,
                Option.apply(null), new Tracker(new Ordinal(0)), chosenStyles, false,
                Option.apply(null), Option.apply(null));
    }
}
