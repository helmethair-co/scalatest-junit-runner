package co.helmethair.scalatest.example

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class YouKnowWho extends AnyFunSpec with Matchers {
  describe("He Who Must Not Be Named... (testing that this runs even when does not contain the magic word 'T e s t')") {
    it("runs") {
      true shouldBe true
    }
  }
}
