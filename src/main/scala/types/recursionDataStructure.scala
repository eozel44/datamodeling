package types

import scala.annotation.tailrec

object recursionDataStructure {


  /**
    * A particular use of algebraic data types
    * that comes up very often is defining recursive data.
    * This is data that is defined in terms of itself,
    * and allows us to create data of potentally unbounded size
    * (though any concrete instance will be infinite).
    * */

  /**
    * Recursive Algebraic Data Types Pattern
    * When defining recursive algebraic data types,
    * there must be at least two cases:
    * one that is recursive, and one that is not.
    * Cases that are not recursive are known as base cases. In code, the general skeleton is:
    *
    * sealed trait RecursiveExample
    * case object BaseCase extends RecursiveExample
    * final case class RecursiveCase(recursion: RecursiveExample) extends RecursiveExample
    *
    * */

  sealed trait IntList{
    //recursive
    def sum: Int =
      this match {
        case End => 0
        case Pair(head, tail) => head+tail.sum
      }

    def double: IntList =
      this match {
        case End => End
        case Pair(head, tail) => Pair(head * 2, tail.double)
      }
  }
  case object End extends IntList
  final case class Pair(head: Int, tail: IntList) extends IntList

  def main(args: Array[String]): Unit = {

    val singlylinkedlist = Pair(1, Pair(2, Pair(3, End)))
    println("rec:"+singlylinkedlist.sum)
    println("duble:"+singlylinkedlist.double)

    /**
      * Tail Recursion
      * You may be concerned that recursive calls will consume excessive stack space.
      * Scala can apply an optimisation, called tail recursion, to many recursive functions
      * to stop them consuming stack space
      * */
    //tailrec
    @tailrec
    def sumtailrec(acc:Int=0,list: IntList): Int =
      list match {
        case End => acc
        case Pair(head, tail) => sumtailrec(acc+head,tail)
      }
    println("tailrec: "+ sumtailrec(0,singlylinkedlist))

    /***************
      * The first is that our list is restricted to storing Int s.
      * The second problem is that here is a lot of repetition
      * look sequencingComputations
      */

  }
}
