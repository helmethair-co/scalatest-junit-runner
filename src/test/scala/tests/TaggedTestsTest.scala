package tests

import org.scalatest.Tag
import org.scalatest.funspec.AnyFunSpec


object Slow extends Tag("slow")

object Fast extends Tag("fast")

class TaggedTestsTest extends AnyFunSpec {

  describe("tagged Slow") {
    it("runs", Slow) {
    }
  }

  describe("tagged Fast and Slow") {
    it("runs", Slow, Fast) {
    }
  }

  describe("tagged Fast") {
    it("runs", Fast) {
    }
  }
  describe("untagged") {
    it("runs") {
    }
  }
}
