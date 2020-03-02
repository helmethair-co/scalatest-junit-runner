package tests

import org.scalatest.Suites
import org.scalatest.funspec.AnyFunSpec


class NestedTest extends Suites(
  new AnyFunSpec {
    describe("nested test1") {
      it("runs") {

      }
    }
  },
  new AnyFunSpec {
    describe("nested test2") {
      it("runs") {

      }
    }
  }
)
