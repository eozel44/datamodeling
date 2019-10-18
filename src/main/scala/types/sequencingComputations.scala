package types

object sequencingComputations {

    /**
    * Generic types allow us to abstract over types.
    * There are useful for all sorts of data structures, but commonly encountered in collections
    * so that’s where we’re going to look at two more language features, generics and functions,
    * and see some abstractions we can build using these features:
    * functors, and monads.
    */

  sealed trait LinkedList[A] {
    def length: Int =
      this match {
        case Pair(hd, tl) => 1 + tl.length
        case End() => 0
      }

    def contains(item: A): Boolean =
      this match {
        case Pair(hd, tl) =>
          if (hd == item)
            true
          else
            tl.contains(item)
        case End() => false
      }

    def apply(index: Int): A =
      this match {
        case Pair(hd, tl) =>
          if (index == 0)
            hd
          else
            tl.apply(index - 1)
        case End() =>
          throw new Exception("Attempted to get element from an Empty list")
      }

    def get(index: Int): Result[A] =
      this match {
        case Pair(hd, tl) =>
          if (index == 0)
            Success(hd)
          else
            tl.get(index - 1)
        case End() =>
          Failure("Index out of bounds")
      }

    /*
        def fold(end: A, f: (A, A) => A): A =
          this match {
            case End() => end
            case Pair(hd, tl) => f(hd, tl.fold(end, f))
          }
        */

    def fold[B](end: B)(f: (A, B) => B): B =
      this match {
        case End() => end
        case Pair(hd, tl) => f(hd, tl.fold(end)(f))
      }

    def map[B](fn: A => B): LinkedList[B] =
      this match {
        case End() => End[B]()
        case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))
      }

    /// TODO: what about filter, flatmap

    def filter(fn: A => Boolean): LinkedList[A] =
      this match {
        case End() => End()
        case Pair(hd, tl) =>
          if (fn(hd))
            Pair(hd, tl.filter(fn))
          else
            tl.filter(fn)
      }
      /*def flatMap[B](fn:A=>LinkedList[B]) =
        this match {
          case End() => End()
        }*/
  }

  final case class End[A]() extends LinkedList[A]

  final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]


  /*result trait*/
  sealed trait Result[A]

  case class Success[A](result: A) extends Result[A]

  case class Failure[A](reason: String) extends Result[A]


  /** ***
    * A type like F[A] with a map method is called a functor. If a functor also has a
    * flatMap method it is called a monad
    */
  def main(args: Array[String]): Unit = {

    val singlylinkedlist = Pair(1, Pair(2, Pair(3, Pair(4, End()))))

    println("length: " + singlylinkedlist.length)
    println("5 in list ? : " + singlylinkedlist.contains(5))
    /*throw exception*/
    println("apply data of index 1: " + singlylinkedlist.apply(1))

    println("get data of index 1: " + singlylinkedlist.get(1))
    println("get data of index : " + singlylinkedlist.get(5))

    println("sum : " + singlylinkedlist.fold(1.0)((total, elt) => total * elt))

    println("sum new linkedlist: " + singlylinkedlist.map(_ * 5).fold(0)(_ + _))

    println("filter: " + singlylinkedlist.filter(_ > 2))

  }

}
