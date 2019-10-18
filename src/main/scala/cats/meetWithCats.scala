package cats

object meetWithCats {

  def main(args: Array[String]): Unit = {

    /**
      * The type classes in Cats are defined in the cats package
      *
      * The cats.instances package provides default instances for a wide variety of
      * types.
      */
    import cats.instances.int._
    import cats.instances.string._

    val showInt: Show[Int] = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]

    val intAsString: String = showInt.show(123)
    val stringAsString: String = showString.show("abc")
    println(intAsString)
    println(stringAsString)


    /**
      * We can make Show easier to use by impor ng the interface syntax from
      * cats.syntax.show.
      */
    import cats.syntax.show._
    println(456.show)

    /*Eq sample*/
    {
      import cats.Eq
      import cats.instances.int._
      val eqInt = Eq[Int]

      println(eqInt.eqv(123, 123))
      println(eqInt.eqv(123, 456))


      /*
        //Unlike Scala’s == method, if we try to compare objects of different types using eqv
        //we get a compile error:
        println(eqInt.eqv(123, "123"))
      */

      import cats.syntax.eq._
      println(123===123)
      println("123"==="123")

      import cats.instances.option._
      import cats.syntax.option._
      1.some === none[Int]

    }
  }

}
