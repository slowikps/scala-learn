package learn.advanced.scala.contramap

object Sample extends App {

  trait Printable[A] {
    def format(value: A): String

    def contramap[B](func: B => A): Printable[B] = {
      val self = this
      (value: B) => self.format(func(value))
    }
  }

  val printableString: Printable[String] = (value: String) => "\"" + value + "\""

  val printableInt = printableString.contramap((in: Int) => s"The number is: $in")

  println(printableInt.format(23))

  final case class Box[A](value: A)

  val printableBoxNotGood = printableString.contramap((b: Box[_]) => b.toString)

  implicit def boxPrintable[A](implicit p: Printable[A]) =
    p.contramap[Box[A]](_.value)

}
