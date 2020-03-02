package co.helmethair.scalatest.descriptor;

import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.util.Optional;
import java.util.Set;

public class ScalatestTestDescriptor extends ScalatestDescriptor {
    private final ScalatestSuiteDescriptor containingSuite;
    private final String testName;
    private final Set<TestTag> tags;

    public ScalatestTestDescriptor(ScalatestSuiteDescriptor containingSuite, String testName, Set<TestTag> tags) {
        super(testId(containingSuite.getSuiteId(), testName));
        this.containingSuite = containingSuite;
        this.testName = testName;
        this.tags = tags;
    }

    @Override
    public String getDisplayName() {
        return testName;
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.of(MethodSource.from(containingSuite.getSuiteId(), testName));
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    public String getTestName() {
        return testName;
    }

    @Override
    public Set<TestTag> getTags() {
        return tags;
    }

    public ScalatestSuiteDescriptor getContainingSuite() {
        return containingSuite;
    }
}
