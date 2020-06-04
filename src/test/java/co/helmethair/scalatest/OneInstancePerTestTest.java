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

public class OneInstancePerTestTest implements TestHelpers {
    @Test
    void areCalledFromOtherInstancesTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.OneInstancePerTestTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        List<String> calls = Arrays.asList("2", "3");

        verifyTestExecuteCode(calls, () -> engine.execute(executionRequest));
    }
}
