package tests

import co.helmethair.scalatest.helper.RegisterCall
import org.scalatest.funsuite.AnyFunSuiteLike

class WithTraitTest extends Object with AnyFunSuiteLike with RegisterCall {

  test("WithTraitTest runs") {
      register()
  }
}
