package cats

object monadWriter {

  def main(args: Array[String]): Unit = {

    /**
      * The Writer Monad
      *
      * cats.data.Writer is a monad that lets us carry a log along with a computation.
      * We can use it to record messages, errors, or additional data about a computation,
      * and extract the log alongside the final result.
      *
      **/

    /**
      * A Writer[W, A] carries two values: a log of type W and a result of type A.
      * We can create a Writer from values of each type as follows
      **/
    import cats.data.Writer
    import cats.instances.vector._ // for Monoid
    Writer(Vector(
      "It was the best of times",
      "it was the worst of times"
    ), 1859)
    // res0: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[
    // String],Int] = WriterT((Vector(It was the best of times, it was
    // the worst of times),1859))


    /**
      * Writers are useful for logging operations in multithreaded environments.
      * Let’s confirm this by computing (and logging) some factorials.
      **/

    def slowly[A](body: => A) =
      try body finally Thread.sleep(100)

    def factorial(n: Int): Int = {
      val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
      println(s"fact $n $ans")
      ans
    }

    factorial(5)

    /** If we start several factorials in parallel, the log messages can become inter-
      * leaved on standard out.
      */

    import scala.concurrent._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    Await.result(Future.sequence(Vector(
      Future(factorial(3)),
      Future(factorial(3))
    )), 5.seconds)


    /**
      * Lets rewrite factorial so it captures the log messages in a Writer.
      */

    /**
      * a type alias for Writer so we can use it with pure syntax
      */

    import cats.data.Writer
    import cats.syntax.applicative._ // for pure
    type Logged[A] = Writer[Vector[String], A]

    42.pure[Logged]
    // res13: Logged[Int] = WriterT((Vector(),42))

    /**
      * import tell syntax
      **/
    import cats.syntax.writer._ // for tell
    Vector("Message").tell
    // res14: cats.data.Writer[scala.collection.immutable.Vector[String],
    //  Unit] = WriterT((Vector(Message),()))

    def factorialV2(n: Int): Logged[Int] =
      for {
        ans <- if (n == 0) {
          1.pure[Logged]
        } else {
          slowly(factorialV2(n - 1).map(_ * n))
        }
        _ <- Vector(s"fact $n $ans").tell
      } yield ans

    val (log, res) = factorialV2(5).run
    // log: Vector[String] = Vector(fact 0 1, fact 1 1, fact 2 2, fact 3 6, fact 4 24, fact 5 120)
    // res: Int = 120

    val Vector((logA, ansA), (logB, ansB)) =
      Await.result(Future.sequence(Vector(
        Future(factorialV2(3).run),
        Future(factorialV2(5).run)
      )), 5.seconds)
    // logA: Vector[String] = Vector(fact 0 1, fact 1 1, fact 2 2, fact 3 6)
    // ansA: Int = 6
    // logB: Vector[String] = Vector(fact 0 1, fact 1 1, fact 2 2, fact 3 6, fact 4 24, fact 5 120)
    // ansB: Int = 120
  }

}
