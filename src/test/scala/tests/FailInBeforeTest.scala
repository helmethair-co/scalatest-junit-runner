package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class FailInBeforeTest extends AnyFunSuite with BeforeAndAfter with RegisterCall {

  before {
    register("before")
    fail("test aborted")
  }

  test("never runs") {
    register()
  }
}
