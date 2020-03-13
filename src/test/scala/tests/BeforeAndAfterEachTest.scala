package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

class BeforeAndAfterEachTest extends AnyFunSpec with RegisterCall with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    register("beforeEach")
  }

  describe("BeforeAndAfterEach") {
    it("runs") {
      register("runs")
    }

    it("runs again") {
      register("runs again")
    }
  }

  override def afterEach(): Unit = {
    register("afterEach")
  }
}
