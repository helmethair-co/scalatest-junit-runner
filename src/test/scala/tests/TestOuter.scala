package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funspec.AnyFunSpec

object TestOuter {

  object Inner {

    case class CaseClass(a: String)

    class NotCase() extends AnyFunSpec with RegisterCall{

    describe("NotCase") {
        it("runs") {
          register()
        }
      }
    }

  }
}
