package co.helmethair.scalatest.example

import org.scalatest.Tag
import org.scalatest.flatspec.AnyFlatSpec

object TagOne extends Tag("TagOne")

object TagTwo extends Tag("TagTwo")

class TaggedTest extends AnyFlatSpec {

  "Integration tests" can "sometimes be slow" taggedAs TagOne in {

  }

  "Unit tests" can "be fast" taggedAs TagTwo in {

  }
}
