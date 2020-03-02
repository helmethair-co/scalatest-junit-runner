package co.helmethair.scalatest.descriptor;

import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.util.Optional;

public class ScalatestTestDescriptor extends ScalatestDescriptor {
    private final ScalatestSuiteDescriptor containingSuite;
    private final String testName;

    public ScalatestTestDescriptor(ScalatestSuiteDescriptor containingSuite, String testName) {
        super(testId(containingSuite.getSuiteId(), testName));
        this.containingSuite = containingSuite;
        this.testName = testName;
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

    public ScalatestSuiteDescriptor getContainingSuite() {
        return containingSuite;
    }
}
