package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;

import static org.mockito.Mockito.spy;

public class ClassRunningErrorTest implements TestHelpers {
    @Test
    void failedToInitializeClass() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.NoClassDefFoundErrorTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String testId = "[engine:scalatest]/[suite:tests.NoClassDefFoundErrorTest]";

        verifyTestStartReported(testId, listener);
        verifyTestStatusReported(testId, listener, TestExecutionResult.Status.FAILED);
    }
}
