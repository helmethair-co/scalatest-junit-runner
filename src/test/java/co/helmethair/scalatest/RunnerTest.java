package co.helmethair.scalatest;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import static org.mockito.Mockito.spy;

public class RunnerTest implements TestHelpers {

    @Test
    void failingTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.FailingTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String failingTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 1fails]";

        verifyTestStartReported(failingTestId, listener);
        verifyTestFailReported(failingTestId, listener);

        String skippedTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 2skips]";

        verifyTestStartNotReported(skippedTestId, listener);
        verifyTestSkipReported(skippedTestId, listener);
    }

    @Test
    void funSpecPassingTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.FunSpecTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String failingTestId = "[engine:scalatest]/[suite:tests.FunSpecTest]/[test:FunspecTest runs]";

        verifyTestStartReported(failingTestId, listener);
        verifyTestSuccessReported(failingTestId, listener);
    }

    @Test
    void anyFunSpecPassingTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.AnyFunSpecTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String failingTestId = "[engine:scalatest]/[suite:tests.AnyFunSpecTest]/[test:AnyFunSpec runs]";

        verifyTestStartReported(failingTestId, listener);
        verifyTestSuccessReported(failingTestId, listener);
    }

    @Test
    void nestedTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.NestedTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String nestedSuiteTestIdSuffix = "nested test1 runs]";
        verifyTestStartReported(nestedSuiteTestIdSuffix, listener);
        verifyTestSuccessReported(nestedSuiteTestIdSuffix, listener);

        String nested1TestIdSuffix = "nested test1 runs]";
        verifyTestStartReported(nested1TestIdSuffix, listener);
        verifyTestSuccessReported(nested1TestIdSuffix, listener);

        String nested2TestIdSuffix = "nested test2 runs]";
        verifyTestStartReported(nested2TestIdSuffix, listener);
        verifyTestSuccessReported(nested2TestIdSuffix, listener);
    }
}
