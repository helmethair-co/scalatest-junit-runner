package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestEngineExecutionListener;
import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.launcher.TestExecutionListener;

import java.util.Arrays;

import static org.mockito.Mockito.spy;

public class ClassInitializationErrorTest implements TestHelpers {
    @Test
    void failedToInitializeClass() {
        EngineDiscoveryRequest discoveryRequest = createClassDiscoveryRequest("tests.InitErrorTest");
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, engineId);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);

        engine.execute(executionRequest);

        String testId = "[engine:scalatest]/[failed:tests.InitErrorTest]";

        verifyTestStartReported(testId, listener);
        verifyTestFailReported(testId, listener);
    }

    @Test
    void failedToInitializeTaggedClass() {
        TestExecutionListener listener = spy(new TestExecutionListener() {
        });

        launch(Arrays.asList("tests.InitErrorTaggedTest"), Arrays.asList("co.helmethair.scalatest.SuitLevelTag"), Arrays.asList(), listener);

        String testId = "[engine:scalatest]/[failed:tests.InitErrorTaggedTest]";
        verifyTestStartReported(testId, listener);
        verifyTestFailReported(testId, listener);
    }
}
