package types.typeClasses

object implicitValues {

  def main(args: Array[String]): Unit = {
    /**
      * A type class is like a trait, defining an interface.
      * However, with type classes we can:
      * • plug in different implementations of an interface for a given class; and
      * • implement an interface without modifying existing code.
      */
    import scala.math.Ordering
    val minOrdering = Ordering.fromLessThan[Int](_ < _)
    val maxOrdering = Ordering.fromLessThan[Int](_ > _)

    val listMinOrder = List(3, 4, 2).sorted(minOrdering)
    val listMaxOrder = List(3, 4, 2).sorted(maxOrdering)

    /**
      * Implicit Values
      * It can be inconvenient to continually pass the type class instance to a method
      * when we want to repeatedly use the same instance.
      * Scala provides a convenience, called an implicit value, that allows us to get the compiler to pass the
      * type class instance for us
      **/
    implicit val ordering = Ordering.fromLessThan[Int](_ < _)
    val listImplictOrder = List(2, 4, 3).sorted

    /**
      * Implicit Value Ambiguity
      * What happens when multiple implicit values are in scope?
      * implicit val minOrdering = Ordering.fromLessThan[Int](_ < _)
      * implicit val maxOrdering = Ordering.fromLessThan[Int](_ > _)
      * List(3,4,5).sorted
      */
  }
}
