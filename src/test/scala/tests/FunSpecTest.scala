package tests

import co.helmethair.scalatest.helper.RegisterCall

class FunSpecTest extends org.scalatest.FunSpec with RegisterCall {
  describe("FunspecTest") {
    it("runs") {
      register()
    }
  }
}
