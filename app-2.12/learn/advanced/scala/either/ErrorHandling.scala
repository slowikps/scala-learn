package learn.advanced.scala.either

import cats.data.EitherT

import scala.concurrent.Future

object ErrorHandling extends App {

  type MyErrorType[A] = EitherT[Future, A, Int]

  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.instances.future._
  import cats.syntax.applicative._


  val err = "s"//"Big fat error".pure[MyErrorType]


  println("The error is: " + err)
  println("The end")

}
