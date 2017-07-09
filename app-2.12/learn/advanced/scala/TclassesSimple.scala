package learn.advanced.scala

import cats.data.OptionT

/**
  * Created by slowi on 08/07/2017.
  */
object TclassesSimple extends App {
  import cats.instances.either._
  import cats.instances.option._
  import cats.syntax.option._
  import cats.syntax.either._
  import cats.Monad
  import cats.syntax.applicative._

  type Error = String

  // Create a type alias, ErrorOr, to convert Either to
  // a type constructor with a single parameter:
  type ErrorOr[A] = Either[Error, A]

  // Use ErrorOr as a type parameter to OptionT:
  type ErrorOptionOr[A] = OptionT[ErrorOr, A]

  val result1: Either[String, Option[Int]] = (41.some).asRight[String]

  val magicType = 41.pure[ErrorOptionOr]

  val stack2: ErrorOptionOr[Int] = OptionT[ErrorOr, Int](
    123.some.asRight[String]
  )

  //NIE OGARNIA TYPOW!!!
//  val stack3: ErrorOptionOr[Int] = OptionT(
//    123.some.asRight[String]
//  )



  println(
    magicType
  )
}
