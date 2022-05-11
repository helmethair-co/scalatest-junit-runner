package tests

import co.helmethair.scalatest.SuitLevelTag
import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec

@SuitLevelTag
class InitErrorTaggedTest extends AnyFunSpec with RegisterCall {
  //throw some serious stuff during instantiation
  throw new InternalError()
  describe("InitErrorTaggedTest") {
    it("never runs") {
      register()
    }
  }
}
