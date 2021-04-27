package co.helmethair.scalatest.example

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CapitalNameTest extends AnyFunSpec with Matchers {
  describe("Capital name") {
    it("runs") {
      true shouldBe true
    }
  }
}
