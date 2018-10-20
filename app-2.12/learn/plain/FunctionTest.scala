package learn.plain

import scala.util.{Success, Try}

object FunctionTest extends App {

  val intToString: (Int) => String = (in: Int) => in + " "

  val stringToStirng = (in: String) => in + in

  val composition = intToString andThen stringToStirng

  def stringArg: PartialFunction[Any, Unit] = {
    case i: String => println(s"This is string: $i")
  }

  def intArg: PartialFunction[Any, Unit] = {
    case i: Int => println(s"This is int: $i")
  }

  def printSafe(in: => Any) = {
    Try {
      in
    } match {
      case Success(_) =>
      case f          => println(s"failure: $f")
    }
  }

  def intOrElseString = intArg orElse stringArg

  printSafe(
    intArg("pawel")
  )
  printSafe(
    stringArg(11)
  )

  printSafe(
    intOrElseString("pawel")
  )
  printSafe(
    intOrElseString(11)
  )

}
