package co.helmethair.scalatest.example

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

@co.helmethair.scalatest.tags.Skip
class SkipWithScalaTag extends AnyFunSpec with Matchers {
  describe("test one") {
    it("assert one") {

    }
    it("assert two") {

    }
  }

  describe("test two") {
    it("assert one") {

    }
    it("assert two") {

    }

  }
}
