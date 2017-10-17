package learn.advanced.scala.either

import cats.data.EitherT
import cats.{ApplicativeError, Monad, MonadError}

import scala.concurrent.Future

object ErrorHandling extends App {

  type MyErrorType[A] = EitherT[Future, A, Int]

  import cats.instances.list._

  def fullConversionPath()(implicit monad: Monad[List]) = {
    val monadError: MonadError[EitherT[List, String, ?], String] =
      EitherT.catsDataMonadErrorForEitherT(monad)

    val whatIsThat: EitherT[List, String, Int] = monadError.raiseError[Int]("Some error")

    whatIsThat
  }

  def shorterConversionPath()(implicit mE: ApplicativeError[EitherT[List, String, ?], String]) = {
    val whatIsThat: EitherT[List, String, Int] = mE.raiseError[Int]("Some error")

    whatIsThat
  }

  type MyEither[A] = EitherT[List, String, A]
  def shorterSyntaxConversionPath()(implicit mE: ApplicativeError[EitherT[List, String, ?], String]) = {
    import cats.syntax.applicativeError._
    val whatIsThat: EitherT[List, String, Int] = "Some error".raiseError[MyEither, Int]

    whatIsThat
  }

  println("The error is: " + fullConversionPath())
  println("The error is: " + shorterConversionPath())
  println("The error is: " + shorterSyntaxConversionPath())

  {
    import cats.syntax.applicativeError._
    val tmp: EitherT[List, String, Int] = "Some error".raiseError[EitherT[List, String, ?], Int]
    println("Directly: " + tmp)
  }
  println("The end")

}
