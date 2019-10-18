package cats

object semigroupal_applicative {

  /**
    * Semigroupal
    * cats.Semigroupal is a type class that allows us to combine contexts1.
    * If we have two objects of type F[A] and F[B] , a Semigroupal[F] allows us to
    * combine them to form an F[(A, B)] . Its defini on in Cats is:
    **/

  /**
    * the parameters fa and fb
    * are independent of one another: we can compute them in either order be-
    * fore passing them to product.This is in contrast to flatMap , which imposes
    * a strict order on its parameters. This gives us more freedom when defining
    * instances of Semigroupal than we get when defining Monads.
    **/
  trait Semigroupal[F[_]] {
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  }

  def main(args: Array[String]): Unit = {


    /**
      * Joining Two Contexts
      * While Semigroup allows us to join values, Semigroupal allows us to join contexts.
      *
      **/
    import cats.Semigroupal
    import cats.instances.option._ // for Semigroupal

    Semigroupal[Option].product(Some(123), Some("abc"))
    // res0: Option[(Int, String)] = Some((123,abc))
    Semigroupal[Option].product(None, Some("abc"))
    // res1: Option[(Nothing, String)] = None
    Semigroupal[Option].product(Some(123), None)
    // res2: Option[(Int, Nothing)] = None

    /**
      * Joining Three or More Contexts
      * The companion object for Semigroupal defines a set of methods on top of product.
      *
      **/

    import cats.instances.option._ // for Semigroupal
    Semigroupal.tuple3(Option(1), Option(2), Option(3))
    // res3: Option[(Int, Int, Int)] = Some((1,2,3))
    Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])
    // res4: Option[(Int, Int, Int)] = None


    Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)
    // res5: Option[Int] = Some(6)
    Semigroupal.map2(Option(1), Option.empty[Int])(_ + _)
    // res6: Option[Int] = None

    /**
      * Apply Syntax
      * Cats provides a convenient apply syntax that provides a shorthand for the
      * methods described above.
      **/

    import cats.instances.option._ // for Semigroupal
    import cats.syntax.apply._ // for tupled and mapN

    (Option(123), Option("abc")).tupled
    // res7: Option[(Int, String)] = Some((123,abc))

    (Option(123), Option("abc"), Option(true)).tupled
    // res8: Option[(Int, String, Boolean)] = Some((123,abc,true))


    case class Cat1(name: String, born: Int, color: String)

    (
      Option("Garfield"),
      Option(1978),
      Option("Orange & black")
    ).mapN(Cat1.apply)
    // res9: Option[Cat] = Some(Cat(Garfield,1978,Orange & black))


    val add: (Int, Int) => Int = (a, b) => a + b
    // add: (Int, Int) => Int = <function2>

    (Option(1), Option(2)).mapN(add)
    //res10:Some(3)

    //(Option("cats"), Option(true)).mapN(add)
    // <console>:27: error: type mismatch;

    /**
      * Fancy Functors and Apply Syntax
      *
      * Apply syntax also has contramapN and imapN methods that accept Contravariant and Invariant functors.
      * For example, we can combine Monoids using Invariant.
      *
      **/

    import cats.Monoid
    import cats.instances.int._ // for Monoid
    import cats.instances.invariant._ // for Semigroupal
    import cats.instances.list._ // for Monoid
    import cats.instances.string._ // for Monoid
    import cats.syntax.apply._ // for imapN

    case class Cat(
                    name: String,
                    yearOfBirth: Int,
                    favoriteFoods: List[String]
                  )
    val tupleToCat: (String, Int, List[String]) => Cat =
      Cat.apply _
    val catToTuple: Cat => (String, Int, List[String]) =
      cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)
    implicit val catMonoid: Monoid[Cat] = (
      Monoid[String],
      Monoid[Int],
      Monoid[List[String]]
    ).imapN(tupleToCat)(catToTuple)


    import cats.syntax.semigroup._ // for |+|
    val garfield = Cat("Garfield", 1978, List("Lasagne"))
    val heathcliff = Cat("Heathcliff", 1988, List("Junk Food"))

    garfield |+| heathcliff
    // res17: Cat = Cat(GarfieldHeathcliff,3966,List(Lasagne, Junk Food))


    /**
      * Semigroupal Applied to Different Types
      **/

    /**
      * Future
      *
      * The two Futures start executing the moment we create them,
      * so they are already calculating results by the me we call product.
      **/

    import cats.Semigroupal
    import cats.instances.future._ // for Semigroupal
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.language.higherKinds
    val futurePair = Semigroupal[Future].
      product(Future("Hello"), Future(123))
    Await.result(futurePair, 1.second)
    // res1: (String, Int) = (Hello,123)

    /**
      * List
      *
      * Combining Lists with Semigroupal produces some potentially unexpected results.
      * We might expect code like the following to zip the lists, but we actually
      * get the cartesian product of their elements:
      *
      **/
    import cats.Semigroupal
    import cats.instances.list._ // for Semigroupal
    Semigroupal[List].product(List(1, 2), List(3, 4))
    // res5: List[(Int, Int)] = List((1,3), (1,4), (2,3), (2,4))


    /**
      * Apply and Applicative
      *
      * Semigroupal and Applicative effec vely provide alterna ve encodings of
      * the same no on of joining contexts.
      *
      *
      * Cats models applicatives using two type classes.
      * The first, cats.Apply, extends Semigroupal and Functor and adds an ap method that applies
      * a parameter to a function within a context.
      * The second, cats.Applicative , extends Apply.
      *
      * */

/*
    trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
      def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
      def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
        ap(map(fa)(a => (b: B) => (a, b)))(fb)
    }
    trait Applicative[F[_]] extends Apply[F] {
      def pure[A](a: A): F[A]
    }
*/

    /**
      * Monad Type Class Hierarchy
      *
      *       Cartesian     Functor
      *        product        map
      *
      *               Apply
      *                ap
      *
      *       Applicative   FlatMap
      *          pure       flatmap
      *
      *
      *               Monad
      * */


    /**
      * Semigroupal and Applicative are most commonly used as a means of com-
      * bining independent values such as the results of validation rules. Cats provides
      * the Validated type for this specific purpose, along with apply syntax as a con-
      * venient way to express the combina on of rules.
      * */




  }
}
