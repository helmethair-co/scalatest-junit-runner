package co.helmethair.scalatest;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.reporter.JUnitReporter;
import co.helmethair.scalatest.runtime.Discovery;
import co.helmethair.scalatest.runtime.Executor;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.UniqueIdSelector;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.helmethair.scalatest.descriptor.ScalatestDescriptor.INIT_FAILURE_TYPE;
import static co.helmethair.scalatest.descriptor.ScalatestDescriptor.SUITE_TYPE;

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

        return runtime.discover(engineDescriptor,
            Stream.concat(
                discoverClassSelectors(discoveryRequest),
                discoverUniqueIdSelectors(discoveryRequest)
            ).collect(Collectors.toSet()),
            Thread.currentThread().getContextClassLoader());
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

    private Stream<String> discoverUniqueIdSelectors(EngineDiscoveryRequest discoveryRequest) {
        return discoveryRequest.getSelectorsByType(UniqueIdSelector.class).stream()
            .map(this::getSuite)
            .filter(Optional::isPresent)
            .map(Optional::get);
    }

    private Optional<String> getSuite(UniqueIdSelector u) {
        UniqueId uniqueId = u.getUniqueId();
        if (uniqueId.hasPrefix(UniqueId.forEngine(ID)) && uniqueId.getSegments().size() > 1) {
            UniqueId.Segment segment = uniqueId.getSegments().get(1);
            if (SUITE_TYPE.equals(segment.getType()) || INIT_FAILURE_TYPE.equals(segment.getType())) {
                return Optional.of(segment.getValue());
            }
        }
        return Optional.empty();
    }

    private Stream<String> discoverClassSelectors(EngineDiscoveryRequest discoveryRequest) {
        return discoveryRequest.getSelectorsByType(ClassSelector.class).stream()
            .map(ClassSelector::getClassName);
    }
}
