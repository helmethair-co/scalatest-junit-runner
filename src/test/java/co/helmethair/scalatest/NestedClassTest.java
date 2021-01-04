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
    void handleNested() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.TestOuter$Inner$CaseClass", "tests.TestOuter$Inner$NotCase");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        verifyTestExecuteCode(1, () -> engine.execute(executionRequest));
    }
}
