package co.helmethair.scalatest;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.reporter.JUnitReporter;
import co.helmethair.scalatest.runtime.Discovery;
import co.helmethair.scalatest.runtime.Executor;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.scalatest.Suite;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScalatestEngine implements TestEngine {
    public static final String ID = "scalatest";
    final static String SCALATEST_PREFIX = "org.scalatest";
    public static String PARAMETER_SKIP_AFTER_FAIL = "scalatest.junit.skip_after_fail";
    public static boolean DEFAULT_SKIP_AFTER_FAIL = false;
    Discovery runtime = new Discovery();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        ScalatestEngineDescriptor engineDescriptor = new ScalatestEngineDescriptor(uniqueId, ID);

        List<Class<? extends Suite>> classes = discoverClassSelectors(discoveryRequest, uniqueId);

        return runtime.discover(engineDescriptor, classes, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void execute(ExecutionRequest executionRequest) {
        Optional<ConfigurationParameters> params = Optional.ofNullable(executionRequest.getConfigurationParameters());
        boolean skipAfterFail = params.map(p -> p.getBoolean(PARAMETER_SKIP_AFTER_FAIL)
                .orElse(DEFAULT_SKIP_AFTER_FAIL)).orElse(DEFAULT_SKIP_AFTER_FAIL);
        Executor executor = new Executor(skipAfterFail);
        JUnitReporter reporter = new JUnitReporter(executionRequest.getEngineExecutionListener(),
                executionRequest.getRootTestDescriptor());

        executor.executeTest(executionRequest.getRootTestDescriptor(), reporter);
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends Suite>> discoverClassSelectors(EngineDiscoveryRequest dicoveryRequest, UniqueId uniqueId) {
        return dicoveryRequest.getSelectorsByType(ClassSelector.class).stream().filter(selector -> {
            try {
                // get all super classes
                LinkedList<String> superClasses = new LinkedList<>();
                Class<?> current = selector.getJavaClass().getSuperclass();
                while (current != null) {
                    superClasses.add(current.getName());
                    current = current.getSuperclass();
                }

                return superClasses.stream().anyMatch(c -> c.startsWith(SCALATEST_PREFIX));
            } catch (Throwable e) {
                dicoveryRequest.getDiscoveryListener().selectorProcessed(uniqueId, selector, SelectorResolutionResult.failed(e));
                throw e;
            }
        }).filter(c -> !(c.getJavaClass().isAnonymousClass()
                || c.getJavaClass().isLocalClass()
                || c.getJavaClass().isSynthetic()
                || Modifier.isAbstract(c.getJavaClass().getModifiers()))
                && Suite.class.isAssignableFrom(c.getJavaClass())
        ).map(c -> ((Class<? extends Suite>) c.getJavaClass())).collect(Collectors.toList());
    }
}
