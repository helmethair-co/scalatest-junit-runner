package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.Suites
import org.scalatest.funspec.AnyFunSpec


class NestedTest extends Suites(
  new AnyFunSpec with RegisterCall {
    describe("nested test1") {
      it("runs") {
        register()
      }
    }
  },
  new AnyFunSpec with RegisterCall {
    describe("nested test2") {
      it("runs") {
        register()
      }
    }
  }
)
