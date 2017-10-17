package learn.advanced.scala

import java.util.concurrent.TimeUnit

import cats.data.{EitherT, OptionT}

import scala.concurrent.Future

object Tclasses extends App {

  import cats.instances.future._
  import cats.syntax.applicative._
  import cats.syntax.either._
  import cats.syntax.option._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.language.postfixOps

  type Error = String

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  type FutureOption[A] = OptionT[Future, A]
  type FutureOptionEither[A] = EitherT[FutureOption, Error, A]

  val superb: OptionT[FutureEither, Int] = OptionT[FutureEither, Int](
    EitherT[Future, String, Option[Int]](
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

  val inTheMiddle: EitherT[Future, String, Int] = EitherT(
    Future.successful(11.asRight[String]))
  //    asFutureEitherT(11)

  val res = for {
    a <- 11.pure[FutureEitherOption]
    b <- 12.pure[FutureEitherOption]
  } yield a + b

  val pure: FutureOption[Option[Int]] = none[Int].pure[FutureOption]

  val futureOptionEither = 1.pure[FutureOptionEither]
  val futureEitherOption = 1.pure[FutureEitherOption]

  TimeUnit.MILLISECONDS.sleep(100)
  println(res)
  println("futureOptionEither: " + futureOptionEither)
  println("futureEitherOption: " + futureEitherOption)

  def getFancyInt(in: Int): EitherT[FutureOption, String, Int] =
    EitherT[FutureOption, String, Int] {
      OptionT[Future, Either[String, Int]] {
        Future.successful((in.asRight[String]).some)
      }
    }

  val res2 = for {
    a <- getFancyInt(30)
    b <- getFancyInt(35)
  } yield (a + b)
  println(
    "futureOptionEither.getOrElse(11): " + futureOptionEither.getOrElse(11)
  )

  TimeUnit.MILLISECONDS.sleep(100)
  futureOptionEither.map((el: Int) => println("Map has direct access: " + el))
  println("futureOptionEither.value: " + futureOptionEither.value)
  println("futureOptionEither.value.value: " + futureOptionEither.value.value)
  println(
    "futureOptionEither.value.isDefined: " + futureOptionEither.value.isDefined)


  val myTest = 11.pure[EitherT[Future,String, ?]]


  println("myTest [pure test]: " + myTest)

  TimeUnit.MILLISECONDS.sleep(100)
}
