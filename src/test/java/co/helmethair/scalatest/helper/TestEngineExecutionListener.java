package co.helmethair.scalatest.helper;

import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;

// This listener is intentionally empty as we will veryfy function calls with Mockito spy
public class TestEngineExecutionListener implements EngineExecutionListener {

    @Override
    public void dynamicTestRegistered(TestDescriptor testDescriptor) {
    }

    @Override
    public void executionSkipped(TestDescriptor testDescriptor, String reason) {
    }

    @Override
    public void executionStarted(TestDescriptor testDescriptor) {
    }

    @Override
    public void executionFinished(TestDescriptor testDescriptor, TestExecutionResult testExecutionResult) {
    }

    @Override
    public void reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
    }
}
