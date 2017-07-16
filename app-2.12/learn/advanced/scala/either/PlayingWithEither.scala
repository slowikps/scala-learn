package learn.advanced.scala.either

trait FailedValidation
case object EmptyString extends FailedValidation
case class SmallNumber() extends FailedValidation
object PlayingWithEither extends App {

  import cats.syntax.either._

  "Error".asLeft[Int].getOrElse(0)
  // res9: Int = 0

  "Error".asLeft[Int].orElse(2.asRight[String])
  // res10: Either[String,Int] = Right(2)

  println(
    1.asRight[String]
  )
  println {
    val tmp: Either[Object, Int] = 1.asRight[String].ensure(SmallNumber)(_ > 10)
    tmp
  }


//  for {
//
//  }
}
