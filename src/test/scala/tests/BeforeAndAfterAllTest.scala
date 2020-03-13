package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec

class BeforeAndAfterAllTest extends AnyFunSpec with RegisterCall with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    register("beforeAll")
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
