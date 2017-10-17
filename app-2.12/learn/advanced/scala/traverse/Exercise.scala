package learn.advanced.scala.traverse

import cats.Applicative

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Exercise extends App {

  import cats.instances.future._
  import cats.syntax.applicative._
  import cats.syntax.cartesian._

  import scala.language.higherKinds

  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60) // just for demonstration

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum |@| func(item)).map(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )

  println(
    Await.result(
      listTraverse(hostnames)(getUptime),
      1.second
    ))

  print("The is the end")


  import cats.instances.vector._
  println(
    listSequence(List(Vector(1, 2), Vector(3, 4)))
  )


  //Option
  import cats.instances.list._
  import cats.instances.option._
  import cats.syntax.option._
  import cats.syntax.traverse._

  val listOfOption = List(1.some, 2.some, 3.some)

  println(listOfOption)
  println(listOfOption.sequence)

  println(
    (1 to 10).toList.traverse(_.some)
  )


}
