package learn.advanced.scala.monadtransformers

object Samples extends App {

  import cats.data.OptionT

  type ListOption[A] = OptionT[List, A]

  import cats.instances.list._
  import cats.syntax.applicative._

  val result: ListOption[Int] = 42.pure[ListOption]

  println(result)
}
