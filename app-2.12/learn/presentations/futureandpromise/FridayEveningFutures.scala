package learn.presentations.futureandpromise

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}


object FridayEveningFutures extends App {
  val start = System.currentTimeMillis

  def info(msg: String) = printf("%.2f: %s\n", (System.currentTimeMillis - start) / 1000.0, msg)

  def provide(name: String) = {
    TimeUnit.SECONDS.sleep(1)
    info(s"Friday treat done: $name")
    FridayTreat(name)
  }

  def consume(treat: FridayTreat) = info(s"About to consume: $treat")

  {//Step 2
    val treat = for {
      bear <- Future(provide("beer"))
      pizza <- Future(provide("pizza"))
    } yield (consume(bear and pizza))

    Await.result(treat, 5 seconds)
  }

  {//Step 3
    val futBear  = Future(provide("beer"))
    val futPizza = Future(provide("pizza"))
    val treat = for {
      bear <- futBear
      pizza <- futPizza
    } yield (consume(bear and pizza))
    Await.result(treat, 5 seconds)
  }

}