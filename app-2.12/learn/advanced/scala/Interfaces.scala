package learn.advanced.scala

trait ToIntegerConverter[A] {
  def toInt(a: A): Int
}

object ToIntegerConverterOps {
  implicit val stringToInt = new ToIntegerConverter[String] {
    override def toInt(a: String): Int = a.length
  }
}

//Interface object
object AsMyIntegerInterfaceObject {
  def asMyInteger[A](value: A)(implicit w: ToIntegerConverter[A]): Int =
    w.toInt(value)
}

object AsMyIntegerInterfaceSyntax {

  implicit class AsMyInteger[A](value: A) {
    //extension methods
    def asMyInteger(implicit w: ToIntegerConverter[A]): Int =
      w.toInt(value)
  }

}

object Interfaces extends App {
  val myString = "Pawel's String"

  import ToIntegerConverterOps._
  {
    println(myString + " asMyInteger: " + AsMyIntegerInterfaceObject.asMyInteger(myString))
  }

  {
    import AsMyIntegerInterfaceSyntax._
    println(myString + " asMyInteger: " + myString.asMyInteger)
//    println(11.0 + " asMyInteger: " + (11.0).asMyInteger)
  }

}
