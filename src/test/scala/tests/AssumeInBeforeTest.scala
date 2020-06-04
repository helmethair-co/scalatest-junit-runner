package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class AssumeInBeforeTest extends AnyFunSuite with BeforeAndAfter with RegisterCall {

  before {
    assume(false, "this should be aborted")
  }

  test("some test") {

  }
}
