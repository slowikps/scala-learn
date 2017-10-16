package learn.advanced.scala.either

import cats.data.EitherT

import scala.concurrent.Future

object ErrorHandling extends App {

  type MyErrorType[A] = EitherT[Future, A, Int]

  import cats.syntax.applicative._
  import cats.data.EitherT._

  val err = "Big fat error".pure[MyErrorType]


  println("The error is: " + err)
  println("The end")

}
