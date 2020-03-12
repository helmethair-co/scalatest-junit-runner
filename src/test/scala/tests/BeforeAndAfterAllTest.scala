package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec

class BeforeAndAfterAllTest extends AnyFunSpec with RegisterCall with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    register()
  }

  describe("BeforeAndAfterAll") {
    it("runs") {
      register()
    }

    it("runs again") {
      register()
    }
  }

  override def afterAll(): Unit = {
    register()
  }
}
