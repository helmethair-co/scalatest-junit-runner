package co.helmethair.scalatest;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class RunnerTest {

    @Test
    void failingTest() {
        UniqueId id = UniqueId.forEngine("scalatest");
        EngineDiscoveryRequest discoveryRequest = new EngineDiscoveryRequest() {
            @Override
            public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
                if (selectorType == ClassSelector.class) {
                    return (List<T>) Collections.singletonList(DiscoverySelectors.selectClass("tests.FailingTest"));
                }
                return null;
            }

            @Override
            public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
                return null;
            }

            @Override
            public ConfigurationParameters getConfigurationParameters() {
                return null;
            }
        };
        ScalatestEngine engine = new ScalatestEngine();
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, id);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);
        engine.execute(executionRequest);

        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().equals("[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 1fails]"))
        );

        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().equals("[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 1fails]")),
                argThat(a -> a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.FAILED
                )
        );

        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().equals("[engine:scalatest]/[suite:tests.FailingTest]/[test:FailingTest 2skips]")),
                argThat(a -> a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.ABORTED
                )
        );
    }

    @Test
    void passingTest() {
        UniqueId id = UniqueId.forEngine("scalatest");
        EngineDiscoveryRequest discoveryRequest = new EngineDiscoveryRequest() {
            @Override
            public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
                if (selectorType == ClassSelector.class) {
                    return (List<T>) Collections.singletonList(DiscoverySelectors.selectClass("tests.FunSpecTest"));
                }
                return null;
            }

            @Override
            public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
                return null;
            }

            @Override
            public ConfigurationParameters getConfigurationParameters() {
                return null;
            }
        };
        ScalatestEngine engine = new ScalatestEngine();
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, id);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);
        engine.execute(executionRequest);

        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().equals("[engine:scalatest]/[suite:tests.FunSpecTest]/[test:FunspecTest runs]"))
        );

        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().equals("[engine:scalatest]/[suite:tests.FunSpecTest]/[test:FunspecTest runs]")),
                argThat(a -> !a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.SUCCESSFUL
                )
        );
    }

    @Test
    void nestedTest() {
        UniqueId id = UniqueId.forEngine("scalatest");
        EngineDiscoveryRequest discoveryRequest = new EngineDiscoveryRequest() {
            @Override
            public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
                if (selectorType == ClassSelector.class) {
                    return (List<T>) Collections.singletonList(DiscoverySelectors.selectClass("tests.NestedTest"));
                }
                return null;
            }

            @Override
            public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
                return null;
            }

            @Override
            public ConfigurationParameters getConfigurationParameters() {
                return null;
            }
        };
        ScalatestEngine engine = new ScalatestEngine();
        TestDescriptor discoveredTests = engine.discover(discoveryRequest, id);
        TestEngineExecutionListener listener = spy(new TestEngineExecutionListener());
        ExecutionRequest executionRequest = new ExecutionRequest(discoveredTests, listener, null);
        engine.execute(executionRequest);

        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith("nested test1 runs]"))
        );

        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith("nested test2 runs]"))
        );

        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().endsWith("nested test1 runs]")),
                argThat(a -> !a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.SUCCESSFUL
                )
        );

        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().endsWith("nested test2 runs]")),
                argThat(a -> !a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.SUCCESSFUL
                )
        );
    }
}
