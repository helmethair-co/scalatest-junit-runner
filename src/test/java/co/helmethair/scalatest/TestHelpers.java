package co.helmethair.scalatest;

import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public interface TestHelpers {
    UniqueId engineId = UniqueId.forEngine("scalatest");

    ScalatestEngine engine = new ScalatestEngine();

    @SuppressWarnings("unchecked")
    default EngineDiscoveryRequest createClassDiscoveryRequest(String... classNames) {
        return new EngineDiscoveryRequest() {
            @Override
            public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
                if (selectorType == ClassSelector.class) {
                    return ((List<T>) Arrays.stream(classNames).map(DiscoverySelectors::selectClass).collect(Collectors.toList()));
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

    default Launcher createLauncher(TestExecutionListener listener) {
        LauncherConfig config = LauncherConfig.builder()
                .addTestEngines(new ScalatestEngine())
                .addTestExecutionListeners(listener)
                .enableTestEngineAutoRegistration(false)
                .enableTestExecutionListenerAutoRegistration(false)
                .build();

        return LauncherFactory.create(config);
    }

    default void launch(List<String> classNames, List<String> includeTags, List<String> excludeTags, TestExecutionListener listener) {
        List<PostDiscoveryFilter> filters = new ArrayList<>(2);
        if (!includeTags.isEmpty()) {
            filters.add(TagFilter.includeTags(includeTags));
        }
        if (!excludeTags.isEmpty()) {
            filters.add(TagFilter.excludeTags(excludeTags));
        }

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(classNames.stream().map(DiscoverySelectors::selectClass).collect(Collectors.toList()))
                .filters(filters.toArray(new PostDiscoveryFilter[0]))
                .build();

        Launcher launcher = createLauncher(listener);

        TestPlan discoveredPlan = launcher.discover(request);
        launcher.execute(discoveredPlan, listener);
    }

    default void verifyTestStartReported(String testIdSuffix, TestEngineExecutionListener listener) {
        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdSuffix))
        );
    }

    default void verifyTestStartReported(String testIdSuffix, TestExecutionListener listener) {
        verify(listener, atLeastOnce()).executionStarted(
                argThat(a -> a.getUniqueId().endsWith(testIdSuffix))
        );
    }

    default void verifyTestStartNotReported(String testIdSuffix, TestEngineExecutionListener listener) {
        verify(listener, never()).executionStarted(
                argThat(a -> a.getUniqueId().toString().endsWith(testIdSuffix))
        );
    }

    default void verifyTestStartNotReported(String testIdSuffix, TestExecutionListener listener) {
        verify(listener, never()).executionStarted(
                argThat(a -> a.getUniqueId().endsWith(testIdSuffix))
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

    default void verifyTestSuccessReported(String testIdsuffix, TestExecutionListener listener) {
        verify(listener, atLeastOnce()).executionFinished(
                argThat(a -> a.getUniqueId().endsWith(testIdsuffix)),
                argThat(a -> !a.getThrowable().isPresent()
                        && a.getStatus() == TestExecutionResult.Status.SUCCESSFUL
                )
        );
    }
}