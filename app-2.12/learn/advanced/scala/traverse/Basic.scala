package learn.advanced.scala.traverse
import scala.language.postfixOps
object Basic extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._
  import scala.concurrent.duration._

  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )
  val allUptimes: Future[List[Int]] = {
    import cats.syntax.applicative._
    import cats.instances.future._

    hostnames.foldLeft(List.empty[Int].pure[Future])(
      (accF, elem) => {

        for {
          acc <- accF
          x <- getUptime(elem)
        } yield x :: acc
      }
    )
  }

  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60) // just for demonstration

  println(
    Await.result(
      allUptimes,
      1 second
    ))
}


