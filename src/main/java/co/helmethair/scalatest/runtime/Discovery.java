package co.helmethair.scalatest.runtime;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestFailedInitDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestSuiteDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestTestDescriptor;
import co.helmethair.scalatest.scala.ScalaConversions;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestTag;
import org.scalatest.Suite;
import scala.Option;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        addTests(scalatestSuiteDescriptor, ScalaConversions.setAsJavaSet(suite.testNames()));
        ScalaConversions.asJavaCollection(suite.nestedSuites()).forEach(scalatestNestedSuite -> {
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
        testNames.forEach(testName ->
                linkChild(suite, new ScalatestTestDescriptor(suite, testName, getTags(suite.getScalasuite(), testName))));
    }

    private Set<TestTag> getTags(Suite scalasuite, String testName) {
        Option<scala.collection.immutable.Set<String>> tagSetOption = scalasuite.tags().get(testName);
        if (tagSetOption.isDefined()) {
            return ScalaConversions.setAsJavaSet(tagSetOption.get()).stream()
                    .map(TestTag::create).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
