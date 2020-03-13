package co.helmethair.scalatest.example

object SomeCode {
  def someFunc(x: Int): Int = {
    System.err.println(s"called with: $x")
    x * x
  }
}
