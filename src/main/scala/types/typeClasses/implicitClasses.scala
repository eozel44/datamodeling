package types.typeClasses


object implicitClasses {
  def main(args: Array[String]): Unit = {

    /**
      * Implicit classes are a Scala language feature that allows us to define extra
      * functionality on existing data types without using conventional inheritance.
      * This is a programming pattern called type enrichment.
      */

    implicit class IntOps(n: Int) {
      def yeah() = for {_ <- 0 until n} println("Oh yeah!")
    }

    /*sample*/
    2.yeah()

  }
}