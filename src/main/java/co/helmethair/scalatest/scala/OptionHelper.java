package co.helmethair.scalatest.scala;

import scala.Option;

public class OptionHelper {
    public static <T> T getOrElse(Option<T> option, T defaultValue) {
        if (option.isDefined()) {
            return option.get();
        } else {
            return defaultValue;
        }
    }

}
