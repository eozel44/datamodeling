package types.typeClasses

object implicitParameters {
  def main(args: Array[String]): Unit = {

    /** * interfaces using enrichment and implicit parameters
      *
      * A type class is a trait with at least one type variable.
      * The type variables specify the concrete types the type class instances are defined for.
      * Methods in the trait usually use the type variables.
      *
      * trait ExampleTypeClass[A] {
      * def doSomething(in: A): Foo
      * }
      *
      */

    trait Equal[A] {
      def equal(v1: A, v2: A): Boolean
    }

    case class Person(name: String, email: String)

    object EmailEqual extends Equal[Person] {
      def equal(v1: Person, v2: Person): Boolean =
        v1.email == v2.email
    }

    object NameEmailEqual extends Equal[Person] {
      def equal(v1: Person, v2: Person): Boolean =
        v1.email == v2.email && v1.name == v2.name
    }

    val p1 = Person("eren", "eozel44@gmail.com")
    val p2 = Person("ahmet", "eozel44@gmail.com")

    import NameEmailEqual._
    val result = equal(p1, p2)
    println(result)

    {
      import EmailEqual._
      val result = equal(p1, p2)
      println(result)
    }
  }

}
