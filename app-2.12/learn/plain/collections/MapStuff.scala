package learn.plain.collections

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MapStuff extends App {

  val m = Map(1 -> "jeden", 2 -> "dwa")

//  println(
//    for{
//      nr <- 1 to 10
//      futRes = Future.successful{nr + 3}
//      futValue <- futRes
//    }yield futValue
//  )

//  println(
//    (1 to 10)
//      .map(in => Future.successful(in + 1))
//      .map(_.map())
//  )

  println(
    for{
      (key, value) <- m
    } yield key
  )
}
