package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class FailInAfterTest extends AnyFunSuite with BeforeAndAfter with RegisterCall {

  after {
    register("after")
    /*
      As per Scalatest, only certain tests causing the test fail in an after block
      "We will swallow an exception thrown from the after code if it is not test-aborting
      and an exception was already thrown by beforeEach or test itself."
     */
    throw new ThreadDeath {}
  }

  test("test") {
    register("runs")
  }
}
