package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.spy;

public class RunnerTest implements TestHelpers {

    @Test
    void failingAndSkipTest() {
        ConfigurationParameters params = configurationParametersOf(new HashMap<String, Object>() {{
            put("scalatest.junit.skip_after_fail", true);
        }});
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest(params,"tests.FailingTest");

        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, params);

        verifyTestExecuteCode(0, ()->  engine.execute(executionRequest));

        String failingTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 1fails]";

        verifyTestStartReported(failingTestId, listener);
        verifyTestFailReported(failingTestId, listener);

        String skippedTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 2skips]";

        verifyTestStartNotReported(skippedTestId, listener);
        verifyTestSkipReported(skippedTestId, listener);
    }

    @Test
    void failingAndContinueTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.FailingTest");

        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        verifyTestExecuteCode(1, ()->  engine.execute(executionRequest));

        String failingTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 1fails]";

        verifyTestStartReported(failingTestId, listener);
        verifyTestFailReported(failingTestId, listener);

        String nonSkippedTestId = "[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 2skips]";

        verifyTestStartReported(nonSkippedTestId, listener);
        verifyTestSuccessReported(nonSkippedTestId, listener);
    }


    @Test
    void funSpecPassingTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.FunSpecTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        verifyTestExecuteCode(1, ()->  engine.execute(executionRequest));

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

        verifyTestExecuteCode(1, ()->  engine.execute(executionRequest));

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

        verifyTestExecuteCode(2, ()->  engine.execute(executionRequest));

        String nestedSuiteTestIdSuffix = "[engine:scalatest]/[suite:tests.NestedTest]";
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
