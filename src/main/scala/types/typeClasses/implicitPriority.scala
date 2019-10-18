package types.typeClasses

object implicitPriority {
  def main(args: Array[String]): Unit = {

    /**
      * Type Class Instance Packaging: Companion Objects
      * When defining a type class instance, if
      * 1. there is a single good default instance for the type; and
      * 2. you can edit the code for the type that you are defining the instance for then define the type class instance in the companion object of the type.
      * This allows users to override the instance by defining one in the local scope
      * whilst still providing sensible default behaviour.
      **/

    /**   Priority
      * • Local or inherited definitions
      * • imported definitions
      * • definitions in the companion object of the type class or the parameter
      * */

    /*Local Scope*/
    final case class Rational(numerator: Int, denominator: Int)
    object example {
      def example() = {
        implicit val higherPriortyOrdering = Ordering.fromLessThan[Rational]((x, y) =>
          (x.numerator.toDouble / x.denominator.toDouble) <
            (y.numerator.toDouble / y.denominator.toDouble)
        )
        assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
          ==
          List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
      }
    }
    /*Companion object scope*/
    // TODO: compaion object altındaki implicitVal bir türlü çalıştıramadım

    object Rational {
      implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
        (x.numerator.toDouble / x.denominator.toDouble) <
          (y.numerator.toDouble / y.denominator.toDouble)
      )
    }
  }


}
