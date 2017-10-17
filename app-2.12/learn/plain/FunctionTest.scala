package learn.plain

object FunctionTest extends App {

  val intToString: (Int) => String = (in: Int) => in + " "

  val stringToStirng = (in: String) => in + in

  val composition = intToString andThen stringToStirng
}
