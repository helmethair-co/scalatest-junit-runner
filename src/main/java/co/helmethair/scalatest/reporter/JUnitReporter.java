package co.helmethair.scalatest.reporter;

import co.helmethair.scalatest.descriptor.ScalatestDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestSuiteDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestTestDescriptor;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.scalatest.Reporter;
import org.scalatest.Suite;
import org.scalatest.events.*;

import java.util.Collections;
import java.util.Optional;

import static co.helmethair.scalatest.scala.OptionHelper.getOrElse;

public class JUnitReporter implements Reporter {
    private final EngineExecutionListener junitListener;
    private final TestDescriptor rootTestDescriptor;
    private volatile Throwable skipWithCause = null;

    public JUnitReporter(EngineExecutionListener junitListener, TestDescriptor rootTestDescriptor) {
        this.junitListener = junitListener;
        this.rootTestDescriptor = rootTestDescriptor;
    }

    @Override
    public void apply(Event event) {

        if (event instanceof TestStarting) {
            TestStarting e = (TestStarting) event;
            junitListener.executionStarted(
                    getOrCreateDescriptor(e.suiteId(), e.suiteName(), e.testName()));
        } else if (event instanceof TestCanceled) {
            TestCanceled e = (TestCanceled) event;
            junitListener.executionFinished(
                    getOrCreateDescriptor(e.suiteId(), e.suiteName(), e.testName()),
                    TestExecutionResult.aborted(getOrElse(e.throwable(), null)));
        } else if (event instanceof TestSucceeded) {
            TestSucceeded e = (TestSucceeded) event;
            junitListener.executionFinished(
                    getOrCreateDescriptor(e.suiteId(), e.suiteName(), e.testName()),
                    TestExecutionResult.successful());
        } else if (event instanceof TestFailed) {
            TestFailed e = (TestFailed) event;
            TestDescriptor descriptor = getOrCreateDescriptor(e.suiteId(), e.suiteName(), e.testName());
            Throwable cause = getOrElse(e.throwable(), null);
            setSkipWithCause(cause);
            junitListener.executionFinished(
                    descriptor,
                    TestExecutionResult.failed(cause));
        } else if (event instanceof RunAborted) {
            RunAborted e = (RunAborted) event;
            Throwable ex = getOrElse(e.throwable(), null);
            TestDescriptor testDescriptor = rootTestDescriptor;
            Object payload = e.payload().getOrElse(null);
            if (payload != null && payload instanceof Suite) {
                Suite s = (Suite) payload;
                testDescriptor = getOrCreateDescriptor(s.suiteId(), s.suiteName(), null);
            }
            junitListener.executionFinished(testDescriptor,
                    TestExecutionResult.failed(ex)); // tests reported as "aborted" does not fail the build
        } else if (event instanceof SuiteStarting) {
            SuiteStarting e = (SuiteStarting) event;
            junitListener.executionStarted(
                    getOrCreateDescriptor(e.suiteId(), e.suiteName(), null));
        } else if (event instanceof SuiteAborted) {
            SuiteAborted e = (SuiteAborted) event;
            Throwable ex = getOrElse(e.throwable(), null);
            TestDescriptor suiteDescriptor = getOrCreateDescriptor(e.suiteId(), e.suiteName(), null);
            junitListener.executionFinished(
                    suiteDescriptor,
                    TestExecutionResult.failed(ex));
        } else if (event instanceof SuiteCompleted) {
            SuiteCompleted e = (SuiteCompleted) event;
            junitListener.executionFinished(
                    getOrCreateDescriptor(e.suiteId(), e.suiteName(), null),
                    TestExecutionResult.successful());
        } else if (event instanceof RunStopped) {
            junitListener.executionFinished(rootTestDescriptor, TestExecutionResult.aborted(null));
        } else if (event instanceof RunCompleted) {
            junitListener.executionFinished(rootTestDescriptor, TestExecutionResult.successful());
        } else if (event instanceof TestIgnored) {
            TestIgnored e = (TestIgnored) event;
            junitListener.executionSkipped(getOrCreateDescriptor(e.suiteId(), e.suiteName(), e.testName()), "ignored");
        }
    }

    public Throwable getSkipWithCause() {
        return skipWithCause;
    }

    public void setSkipWithCause(Throwable skipWithCause) {
        this.skipWithCause = skipWithCause;
    }

    private TestDescriptor getOrCreateDescriptor(String suiteId, String suiteName, String testName) {
        Optional<ScalatestDescriptor> existingDescriptor = ScalatestDescriptor.find(suiteId, testName);
        return existingDescriptor.orElseGet(() -> {
            ScalatestDescriptor newDescriptor = null;
            if (testName != null) {
                ScalatestSuiteDescriptor suite = (ScalatestSuiteDescriptor) getOrCreateDescriptor(suiteId, suiteName, null);
                new ScalatestTestDescriptor(suite, testName, Collections.emptySet());
            } else {
                newDescriptor = new ScalatestSuiteDescriptor(null, suiteId, suiteName);
            }
            junitListener.dynamicTestRegistered(newDescriptor);
            return newDescriptor;
        });
    }

    public EngineExecutionListener getJunitListener() {
        return junitListener;
    }

    public TestDescriptor getRootTestDescriptor() {
        return rootTestDescriptor;
    }
}
