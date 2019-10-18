package cats

object monadCustom {




  def main(args: Array[String]): Unit = {

    /**
      * The Custom Monad
      *
      *
      * We can define a Monad for a custom type by providing implementations of three methods:
      * flatMap , pure , and a method we haven’t seen yet called tailRecM.
      * Here is an implementation of Monad for Option
      * */

    import cats.Monad
    import scala.annotation.tailrec
    val optionMonad = new Monad[Option] {
      def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] =
        opt flatMap fn

      def pure[A](opt: A): Option[A] =
        Some(opt)

      @tailrec
      def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
        fn(a) match {
          case None => None
          case Some(Left(a1)) => tailRecM(a1)(fn)
          case Some(Right(b)) => Some(b)
        }
      /**
        * The tailRecM method is an optimisation used in Cats to limit the amount
        * of stack space consumed by nested calls to flatMap . The technique comes
        * from a 2015 paper by PureScript creator Phil Freeman. The method should
        * recursively call itself until the result of fn returns a Right
        * */
    }


  }

}
