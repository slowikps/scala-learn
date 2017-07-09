package learn.advanced.scala

import java.util.concurrent.TimeUnit

import cats.data.OptionT
import cats.data.EitherT
import learn.advanced.scala.TclassesSimple.ErrorOr

import scala.concurrent.{Await, Future}


object Tclasses extends App {

  import cats.syntax.option._
  import cats.syntax.either._
  import cats.syntax.applicative._
  import scala.language.postfixOps
  def asFutureOption[A](a: A): Future[Option[A]] =
    Future.successful(a.some)

  def asFutureOptionT[A](a: A): OptionT[Future, A] =
    OptionT(Future.successful(a.some))

  def asFutureEitherT[A](a: A): EitherT[Future, String, A] =
    EitherT(Future.successful(a.asRight[String]))

  val tmp: EitherT[Future, String, Option[Int]] = asFutureEitherT(11.some)

//  type test[A] = EitherT[OptionT[Future[A],A], String, A]

  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.instances.future._
  import scala.concurrent.duration._
  type Error = String
  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  val superb: OptionT[FutureEither, Int] = OptionT[FutureEither, Int](EitherT[Future, String, Option[Int]](
    Future.successful((11.some).asRight[String])
  ))

  val tmp2: Future[Either[String, Int]] = Future.successful(11.asRight[String])
  val eitTmp2: EitherT[Future, String, Int] = EitherT(tmp2)



  val superbMapped = superb.map(_ + 10)
  TimeUnit.MILLISECONDS.sleep(100)
  println(superb)
  println(superbMapped)

  val intTheMiddle1: OptionT[Future, Int] = OptionT(Future.successful(11.some))
//  EitherT.liftT(intTheMiddle1)

  val inTheMiddle: EitherT[Future, String, Int] = EitherT(Future.successful(11.asRight[String]))
//    asFutureEitherT(11)

  val res = for {
    a <- 11.pure[FutureEitherOption]
    b <- 12.pure[FutureEitherOption]
  } yield a + b

  TimeUnit.MILLISECONDS.sleep(100)
  println(res)

  val futureOfOption: Future[Option[Int]] = Future(1.some)

  println(

  )
//  OptionT.

}
