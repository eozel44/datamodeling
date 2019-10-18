package cats

object monoidSemigroup {

  /** *
    * a Monoid for a type A is:
    * • an operation combine with type (A, A) => A
    * • an element empty of type A
    *
    * combine must be associative { (1 + 2) + 3  === 1+(2+3) }
    * and empty must be an iden ty element:
    *
    * */
  object monoidSample{

    trait Monoid[A] {
      def combine(x: A, y: A): A

      def empty: A
    }

    def associativeLaw[A](x: A, y: A, z: A)
                         (implicit m: Monoid[A]): Boolean = {
      m.combine(x, m.combine(y, z)) ==
        m.combine(m.combine(x, y), z)
    }

    def identityLaw[A](x: A)
                      (implicit m: Monoid[A]): Boolean = {
      (m.combine(x, m.empty) == x) &&
        (m.combine(m.empty, x) == x)
    }
  }

  /**
    * A semigroup is just the combine part of a monoid.
    * While many semigroups are also monoids,
    * there are some data types for which we cannot define an empty element
    * */
  object semigroupSample{

    trait Semigroup[A] {
      def combine(x: A, y: A): A
    }

    trait Monoid[A] extends Semigroup[A] {
      def empty: A
    }
  }

  def main(args: Array[String]): Unit = {

    import cats.instances.string._
    import cats.Monoid
    Monoid[String].combine("Hi ", "there")
    // res0: String = Hi there
    Monoid[String].empty
    // res1: String = ""

    /* which is equivalent to */

    Monoid.apply[String].combine("Hi ", "there")
    // res2: String = Hi there
    Monoid.apply[String].empty
    // res3: String = ""


    import cats.Semigroup
    Semigroup[String].combine("Hi ", "there")
    // res4: String = Hi there


    import cats.instances.int._ // for Monoid
    val res5 = Monoid[Int].combine(32, 10)
    // res5: Int = 42
    println(res5)

    /*Option sample*/
    {
      import cats.Monoid
      import cats.instances.int._
      // for Monoid
      import cats.instances.option._ // for Monoid
      val a = Option(22)
      // a: Option[Int] = Some(22)
      val b = Option(20)
      // b: Option[Int] = Some(20)
      val res6 = Monoid[Option[Int]].combine(a, b)
      // res6: Option[Int] = Some(42)

      println(res6)
    }

    /*Monoid syntax
    * Cats provides syntax for the combine method in the form of the |+| operator.
    * */
    {
      import cats.instances.string._ // for Monoid
      import cats.syntax.semigroup._ // for |+|

      val stringResult = "Hi " |+| "there" |+| Monoid[String].empty
      // stringResult: String = Hi there

      import cats.instances.int._ // for Monoid
      val intResult = 1 |+| 2 |+| Monoid[Int].empty

    }


    /** *
      * We can use Semigroups and Monoids by importing three things:
      * the type classes themselves, the instances for the types we care about,
      * and the semi-group syntax to give us the |+| operator:
      * */
    {
      import cats.syntax.semigroup._ // for |+|
      import cats.instances.int._

      import cats.instances.option._
      Option(1) |+| Option(2)
      // res1: Option[Int] = Some(3)
      
      import cats.instances.map._
      val map1 = Map("a" -> 1, "b" -> 2)
      val map2 = Map("b" -> 3, "d" -> 4)
      map1 |+| map2
      // res3: Map[String,Int] = Map(b -> 5, d -> 4, a -> 1)

      import cats.instances.tuple._

      val tuple1 = ("hello", 123)
      val tuple2 = ("world", 321)
      tuple1 |+| tuple2
      // res6: (String, Int) = (helloworld,444)

    }


  }
}
