package co.helmethair.scalatest.descriptor;

import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.util.Optional;
import java.util.Set;

public class ScalatestFailedInitDescriptor extends ScalatestDescriptor {
    private final Throwable cause;
    private final String suiteId;

    public ScalatestFailedInitDescriptor(Throwable cause, String name, Set<TestTag> tags) {
        super(ENGINE_ID.append(INIT_FAILURE_TYPE, name));
        this.cause = cause;
        this.suiteId = name;
        this.tags = tags;
    }

    @Override
    public String getDisplayName() {
        return "Failed to load test from " + suiteId + ": '" + cause.getMessage() + "'";
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.of(MethodSource.from(suiteId, "<init>"));
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    public Throwable getCause() {
        return cause;
    }

    public String getSuiteId() {
        return suiteId;
    }
}
