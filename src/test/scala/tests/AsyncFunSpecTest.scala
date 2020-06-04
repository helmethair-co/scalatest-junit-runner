package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.{ FutureOutcome, OneInstancePerTest }
import org.scalatest.funspec.AsyncFunSpec

import scala.concurrent.Future

class AsyncFunSpecTest extends AsyncFunSpec with RegisterCall with OneInstancePerTest {

  override def withFixture(test: NoArgAsyncTest): FutureOutcome = {
    register("setup")
    complete {
      super.withFixture(test) // Invoke the test function
    } lastly {
      register("cleanup")
    }
  }

  describe("AsyncTestSuite") {
    it("runs first") {
      Future {
        Thread.sleep(2000)
        register("runs asynchronously")
        assert(true)
      }
    }

    it("runs second") {
      Future {
        Thread.sleep(1000)
        register("runs again asynchronously")
        assert(true)
      }
    }
  }
}
