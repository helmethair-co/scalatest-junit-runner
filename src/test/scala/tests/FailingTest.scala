package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec


class FailingTest extends AnyFunSpec with RegisterCall {
  describe("FailingTest") {
    it("1fails") {
      assert(false)
      register()
    }
    it("2does not fail") {
      register()
    }
  }
}
