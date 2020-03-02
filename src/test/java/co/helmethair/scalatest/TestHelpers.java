package co.helmethair.scalatest;

import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public interface TestHelpers {
    UniqueId engineId = UniqueId.forEngine("scalatest");

    ScalatestEngine engine = new ScalatestEngine();

    @SuppressWarnings("unchecked")
    default EngineDiscoveryRequest createClassDiscoveryRequest(String... className) {
        return new EngineDiscoveryRequest() {
            @Override
            public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
                if (selectorType == ClassSelector.class) {
                    return ((List<T>) Arrays.stream(className).map(DiscoverySelectors::selectClass).collect(Collectors.toList()));
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
    }

    default void verifyTestStartReported(String testIdSuffix, TestEngineExecutionListener listener) {
        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdSuffix))
        );
    }

    default void verifyTestStartNotReported(String testIdSuffix, TestEngineExecutionListener listener) {
        verify(listener, never()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdSuffix))
        );
    }

    default void verifyTestFailReported(String testIdsuffix, TestEngineExecutionListener listener) {
        verifyTestFailReportedWith(testIdsuffix, listener, null);
    }

    default void verifyTestFailReportedWith(String testIdsuffix, TestEngineExecutionListener listener, Class<? extends Throwable> cause) {
        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdsuffix)),
                argThat(a -> a.getThrowable().isPresent()
                        && (cause == null || cause.isInstance(a.getThrowable().get()))
                        && a.getStatus() == TestExecutionResult.Status.FAILED
                )
        );
    }

    default void verifyTestSkipReported(String testIdsuffix, TestEngineExecutionListener listener) {
        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdsuffix)),
                argThat(a -> a.getStatus() == TestExecutionResult.Status.ABORTED)
        );
    }

    default void verifyTestSuccessReported(String testIdsuffix, TestEngineExecutionListener listener) {
        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdsuffix)),
                argThat(a -> !a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.SUCCESSFUL
                )
        );
    }
}
