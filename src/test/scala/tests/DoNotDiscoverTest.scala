package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.DoNotDiscover
import org.scalatest.funsuite.AnyFunSuite

@DoNotDiscover
class DoNotDiscoverTest extends AnyFunSuite with RegisterCall {

  test("never registered") {
    register()
  }

}
