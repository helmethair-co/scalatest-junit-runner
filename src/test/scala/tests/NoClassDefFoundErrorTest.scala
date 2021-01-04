package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec

class NoClassDefFoundErrorTest extends AnyFunSpec with RegisterCall {
  describe("InitErrorTest") {
    it("never runs") {
      //throw an error what Scalatest handles as an abort instead failure
      throw new NoClassDefFoundError("ThisFails")
      register()
    }
  }
}
