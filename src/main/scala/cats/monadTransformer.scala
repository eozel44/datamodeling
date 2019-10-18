package cats

import cats.data.OptionT

object monadTransformer {

  /**
    * Imagine we are interacting with a database. We want to look up a user record.
    * The user may or may not be present, so we return an Option[User].
    * Our communication with the database could fail for many reasons
    * (network issues, authentication problems, and so on), so this result is wrapped up in an Either ,
    * giving us a final result of Either[Error, Option[User]] .
    **/
/*
  def lookupUserName(id: Long): Either[Error, Option[String]] =
    for {
      optUser <- lookupUser(id)
    } yield {
      for {user <- optUser} yield user.name
    }
*/


  def main(args: Array[String]): Unit = {


  /**
    * Cats provides transformers for many monads, each named with a T suffix:
    * EitherT composes Either with other monads, OptionT composes Option, and so on.
    *
    * */
    import cats.data.OptionT
    type ListOption[A] = OptionT[List, A]

    import cats.Monad
    import cats.instances.list._ // for Monad
    import cats.syntax.applicative._ // for pure

    val result1: ListOption[Int] = OptionT(List(Option(10)))
    // result1: ListOption[Int] = OptionT(List(Some(10)))
    val result2: ListOption[Int] = 32.pure[ListOption]
    // result2: ListOption[Int] = OptionT(List(Some(32)))

  /**
    * The map and flatMap methods combine the corresponding methods of List
    * and Option into single operations:
    *
    * This is the basis of all monad transformers. The combined map and flatMap
    * methods allow us to use both component monads without having to
    * recursively unpack and repack values at each stage in the computation.
    *
    */

  result1.flatMap { (x: Int) =>
    result2.map { (y: Int) =>
      x + y
    }
  }
  // res1: cats.data.OptionT[List,Int] = OptionT(List(Some(42)))

  }
}
