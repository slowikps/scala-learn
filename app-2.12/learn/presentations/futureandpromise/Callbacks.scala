package learn.presentations.futureandpromise


import java.util.concurrent.TimeUnit

import learn.presentations.futureandpromise.utils.FancyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


object Callbacks extends App with FancyLogging {
  /**
    * We now know how to start an asynchronous computation to create a new future value,
    * but we have not shown how to use the result once it becomes available
    */

  Future {
    //Computation is scheduled to start immediately
    info("Doing expensive operation")
    ExpensiveOperation.doIt() // non-blocking long lasting computation
  } onComplete { //Try[ExpensiveOperationResult]
    case Success(ExpensiveOperationResult(result)) => info(s"Success: $result")
    case Failure(ex) => info(s"Failure [ex: $ex]")
  }

  Future {
    throw new IllegalArgumentException("Bleh - exception")
  } onComplete { //Try[ExpensiveOperationResult]
    case Success(_) => info(s"Success")
    case Failure(ex) => info(s"Failure [ex: $ex]")
  }

  TimeUnit.SECONDS.sleep(1)
  /**
    * This callback is called asynchronously once the future is completed.
    * If the future has already been completed when registering the callback,
    * then the callback may either be executed asynchronously, or sequentially on the same thread.
    */

  Future {
    if (true) throw new IllegalArgumentException("Bleh1 - exception")
  } foreach { //onSuccess @deprecated("use `foreach` or `onComplete` instead (keep in mind that they take total rather than partial functions)", "2.12.0")
    case s => info(s"foreach $s")
  }

  Future {
    throw new IllegalArgumentException("Bleh2 - exception")
  }.failed.foreach { //onFailure @deprecated("use `onComplete` or `failed.foreach` instead (keep in mind that they take total rather than partial functions)", "2.12.0")
    //A bit confusing though!
    case s => info(s"failed.foreach part: $s")
  }

  /**
    * Furthermore, the order in which the callbacks are executed is not predefined,
    * even between different runs of the same application.
    * In fact, the callbacks may not be called sequentially one after the other, but may concurrently execute at the same time.
    */


  //NEXT Functional Composition and For-Comprehensions:
  // https://docs.google.com/presentation/d/1tHiogBGCkFsDBFVV4MVenK9t9QSJmSV6jcPK_oFsdpQ/edit#slide=id.g2251017f85_0_11

  TimeUnit.SECONDS.sleep(1)
}
