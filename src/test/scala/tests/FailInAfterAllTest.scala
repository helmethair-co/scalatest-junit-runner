package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class FailInAfterAllTest extends AnyFunSuite with BeforeAndAfterAll with RegisterCall {

  override def afterAll() {
    register("after")
    fail()
  }

  test("test 1") {
    register("test 1 runs")
  }

  test("test 2") {
    register("test 2 runs")
  }
}
