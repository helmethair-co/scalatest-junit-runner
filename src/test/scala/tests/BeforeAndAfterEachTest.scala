package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

class BeforeAndAfterEachTest extends AnyFunSpec with RegisterCall with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    register()
  }

  describe("BeforeAndAfterEach") {
    it("runs") {
      register()
    }

    it("runs again") {
      register()
    }
  }

  override def afterEach(): Unit = {
    register()
  }
}
