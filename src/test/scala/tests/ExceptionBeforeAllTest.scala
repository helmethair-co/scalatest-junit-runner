package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec

class ExceptionBeforeAllTest extends AnyFunSpec with RegisterCall with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    super.beforeAll()
    register("beforeAll begin")
    throw new Exception("before all faliled")
  }

  describe("BeforeAndAfterAll") {
    it("runs") {
      register("runs")
    }

    it("runs again") {
      register("runs again")
    }
  }

  override def afterAll(): Unit = {
    register("afterAll")
  }
}
