package types

object algebraicDataTypes {

  /**
    * An algebraic data type is any data that uses the above two patterns.
    * In the functional programming literature, data using the “has-a and” pattern is known as a product type,
    * and the “is-a or” pattern is a sum type
    **/

  /**
    *           And               Or
    * Is-a                        Sum type
    * Has-a     Product type
    **/
  def main(args: Array[String]): Unit = {

    /**
      * Sum Type Pattern
      * If A is a B or C write
      */
    {
      sealed trait A
      final case class B() extends A
      final case class C() extends A
    }
    /** Product Type Pattern
      * If A has a b (with type B ) and a c (with type C ) write
      * */

    {
      final case class B()
      final case class C()

      trait A {
        def b: B
        def c: C
      }

      /*or*/
      case class AA(a: A, b: B)
    }

  }
}
