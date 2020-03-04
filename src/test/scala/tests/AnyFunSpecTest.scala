package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec


class AnyFunSpecTest extends AnyFunSpec with RegisterCall {
  describe("AnyFunSpec") {
    it("runs") {
      register()
    }
  }
}
