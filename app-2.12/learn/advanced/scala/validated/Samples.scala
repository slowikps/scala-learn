package learn.advanced.scala.validated

import cats.data.Validated


object Samples extends App {

  val v = Validated.Valid(123)
  val i = Validated.Invalid("Badness")

  val v2 = Validated.valid[String, Int](123)
  val i2 = Validated.invalid[String, Int]("Badness")

  import cats.syntax.validated._

  123.valid[String]
  "Badness".invalid[Int]

  Validated.catchOnly[NumberFormatException]("foo".toInt)
  Validated.catchNonFatal(sys.error("Badness"))
  Validated.fromTry(scala.util.Try("foo".toInt))
  Validated.fromEither[String, Int](Left("Badness"))
  Validated.fromOption[String, Int](None, "Badness")
}
