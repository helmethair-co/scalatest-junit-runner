package tests

import org.scalatest.funspec.AnyFunSpec


class FailingTest extends AnyFunSpec {
  describe("FailingTest") {
    it("1fails") {
      assert(false)
    }
    it("2skips") {
    }
  }
}
