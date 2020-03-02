package co.helmethair.scalatest.descriptor;

import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.scalatest.Suite;

import java.util.Optional;

public class ScalatestSuiteDescriptor extends ScalatestDescriptor {
    private final Suite scalasuite;
    private final String suiteId;
    private final String suiteName;

    public ScalatestSuiteDescriptor(Suite scalasuite, String suiteId, String suiteName) {
        super(ScalatestDescriptor.containerId(suiteId));
        this.scalasuite = scalasuite;
        this.suiteId = suiteId;
        this.suiteName = suiteName;
    }

    @Override
    public String getDisplayName() {
        return suiteName;
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.of(ClassSource.from(suiteId));
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public Suite getScalasuite() {
        return scalasuite;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public String getSuiteId() {
        return suiteId;
    }
}
