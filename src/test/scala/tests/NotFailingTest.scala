package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec


class NotFailingTest extends AnyFunSpec with RegisterCall {
  describe("NotFailingTest") {
    it("does not fail") {
      register()
    }
  }
}
