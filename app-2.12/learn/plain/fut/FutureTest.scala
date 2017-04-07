package learn.plain.fut

import java.util.concurrent.TimeUnit

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureTest extends App {

  def getFailed(condition: Boolean) =
    if(condition) Future.failed(new RuntimeException)
    else Future.successful(11)

  (
    for {
      i <- Future.successful(11)
      _ <- getFailed(true)
    } yield (i)
    ).map(in => println(s"Failed: $in"))

  (
    for {
      i <- Future.successful(11)
      _ <- getFailed(false)
    } yield (i)
  ).map(in => println(s"Successful: $in"))

  (
    for {
      i <- Future.successful(11)
      _ = getFailed(true)
    } yield (i)
  ).map(in => println(s"Successful with equal: $in"))

  TimeUnit.SECONDS.sleep(1)

}
