package learn.presentations.futureandpromise

import java.util.concurrent.TimeUnit

import learn.presentations.futureandpromise.utils.FancyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

object PromisesExample extends App with FancyLogging {

  /**
    * Promise can be thought of as a writable, single-assignment container,
    */

  val promise = Promise[Int]()
  val future = promise.future

  future.foreach {
    case res => info(s"I got: $res")
  }

  promise success 23

  TimeUnit.SECONDS.sleep(1)

  /**
    * In some cases the client may want to complete the promise only if it has not been completed yet
    * (e.g., there are several HTTP requests being executed from several different futures
    * and the client is interested only in the first HTTP response -
    * corresponding to the first future to complete the promise).
    * For these reasons methods tryComplete, trySuccess and tryFailure exist on future.
    * The client should be aware that using these methods results in programs which are not deterministic, but depend on the execution schedule.
    */
  //  promise trySuccess {
  //    info("Try to finish Promise again - which is ok")
  //    1
  //  }

  def first[T](f: Future[T], g: Future[T]): Future[T] = {
    val p = Promise[T]
    f foreach  {
      case x => p.trySuccess(x)
    }
    g foreach {
      case x => p.trySuccess(x)
    }
    p.future
  }

  //  2) Illegal:
  //  promise failure new IllegalArgumentException("You cannot change value of the Promise")
}
