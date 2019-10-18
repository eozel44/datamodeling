package cats

object functor {

  /**
    * functor: a class that encapsulates sequencing computations.
    * Formally, a functor is a type F[A] with an operation
    * map with type (A => B) => F[B]
    */

  object functorSample {

    import scala.language.higherKinds

    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B): F[B]
    }

  }

  /**
    * Functor Laws;
    * Functors guarantee the same semantics whether we sequence many small operations one by one,
    * or combine them into a larger function before mapping.
    * To ensure this is the case the following laws must hold:
    *
    * Identity: calling map with the identity function is the same as doing nothing:
    * fa.map(a => a) == fa
    *
    * Composition: mapping with two functions f and g is the same as mapping with f and then mapping with g:
    * fa.map(g(f(_))) == fa.map(f).map(g)
    */


  def main(args: Array[String]): Unit = {


    import scala.language.higherKinds
    import cats.Functor
    import cats.instances.list._
    // for Functor
    import cats.instances.option._ // for Functor
    val list1 = List(1, 2, 3)
    // list1: List[Int] = List(1, 2, 3)
    val list2 = Functor[List].map(list1)(_ * 2)
    // list2: List[Int] = List(2, 4, 6)
    val option1 = Option(123)
    // option1: Option[Int] = Some(123)
    val option2 = Functor[Option].map(option1)(_.toString)
    // option2: Option[String] = Some(123)

    /**
      * Functor also provides the lift method, which converts a function of type
      * A => B to one that operates over a functor and has type F[A] => F[B]
      *
      */
    val func = (x: Int) => x + 1
    val liftedFunc = Functor[Option].lift(func)
    liftedFunc(Option(1))

    /**
      * Functor Syntax
      */

    import cats.instances.function._ // for Functor
    import cats.syntax.functor._
    // for map
    val func1 = (a: Int) => a + 1
    val func2 = (a: Int) => a * 2
    val func3 = (a: Int) => a + "!"
    val func4 = func1.map(func2).map(func3)
    func4(123)
    // res1: String = 248!

    /**
      * Type Constructor
      **/

    def doMath[F[_]](start: F[Int])
                    (implicit functor: Functor[F]): F[Int] =
      start.map(n => n + 1 * 2)

    val res3 = doMath(Option(20))
    // res3: Option[Int] = Some(22)
    println(res3)
    val res4 = doMath(List(1, 2, 3))
    // res4: List[Int] = List(3, 4, 5)
    println(res4)

  }
}
