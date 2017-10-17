package learn.presentations.futureandpromise

import java.util.concurrent.TimeUnit

import learn.presentations.futureandpromise.utils.FancyLogging

import scala.concurrent.Future


object FuturesAgain extends App with FancyLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  val expensiveTask = Future {
    TimeUnit.MILLISECONDS.sleep(100)
    info("expensiveTask done")
    1
  }

  val expensiveTask2 = Future {
    TimeUnit.MILLISECONDS.sleep(100)
    info("expensiveTask done")
    1
  }

  val sum: Future[Int] = for {
    x <- expensiveTask
    y <- Future(11)
  } yield (x + y)

  sum.foreach(println)






  TimeUnit.SECONDS.sleep(5)
}
