package cats

import cats.data.WriterT

object monad {

  /**
    * A monad is a mechanism for sequencing computations.
    * Informally, a monad is anything with a constructor and a flatMap method.
    **/

  object monadSample {

    import scala.language.higherKinds

    trait Monad[F[_]] {
      def pure[A](value: A): F[A]

      def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
    }

  }

  /**
    * Monad Laws
    * pure and flatMap must obey a set of laws that allow us to sequence
    * operations freely without unintended glitches and side-effects:
    *
    * Left identity: calling pure and transforming the result with func is the
    * same as calling func :
    * pure(a).flatMap(func) == func(a)
    *
    * Right identity: passing pure to flatMap is the same as doing nothing:
    * m.flatMap(pure) == m
    *
    * Associativity: flatMapping over two functions f and g is the same as
    * flatMapping over f and then flatMapping over g :
    * m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
    **/

  /**
    * NOTE: what about map
    * def map[A, B](value: F[A])(func: A => B): F[B] =
    * flatMap(value)(a => pure(func(a)))
    **/

  def main(args: Array[String]): Unit = {


    /**
      * Monad sample
      **/

    def parseInt(str: String): Option[Int] =
      scala.util.Try(str.toInt).toOption

    def divide(a: Int, b: Int): Option[Int] =
      if (b == 0) None else Some(a / b)

    def stringDivideBy(aStr: String, bStr: String): Option[Int] =
      parseInt(aStr).flatMap { aNum =>
        parseInt(bStr).flatMap { bNum =>
          divide(aNum, bNum)
        }
      }

    stringDivideBy("8", "2")
    stringDivideBy("8", "0")
    stringDivideBy("8", "a")

    /**
      * • the first call to parseInt returns a None or a Some;
      * • if it returns a Some , the flatMap method calls our function and passes us the integer aNum;
      * • the second call to parseInt returns a None or a Some;
      * • if it returns a Some , the flatMap method calls our func on and passes us bNum;
      * • the call to divide returns a None or a Some , which is our result.
      **/

    /**
      * ForComprehension way
      **/
    def stringDivideByForComprehension(aStr: String, bStr: String): Option[Int] =
      for {
        aNum <- parseInt(aStr)
        bNum <- parseInt(bStr)
        ans <- divide(aNum, bNum)
      } yield ans


    /**
      * NOTE:Future is a monad that sequences computations without worrying that they are asynchronous
      **/

    /**
      * The Monad Type Class
      * The monad type class is cats.Monad . Monad extends two other type classes:
      * FlatMap , which provides the flatMap method, and Applicative , which pro-
      * vides pure . Applicative also extends Functor , which gives every Monad a
      * map method as we saw in the exercise above.
      */
    import cats.Monad
    import cats.instances.option._
    import cats.instances.list._

    val opt1 = Monad[Option].pure(3)
    // opt1: Option[Int] = Some(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    // opt2: Option[Int] = Some(5)
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)
    // opt3: Option[Int] = Some(500)

    val list1 = Monad[List].pure(3)
    // list1: List[Int] = List(3)
    val list2 = Monad[List].
      flatMap(List(1, 2, 3))(a => List(a, a * 10))
    // list2: List[Int] = List(1, 10, 2, 20, 3, 30)
    val list3 = Monad[List].map(list2)(a => a + 123)
    // list3: List[Int] = List(124, 133, 125, 143, 126, 153)


    /**
      * Future sample
      **/
    import cats.instances.future._ // for Monad
    import scala.concurrent._
    import scala.concurrent.duration._

    import scala.concurrent.ExecutionContext.Implicits.global
    val fm = Monad[Future]

    val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
    val res3 = Await.result(future, 1.second)
    // res3: Int = 3
    println(res3)


    /**
      * The Identity Monad
      **/
    import scala.language.higherKinds
    import cats.syntax.functor._ // for map
    import cats.syntax.flatMap._ // for flatMap

    def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
      for {
        x <- a
        y <- b
      } yield x * x + y * y

    /**
      * This method works well on Options and Lists but we can’t call it passing in
      * plain values:
      * sumSquare(3, 4)
      **/
    import cats.Id
    sumSquare(3: Id[Int], 4: Id[Int])
    // res2: cats.Id[Int] = 25


    /**
      * another useful monad: the Either
      **/

    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(32)
    for {
      a <- either1.right
      b <- either2.right
    } yield a + b
    // res0: scala.util.Either[String,Int] = Right(42)

    // or

    for {
      a <- either1
      b <- either2
    } yield a + b
    // res0: scala.util.Either[String,Int] = Right(42)

    /**
      * Eval Monad, the three behaviours
      *
      * Scala       Cats      Properties
      *
      * val         Now       eager,memoized
      * lazy val    Later     lazy,memoized
      * def         Always    lazy, not memoized
      *
      **/

    val ans = for {
      a <- Eval.now {
        println("Calculating A");
        40
      }
      b <- Eval.always {
        println("Calculating B");
        2
      }
    } yield {
      println("Adding A and B")
      a + b
    }
    // Calculating A
    // ans: cats.Eval[Int] = cats.Eval$$anon$8@60912c39
    ans.value // first access
    // Calculating B
    // Adding A and B
    // res16: Int = 42
    ans.value // second access
    // Calculating B
    // Adding A and B
    // res17: Int = 42
  }
}
