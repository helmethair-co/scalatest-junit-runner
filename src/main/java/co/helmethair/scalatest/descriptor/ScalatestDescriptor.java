package co.helmethair.scalatest.descriptor;

import co.helmethair.scalatest.ScalatestEngine;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ScalatestDescriptor implements TestDescriptor {
    private final UniqueId id;
    private TestDescriptor parentDescriptor = null;
    private Set<TestDescriptor> childDescriptors = new HashSet<TestDescriptor>();
    final static UniqueId ENGINE_ID = UniqueId.forEngine(ScalatestEngine.ID);
    static final ConcurrentHashMap<UniqueId, ScalatestDescriptor> descriptorsById = new ConcurrentHashMap<>();

    protected ScalatestDescriptor(UniqueId id) {
        this.id = id;
        descriptorsById.put(id, this);
    }


    public static Optional<ScalatestDescriptor> find(String suiteId, String testName) {
        return findById(descriptorId(suiteId, testName));
    }

    public static Optional<ScalatestDescriptor> findById(UniqueId uniqueId) {
        return Optional.ofNullable(descriptorsById.get(uniqueId));
    }


    static UniqueId testId(String suiteId, String testName) {
        return ENGINE_ID.append("suite", suiteId).append("test", testName);
    }

    static UniqueId containerId(String suiteId) {
        return ENGINE_ID.append("suite", suiteId);
    }

    static UniqueId descriptorId(String suiteId, String testName) {
        if (testName == null) {
            return containerId(suiteId);
        } else {
            return testId(suiteId, testName);
        }
    }


    @Override
    public UniqueId getUniqueId() {
        return id;
    }

    @Override
    public void setParent(TestDescriptor parent) {
        if (parent != null && (parent instanceof ScalatestDescriptor || parent instanceof ScalatestEngineDescriptor)) {
            this.parentDescriptor = parent;
        }
    }

    @Override
    public Optional<TestDescriptor> getParent() {
        return Optional.ofNullable(parentDescriptor);
    }

    @Override
    public void addChild(TestDescriptor descriptor) {
        childDescriptors.add(descriptor);
    }

    @Override
    public Set<TestDescriptor> getChildren() {
        return childDescriptors;
    }

    @Override
    public Set<TestTag> getTags() {
        return new HashSet<>();
    }

    @Override
    public void removeFromHierarchy() {
        getParent().ifPresent(parent -> parent.removeChild(this));
    }

    @Override
    public void removeChild(TestDescriptor descriptor) {
        if (getType() == Type.CONTAINER) {
            childDescriptors.remove(descriptor);
        }
    }

    @Override
    public Optional<? extends TestDescriptor> findByUniqueId(UniqueId uniqueId) {
        return findById(uniqueId);
    }
}
