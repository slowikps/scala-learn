package learn.plain.fut

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object ForCompFuture {

  def main(args: Array[String]): Unit = {
    val res = for {
      x <- getInt(5)
      _ = println("In the middle of for")
      y <- getInt(8)
      _ = println("At the end of for")
    } yield (x + y)

    TimeUnit.SECONDS.sleep(1)
    println(res)

    println("Before Future")
    val res2 = for {
      x <- Future {
        TimeUnit.MILLISECONDS.sleep(100)
        Random.nextInt(10)
      }
      _ = println(s"Got x: $x")
      y <- if (x > 5) Future {
        TimeUnit.MILLISECONDS.sleep(100)
        11
      } else {
        Future {
          TimeUnit.MILLISECONDS.sleep(100)
          1
        }
      }
    } yield (x + y)

    TimeUnit.SECONDS.sleep(1)
    println(res2)
  }

  def getInt(in: Int): Future[Int] =
    Future {
      TimeUnit.MILLISECONDS.sleep(100)
      println(s"Hej ho - about to return result: $in")
      in
    }.flatMap { in =>
      Future {
        TimeUnit.MILLISECONDS.sleep(100)
        println(s"Hej ho - about to return result: $in + 1")
        in + 1
      }
    }

}
