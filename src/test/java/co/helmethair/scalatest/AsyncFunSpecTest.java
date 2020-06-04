package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.spy;

public class AsyncFunSpecTest implements TestHelpers {
    @Test
    void areCalledAsynchronouslyTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.AsyncFunSpecTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        List<String> calls = Arrays.asList(
            "setup",
            "runs asynchronously",
            "runs again asynchronously",
            "cleanup"
        );

        verifyTestExecuteCode(calls, () -> engine.execute(executionRequest));
    }
}
