package learn.advanced.scala.monad

import cats.data.EitherT

import scala.concurrent.Future


object FutureMonadTest extends App {

  import cats.Monad
  import scala.concurrent.Future
  import cats.instances.future._
  import cats.syntax.either._
  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.syntax.applicative._


  type EitherTFuture[B] = EitherT[Future, String, B]
  type EitherTString[B] = EitherT[Future, String, B]
  def liftFutureSuccessfulEitherT[A, B](in: Either[String, B]) = {
    val tmp = (in.pure[Future])
    val tmp2 = in.pure[EitherTString]

    println("Pure: " + tmp)
    println("Pure: " + tmp2)
    ()
  }

  liftFutureSuccessfulEitherT(11.asRight)

  val fm = Monad[Future]

  println(Thread.currentThread().getName)
  println(1.pure)
  ({
    println(Thread.currentThread().getName)
    1
  }).pure
}
