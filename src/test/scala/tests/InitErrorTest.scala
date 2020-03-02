package tests

import org.scalatest.funspec.AnyFunSpec

class InitErrorTest extends AnyFunSpec {
  //throw some serious stuff during instantiation
  throw new InternalError()
  describe("InitErrorTest") {
    it("never runs") {
    }
  }
}
