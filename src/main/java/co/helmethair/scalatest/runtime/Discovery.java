package co.helmethair.scalatest.runtime;

import co.helmethair.scalatest.descriptor.ScalatestEngineDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestFailedInitDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestSuiteDescriptor;
import co.helmethair.scalatest.descriptor.ScalatestTestDescriptor;
import co.helmethair.scalatest.scala.ScalaConversions;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestTag;
import org.scalatest.DoNotDiscover;
import org.scalatest.Suite;
import org.scalatest.TagAnnotation;
import scala.Option;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class Discovery {

    @SuppressWarnings("unchecked")
    public ScalatestEngineDescriptor discover(ScalatestEngineDescriptor engineDescriptor, Set<String> classes, ClassLoader classLoader) {
        classes.forEach(c -> {
            Class<?> aClass = null;
            try {
                aClass = classLoader.loadClass(c);
                if (isScalaSuite(aClass)) {
                    Suite suite = ((Suite) aClass.newInstance());
                    addSuite(suite, engineDescriptor);
                }
            } catch (Throwable e) {
                if (aClass != null && Suite.class.isAssignableFrom(aClass)) {
                    addFailedInit(e, (Class<? extends Suite>) aClass, engineDescriptor);
                } else {
                    addFailedInit(e, c, engineDescriptor);
                }
            }
        });

        return engineDescriptor;
    }

    private boolean isScalaSuite(Class<?> c) {
        return !(c.getEnclosingMethod() != null //only local or anonymous classes have an enclosing method
            || c.isSynthetic()
            || Modifier.isAbstract(c.getModifiers())
            || c.getAnnotation(DoNotDiscover.class) != null)
            && Suite.class.isAssignableFrom(c);
    }

    private void addFailedInit(Throwable cause, String className, TestDescriptor parent) {
        ScalatestFailedInitDescriptor failed = new ScalatestFailedInitDescriptor(cause, className, emptySet());
        linkChild(parent, failed);
    }

    private void addFailedInit(Throwable cause, Class<? extends Suite> clazz, TestDescriptor parent) {
        String className = clazz.getName();
        ScalatestFailedInitDescriptor failed = new ScalatestFailedInitDescriptor(cause, className, extractTags(clazz));
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
                addFailedInit(e, scalatestNestedSuite.getClass(), scalatestSuiteDescriptor);
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

    private Set<TestTag> extractTags(Class<? extends Suite> clazz) {
        return Arrays.stream(clazz.getAnnotations()).filter(a ->
                        a.annotationType().isAnnotationPresent(TagAnnotation.class)
                ).map(a -> TestTag.create(a.annotationType().getName()))
                .collect(Collectors.toSet());
    }

    private Set<TestTag> getTags(Suite scalasuite, String testName) {
        Option<scala.collection.immutable.Set<String>> tagSetOption = scalasuite.tags().get(testName);
        if (tagSetOption.isDefined()) {
            return ScalaConversions.setAsJavaSet(tagSetOption.get()).stream()
                    .map(TestTag::create).collect(Collectors.toSet());
        }
        return emptySet();
    }
}
