package learn.advanced.scala.monadtransformers

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

object Autobots extends App {

  type Response[A] = EitherT[Future, String, A]

  import cats.instances.future._
  import cats.syntax.applicative._
  import cats.syntax.either._
  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  def getPowerLevelNoError(autobot: String): Response[Int] =
    powerLevels(autobot).pure[Response]

  def getPowerLevelWithError(autobot: String): Response[Int] =
    EitherT(
      Future.successful(
        Either.fromOption(powerLevels.get(autobot), s"$autobot unreachable")
      ))

  def getPowerLevelWithError2(autobot: String): Response[Int] =
    EitherT(
    Either.fromOption(powerLevels.get(autobot), s"$autobot unreachable").pure
    )

  def getPowerLevel(ally: String): Response[Int] = {
    powerLevels.get(ally) match {
      case Some(avg) => EitherT.right(Future(avg))
      case None => EitherT.left(Future(s"$ally unreachable"))
    }
  }

  def canSpecialMove(
                      ally1: String,
                      ally2: String
                    ): Response[Boolean] =
    for {
      pl1 <- getPowerLevel(ally1)
      pl2 <- getPowerLevel(ally2)
    } yield (pl1 + pl2) > 15

  import scala.concurrent.duration._
  def tacticalReport(
                      ally1: String,
                      ally2: String
                    ): String =
    Await.result(
      canSpecialMove(ally1, ally2).value,
      1.second
    ) match {
      case Left(msg) =>
        s"Comms error: $msg"
      case Right(true)  =>
        s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) =>
        s"$ally1 and $ally2 need a recharge."
    }
}
