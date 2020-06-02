package co.helmethair.scalatest;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.reporter.JUnitReporter;
import co.helmethair.scalatest.runtime.Discovery;
import co.helmethair.scalatest.runtime.Executor;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.scalatest.Suite;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScalatestEngine implements TestEngine {
    public static final String ID = "scalatest";
    public static final String PARAMETER_SKIP_AFTER_FAIL = "scalatest.junit.skip_after_fail";
    public static final boolean DEFAULT_SKIP_AFTER_FAIL = false;
    Discovery runtime = new Discovery();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        ScalatestEngineDescriptor engineDescriptor = new ScalatestEngineDescriptor(uniqueId, ID);

        List<Class<? extends Suite>> classes = discoverClassSelectors(discoveryRequest);

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
    private List<Class<? extends Suite>> discoverClassSelectors(EngineDiscoveryRequest dicoveryRequest) {
        return dicoveryRequest.getSelectorsByType(ClassSelector.class).stream().filter(c -> !(c.getJavaClass().isAnonymousClass()
                || c.getJavaClass().isLocalClass()
                || c.getJavaClass().isSynthetic()
                || Modifier.isAbstract(c.getJavaClass().getModifiers()))
                && Suite.class.isAssignableFrom(c.getJavaClass())
        ).map(c -> ((Class<? extends Suite>) c.getJavaClass())).collect(Collectors.toList());
    }
}
