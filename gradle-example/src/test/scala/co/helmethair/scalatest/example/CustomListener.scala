package co.helmethair.scalatest.example
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.{TestExecutionListener, TestIdentifier}

class CustomListener extends TestExecutionListener {
  override def executionStarted(testIdentifier: TestIdentifier): Unit = {
    System.err.println(s">>>>> CustomListener: execution STARTED: ${testIdentifier.getDisplayName}")
  }

  override def executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult): Unit = {
    System.err.println(s">>>>> CustomListener: execution FINISHED: ${testIdentifier.getDisplayName} with result: ${testExecutionResult.getStatus.name()}")
  }
}
