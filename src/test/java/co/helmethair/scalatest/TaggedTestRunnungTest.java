package co.helmethair.scalatest;

import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.TestExecutionListener;

import java.util.Arrays;

import static org.mockito.Mockito.spy;

public class TaggedTestRunnungTest implements TestHelpers {

    String slowTestId = "[engine:scalatest]/[suite:tests.TaggedTestsTest]/[test:tagged Slow runs]";
    String slowFastTestId = "[engine:scalatest]/[suite:tests.TaggedTestsTest]/[test:tagged Fast and Slow runs]";
    String fastTestId = "[engine:scalatest]/[suite:tests.TaggedTestsTest]/[test:tagged Fast runs]";
    String untaggedTestId = "[engine:scalatest]/[suite:tests.TaggedTestsTest]/[test:untagged runs]";

    @Test
    void fastTest() {
        TestExecutionListener listener = spy(new TestExecutionListener() {
        });

        launch(Arrays.asList("tests.TaggedTestsTest"), Arrays.asList("fast"), Arrays.asList(), listener);

        verifyTestStartReported(fastTestId, listener);
        verifyTestSuccessReported(fastTestId, listener);

        verifyTestStartReported(slowFastTestId, listener);
        verifyTestSuccessReported(slowFastTestId, listener);

        verifyTestStartNotReported(slowTestId, listener);

        verifyTestStartNotReported(untaggedTestId, listener);
    }

    @Test
    void onlyFastTest() {
        TestExecutionListener listener = spy(new TestExecutionListener() {
        });

        launch(Arrays.asList("tests.TaggedTestsTest"), Arrays.asList("fast"), Arrays.asList("slow"), listener);

        verifyTestStartReported(fastTestId, listener);
        verifyTestSuccessReported(fastTestId, listener);

        verifyTestStartNotReported(slowFastTestId, listener);

        verifyTestStartNotReported(slowTestId, listener);
        verifyTestStartNotReported(untaggedTestId, listener);
    }

    @Test
    void excludeSlowTest() {
        TestExecutionListener listener = spy(new TestExecutionListener() {
        });

        launch(Arrays.asList("tests.TaggedTestsTest"), Arrays.asList(), Arrays.asList("slow"), listener);

        verifyTestStartReported(fastTestId, listener);
        verifyTestSuccessReported(fastTestId, listener);

        verifyTestStartNotReported(slowFastTestId, listener);

        verifyTestStartNotReported(slowTestId, listener);

        verifyTestStartReported(untaggedTestId, listener);
        verifyTestSuccessReported(untaggedTestId, listener);
    }

    @Test
    void allTagsTest() {
        TestExecutionListener listener = spy(new TestExecutionListener() {
        });

        launch(Arrays.asList("tests.TaggedTestsTest"), Arrays.asList(), Arrays.asList(), listener);

        verifyTestStartReported(fastTestId, listener);
        verifyTestSuccessReported(fastTestId, listener);

        verifyTestStartReported(slowFastTestId, listener);
        verifyTestSuccessReported(slowFastTestId, listener);

        verifyTestStartReported(slowTestId, listener);
        verifyTestSuccessReported(slowTestId, listener);

        verifyTestStartReported(untaggedTestId, listener);
        verifyTestSuccessReported(untaggedTestId, listener);
    }
}
