package learn.advanced.scala.either

import cats.data.EitherT

import scala.concurrent.Future

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

  val catchOnly: Either[NumberFormatException, Int] =
    Either.catchOnly[NumberFormatException]("foo".toInt)

  val catchNonFatal: Either[Throwable, Nothing] =
    Either.catchNonFatal(sys.error("Badness"))

  type MyType[A] = EitherT[Future, Int, A]
  type MyTypeError[A] = EitherT[Future, A, String]

  import cats.instances.future._
  import cats.syntax.applicative._

  import scala.concurrent.ExecutionContext.Implicits.global


  val pureTest: MyType[String] = "success".pure[MyType]

  println("Pure test: " + pureTest)

//  implicit val in = cats.ApplicativeError[Future, String]
//  val test = in.raiseError("someBadError")
//  println("Pure test: " + test)
}
