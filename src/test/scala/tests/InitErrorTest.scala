package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec

class InitErrorTest extends AnyFunSpec with RegisterCall {
  //throw some serious stuff during instantiation
  throw new InternalError()
  describe("InitErrorTest") {
    it("never runs") {
      register()
    }
  }
}
