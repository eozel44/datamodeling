package types.typeClasses

object typeClassInterfaces {
  def main(args: Array[String]): Unit = {
    /**
      * Type Class Interface Pattern
      * If the desired interface to a type class TypeClass is exactly the methods
      * defined on the type class trait, define an interface on the companion
      * object using a no-argument apply method like
      *
      * object TypeClass {
      * def apply[A](implicit instance: TypeClass[A]): TypeClass[A] =
      * instance
      * }
      */


    trait Equal[A] {
      def equal(v1: A, v2: A): Boolean
    }

    object Eq {
      def apply[A](v1: A, v2: A)(implicit equal: Equal[A]): Boolean =
        equal.equal(v1, v2)
    }


    case class Person(name: String, email: String)
    case class Customer(id:Int,name:String)

    object NameAndEmailImplicit {
      implicit object NameEmailEqual extends Equal[Person] {
        def equal(v1: Person, v2: Person): Boolean =
          v1.email == v2.email && v1.name == v2.name
      }

    }
    object CustomerCompare {
      implicit class customerCompare[T](v1:T) extends AnyRef {
        def equalme(v2: T)(implicit eq:Equal[T]): Boolean = eq.equal(v1,v2)
      }

    }
    class eqCustomer extends Equal[Customer] {
      override def equal(v1: Customer, v2: Customer): Boolean = v1 == v2
    }


    implicit val quealCus = new eqCustomer
    object example {
      def byNameAndEmail = {
        import NameAndEmailImplicit._
        println(Eq(Person("Noel", "noel@example.com"), Person("Noel", "noel@example.com")))
      }
      def byCustomerId = {
        import CustomerCompare._
        println(Customer(1, "noel@example.com").equalme(Customer(1, "noel@example.com")))
      }
    }

    example.byNameAndEmail
    example.byCustomerId
  }
}