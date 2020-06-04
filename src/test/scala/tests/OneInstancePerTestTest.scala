package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.OneInstancePerTest
import org.scalatest.funspec.AnyFunSpec

class OneInstancePerTestTest extends AnyFunSpec with RegisterCall with OneInstancePerTest {

  private val id: Int = OneInstancePerTestTest.nextId

  describe("OneInstancePerTest") {
    it("runs first") {
      register(id.toString)
    }

    it("runs second") {
      register(id.toString)
    }
  }
}

object OneInstancePerTestTest {
  var id: Int = 0

  def nextId: Int = {
    id = id + 1
    id
  }
}
