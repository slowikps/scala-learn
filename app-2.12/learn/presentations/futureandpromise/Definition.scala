package learn.presentations.futureandpromise

import java.util.concurrent.Executors

import learn.presentations.futureandpromise.utils.FancyLogging

import scala.concurrent.{ExecutionContext, Future}

case class ExpensiveOperationResult(result: String)

object ExpensiveOperation {
  def doIt(): ExpensiveOperationResult = {
    ExpensiveOperationResult("ExpensiveOperationResult")
  }
}

object Definition extends App with FancyLogging {

  /* An `ExecutionContext` can execute program logic asynchronously,
  * typically but not necessarily on a thread pool.
  *
  * An ExecutionContext is similar to an Executor: it is free to execute computations in a new thread, in a pooled thread or in the current thread
  * (although executing the computation in the current thread is discouraged).
  */
  //Java Executors to Scala ExecutionContext
  val executionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  info("App started")
  val inverseFuture: Future[ExpensiveOperationResult] = Future {//Computation is scheduled to start immediately
    info("Doing expensive operation")
    ExpensiveOperation.doIt() // non-blocking long lasting computation
  }(executionContext) //Context is explicitly provided

  import ExecutionContext.Implicits.global // ExecutionContext backed by a ForkJoinPool

  val inverseFuture2: Future[ExpensiveOperationResult] = Future {
    info("Doing expensive operation")
    ExpensiveOperation.doIt() // non-blocking long lasting computation
  }//Context is implicitly provided



  /*
  A Future is an object holding a value which may become available at some point. This value is usually the result of some other computation:

    1 If the computation has not yet completed, we say that the Future is not completed.
    2 If the computation has completed with a value or with an exception, we say that the Future is completed.
      Completion can take one of two forms:

    1 When a Future is completed with a value, we say that the future was successfully completed with that value.
    2 When a Future is completed with an exception thrown by the computation, we say that the Future was failed with that exception.

    A Future has an important property that it may only be assigned once. Once a Future object is given a value or an exception, it becomes in effect immutable– it can never be overwritten.
   */

  //Callbacks


}
