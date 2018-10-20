package learn.advanced.scala.case_study

import cats.{Applicative, Id, Traverse}

import scala.concurrent.Future
import cats.instances.future._
import cats.instances.list._
import cats.syntax.traverse._
import scala.concurrent.ExecutionContext.Implicits.global

trait UptimeClient[F[_]] {
  def getUptime(hostname: String): F[Int]
}

trait RealUptimeClient extends UptimeClient[Future] {
  override def getUptime(hostname: String): Future[Int] //Can be omitted
}

class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
  def getUptime(hostname: String): Id[Int] =
  hosts.getOrElse(hostname, 0)
}

//class UptimeService[F[_]](client: UptimeClient[F])(implicit val ap: Applicative[F]) {
import cats.syntax.functor._ //To have map function!
class UptimeService[F[_]: Applicative](client: UptimeClient[F]) { //Context bound
  def getTotalUptime(hostnames: List[String]): F[Int] = {
    //Future.traverse(hostnames)(client.getUptime).map(_.sum)
//    Traverse[List].traverse(hostnames)(client.getUptime)//same as below
    hostnames.traverse(client.getUptime).map(_.sum)
  }
}
//class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient {
//  def getUptime(hostname: String): Future[Int] =
//    Future.successful(hosts.getOrElse(hostname, 0))
//}
object Uptime extends App {

  def testTotalUptime() = {
    val hosts    = Map("host1" -> 10, "host2" -> 6)
    val client   = new TestUptimeClient(hosts)
    val service  = new UptimeService(client)
    val actual   = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    println("About to run the assertion")
    assert(actual == expected)
  }

  testTotalUptime()


  println("The end")
}


class OldUptimeService(client: UptimeClient[Future]) {
  def getTotalUptime(hostnames: List[String]): Future[Int] = {
    //Future.traverse(hostnames)(client.getUptime).map(_.sum)
    Traverse[List].traverse(hostnames)(client.getUptime)//same as below
    hostnames.traverse(client.getUptime).map(_.sum)
  }
}