package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.Tag
import org.scalatest.funspec.AnyFunSpec


object Slow extends Tag("slow")

object Fast extends Tag("fast")

class TaggedTestsTest extends AnyFunSpec with RegisterCall {

  describe("tagged Slow") {
    it("runs", Slow) {
      register()
    }
  }

  describe("tagged Fast and Slow") {
    it("runs", Slow, Fast) {
      register()
    }
  }

  describe("tagged Fast") {
    it("runs", Fast) {
      register()
    }
  }
  describe("untagged") {
    it("runs") {
      register()
    }
  }
}
