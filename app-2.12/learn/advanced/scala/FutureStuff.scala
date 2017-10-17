package learn.advanced.scala

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
object FutureStuff extends App {

  val vec: Vector[Int] = Vector()

  vec :+ 1

  println(vec)

  def waitForSomething(in: Int): Future[Int] = {
    Future {
      TimeUnit.MILLISECONDS.sleep(400)
      println(s"Done for input: $in")
      in
    }
  }
  def longWait(in: Int): Future[Int] = {
    Future {
      TimeUnit.MILLISECONDS.sleep(1000)
      println(s"Done for input: $in")
      in
    }
  }

  val result = for {
    res <- waitForSomething(1)
    _ <- waitForSomething(2)
    _ = longWait(3)
    _ <- waitForSomething(4)
  } yield (res)

  println("Waiting")
  Await.result(result, 10 seconds)
  println("The end")

  TimeUnit.SECONDS.sleep(1)
//  println(res)
}
