package learn.presentations.futureandpromise

import java.util.concurrent.TimeUnit

import learn.presentations.futureandpromise.utils.FancyLogging

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object FridayEveningFutures extends App with FancyLogging {

  def provide(name: String) = {
    TimeUnit.SECONDS.sleep(1)
    info(s"Friday treat done: $name")
    FridayTreat(name)
  }

  def consume(treat: FridayTreat) = info(s"About to consume: $treat")




  //Definition First!
//  {
//    //Step 1 - ugly way
//    val futBear = Future(provide("beer"))
//    val futPizza = Future(provide("pizza"))
//    //Blocking
//    Await.result(futBear, 5 seconds)
//    Await.result(futPizza, 5 seconds)
//
//    consume(futBear.value.get.get and futPizza.value.get.get)
//  }
//








  {
    //Step 2 - not really working
    val treat = for {
      bear <- Future(provide("beer"))
      pizza <- Future(provide("pizza"))
    } yield (consume(bear and pizza))

    Await.result(treat, 5 seconds)
  }









//
//  {
//    //Step 3
//    val futBear = Future(provide("beer"))
//    val futPizza = Future(provide("pizza"))
//
//    val treat = for {
//      bear <- futBear
//      pizza <- futPizza
//    } yield (consume(bear and pizza))
//    Await.result(treat, 5 seconds)
//  }
//
//
//
//  println(
//    Future.successful("No need to context switch")
//  )
}