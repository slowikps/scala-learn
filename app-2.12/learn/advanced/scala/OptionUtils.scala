package learn.advanced.scala

import cats.data.Validated

import scala.concurrent.Future


object OptionUtils extends App {

  import cats.syntax.option._

  val maybeInt: Option[Int] = 1.some
  val noneInt: Option[Int] = none

  val dupa: String = null

  import cats.instances.future._

  print(
    dupa.some
  )
}
