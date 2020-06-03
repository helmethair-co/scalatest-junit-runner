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

public class IgnoredTest implements TestHelpers {
    @Test
    void beforeAllAndAfterEachCalledTest() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.IgnoredTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        Map<String, Integer> calls = new HashMap<String, Integer>() {{
         //   put("not ignored", 1);
           // put("ignored", 0);
        }};

        verifyTestExecuteCode(calls, () -> engine.execute(executionRequest));
    }
}
