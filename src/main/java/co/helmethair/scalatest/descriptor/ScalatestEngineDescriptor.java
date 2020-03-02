package co.helmethair.scalatest.descriptor;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class ScalatestEngineDescriptor extends EngineDescriptor {
    public ScalatestEngineDescriptor(UniqueId uniqueId, String name) {
        super(uniqueId, name);
    }
}
