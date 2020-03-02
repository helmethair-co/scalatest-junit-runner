package co.helmethair.scalatest.runtime;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestFailedInitDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestSuiteDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestTestDescriptor;
import org.junit.platform.engine.TestDescriptor;
import org.scalatest.Suite;
import scala.collection.JavaConversions;
import scala.collection.immutable.Set;

import java.util.List;

public class Discovery {

    public ScalatestEngineDescriptor discover(ScalatestEngineDescriptor engineDescriptor, List<Class<? extends Suite>> classes, ClassLoader classLoader) {
        classes.forEach(c -> {
            try {
                Suite suite = ((Suite) classLoader.loadClass(c.getName()).newInstance());
                addSuite(suite, engineDescriptor);
            } catch (Throwable e) {
                addFailedInit(e, c.getName(), engineDescriptor);
            }
        });

        return engineDescriptor;
    }

    private void addFailedInit(Throwable cause, String className, TestDescriptor parent) {
        ScalatestFailedInitDescriptor failed = new ScalatestFailedInitDescriptor(cause, className);
        linkChild(parent, failed);
    }

    private void addSuite(Suite suite, TestDescriptor parent) {
        ScalatestSuiteDescriptor scalatestSuiteDescriptor = new ScalatestSuiteDescriptor(suite, suite.suiteId(), suite.suiteName());
        linkChild(parent, scalatestSuiteDescriptor);
        addTests(scalatestSuiteDescriptor, suite.testNames());
        JavaConversions.asJavaCollection(suite.nestedSuites()).forEach(scalatestNestedSuite -> {
            try {
                addSuite(scalatestNestedSuite, scalatestSuiteDescriptor);
            } catch (Throwable e) {
                addFailedInit(e, scalatestNestedSuite.getClass().getName(), scalatestSuiteDescriptor);
            }
        });
    }

    private void linkChild(TestDescriptor parent, TestDescriptor child) {
        child.setParent(parent);
        parent.addChild(child);
    }

    private void addTests(ScalatestSuiteDescriptor suite, Set<String> testNames) {
        JavaConversions.asJavaCollection(testNames).forEach(testName -> {
            linkChild(suite, new ScalatestTestDescriptor(suite, testName));
        });
    }
}
