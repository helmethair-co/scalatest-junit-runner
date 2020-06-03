package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.funsuite.AnyFunSuite

class FailInBeforeAllTest extends AnyFunSuite with BeforeAndAfterAll  with RegisterCall {

    override def beforeAll(): Unit = {
        fail("test aborted")
    }

    test("never runs") {
        register()
    }
}
