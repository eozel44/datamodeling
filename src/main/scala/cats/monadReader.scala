package cats

import cats.data.Reader

object monadReader {

  def main(args: Array[String]): Unit = {
    /**
      * The Reader Monad
      *
      * cats.data.Reader is a monad that allows us to sequence operations that depend on some input.
      * Instances of Reader wrap up functions of one argument,
      * providing us with useful methods for composing them.
      *
      * One common use for Readers is dependency injection. If we have a number
      * of operations that all depend on some external configuration, we can chain
      * them together using a Reader to produce one large operation that accepts
      * the configuration as a parameter and runs our program in the order specified.
      *
      **/

    /**
      * We can create a Reader[A, B] from a func on A => B using the
      * Reader.apply constructor:
      **/

    import cats.data.Reader
    case class Cat(name: String, favoriteFood: String)

    val catName: Reader[Cat, String] =
      Reader(cat => cat.name)
    // catName: cats.data.Reader[Cat,String] = Kleisli(<function1>)

    catName.run(Cat("Garfield", "lasagne"))
    // res0: cats.Id[String] = Garfield

    /**
      * The power of Readers comes from their map and flatMap methods, which
      * represent different kinds of function composition. We typically create a set of
      * Readers that accept the same type of configuration, combine them with map
      * and flatMap , and then call run to inject the config at the end.
      **/

    val greetKitty: Reader[Cat, String] =
      catName.map(name => s"Hello ${name}")

    greetKitty.run(Cat("Heathcliff", "junk food"))
    // res1: cats.Id[String] = Hello Heathcliff


    /** flatmap */

    val feedKitty: Reader[Cat, String] =
      Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

    val greetAndFeed: Reader[Cat, String] =
      for {
        greet <- greetKitty
        feed <- feedKitty
      } yield s"$greet. $feed."

    greetAndFeed(Cat("Garfield", "lasagne"))
    // res3: cats.Id[String] = Hello Garfield. Have a nice bowl of lasagne.
    greetAndFeed(Cat("Heathcliff", "junk food"))
    // res4: cats.Id[String] = Hello Heathcliff. Have a nice bowl of junk food.

    // TODO: reader monadların raw functions'tan farkı tam olarak nedir?
    // TODO: Aşağıdaki örnegi analiz edelim

    val users = Map(
      1 -> "dade",
      2 -> "kate",
      3 -> "margo"
    )
    val passwords = Map(
      "dade" -> "zerocool",
      "kate" -> "acidburn",
      "margo" -> "secret")

    val db = Db(users, passwords)
    checkLogin(1, "zerocool").run(db)
    // res10: cats.Id[Boolean] = true
    checkLogin(4, "davinci").run(db)
    // res11: cats.Id[Boolean] = false

    /**
      * Readers provide a tool for doing dependency injection.
      *
      * There are many ways of implementing dependency injection in Scala,
      * from simple techniques like methods with multiple parameter lists,
      * through implicit parameters and type classes, to complex techniques like the cake pattern and
      * DI frameworks.
      *
      *
      * Readers are most useful in situations where:
      *   • we are constructing a batch program that can easily be represented by a function;
      *   • we need to defer injection of a known parameter or set of parameters;
      *   • we want to be able to test parts of the program in isolation.
      * */

  }

  /**
    * The classic use of Readers is to build programs that accept a configuration as a parameter.
    **/

  case class Db(
                 usernames: Map[Int, String],
                 passwords: Map[String, String]
               )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(
                     username: String,
                     password: String): DbReader[Boolean] =
    Reader(db => db.passwords.get(username).contains(password))

  import cats.syntax.applicative._ // for pure
  def checkLogin(
                  userId: Int,
                  password: String): DbReader[Boolean] =
    for {
      username <- findUsername(userId)
      passwordOk <- username.map {
        username => checkPassword(username, password)
      }.getOrElse {
        false.pure[DbReader]
      }
    } yield passwordOk

}
