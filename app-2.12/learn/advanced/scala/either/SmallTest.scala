package learn.advanced.scala.either

import cats.data.EitherT


import scala.concurrent.Future

object SmallTest extends App {

  type MyType[A] = EitherT[Future, Int, A]

  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.instances.future._
  import cats.syntax.applicative._

  val pureTest: MyType[String] = "success".pure[MyType]

  println("?Pure test: " + pureTest)

}
