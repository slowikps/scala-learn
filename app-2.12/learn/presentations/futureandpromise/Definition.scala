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

  /*
    From Scala documentation:
        *Futures* provide a way to reason about performing many operations in parallel– in an efficient and non-blocking way.
        A Future is a placeholder object for a value that may not yet exist

    Akka documentation:
        * This result can be accessed synchronously (blocking) or asynchronously (non-blocking).

    * By default, futures and promises are non-blocking, making use of callbacks instead of typical blocking operations.
      To simplify the use of callbacks both syntactically and conceptually,
      Scala provides combinators such as flatMap, foreach, and filter used to compose futures in a non-blocking way.
      Blocking is still possible - for cases where it is absolutely necessary, futures can be blocked on (although this is discouraged).


  A Future is an object holding a value which may become available at some point.
  This value is usually the result of some other computation:

    1 If the computation has not yet completed, we say that the Future is not completed.
    2 If the computation has completed with a value or with an exception, we say that the Future is completed.
  Completion can take one of two forms:

    1 When a Future is completed with a value, we say that the future was successfully completed with that value.
    2 When a Future is completed with an exception thrown by the computation, we say that the Future was failed with that exception.

  A Future has an important property that it may only be assigned once. Once a Future object is given a value or an exception, it becomes in effect immutable– it can never be overwritten.
 */


//  {
//    Future {
//      info("Doing expensive operation")
//      ExpensiveOperation.doIt()
//    }
//  }





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

  //Callbacks
}
