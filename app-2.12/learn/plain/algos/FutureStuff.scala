package learn.plain.algos

import java.util.concurrent.TimeUnit

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureStuff extends App{
  println("Start")

  var in: List[Future[Int]] = List(Future.successful(1), Future.failed(new RuntimeException("Boom")), Future.successful(2), Future.successful(3))

  in = in.map(_.recover{case _ => 0})

  Future.traverse(List(1,2,3,4))(in => Future.successful(in)).map(_.sum).map(println)
  Future.sequence(in).map(_.sum).map(in => println(s"ho ho ho: $in"))

  in.reduce((f1, f2) => f1.flatMap(r1 => f2.map(_ + r1))).map(println)
  in.foldLeft(Future.successful(0))((acc, x) => acc.flatMap(acc1 => x.map(_ + acc1))).map(println)
  TimeUnit.SECONDS.sleep(2)

}
