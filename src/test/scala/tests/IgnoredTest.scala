package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class IgnoredTest extends AnyFunSuite with BeforeAndAfter with RegisterCall {

  test("Normal Test") {
    register("not ignored")
  }

  ignore("Ignored Test") {
    register("ignored")
  }
}
