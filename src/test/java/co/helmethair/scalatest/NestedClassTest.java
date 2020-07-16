package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import static org.mockito.Mockito.spy;

class NestedClassTest implements TestHelpers {

    @Test
    void useNested() { // No test will be executed but this should not fail with a "Malformed class name" exception
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.TestOuter$Inner$CaseClass");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        verifyTestExecuteCode(0, () -> engine.execute(executionRequest));
    }
}
