package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.spy;

public class BeforeAfterTest implements TestHelpers {
    @Test
    void beforeAllAndAfterEachCalledTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.BeforeAndAfterEachTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        Map<String, Integer> calls = new HashMap<String, Integer>() {{
            put("beforeEach", 2);
            put("afterEach", 2);
            put("runs", 1);
            put("runs again", 1);
        }};

        verifyTestExecuteCode(calls, () -> engine.execute(executionRequest));
    }

    @Test
    void beforeAllAndAfterAllCalledTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.BeforeAndAfterAllTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        Map<String, Integer> calls = new HashMap<String, Integer>() {{
            put("beforeAll", 1);
            put("afterAll", 1);
            put("runs", 1);
            put("runs again", 1);
        }};

        verifyTestExecuteCode(calls, () -> engine.execute(executionRequest));
    }
}
