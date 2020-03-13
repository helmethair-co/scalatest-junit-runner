package co.helmethair.scalatest.example

class SomeCodeTest extends AnyFunSpec with Matchers {
  describe("someFunc in SomeCode") {
    it("calculates square") {
      SomeCode.someFunc(3) shouldBe 9
    }
  }
}
