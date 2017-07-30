package learn.advanced.scala.validated

import cats.data.Validated

object Usage extends App {

  type AllErrorsOr[A] = Validated[List[String], A]

  import cats.instances.string._
  import cats.instances.list._
  import cats.syntax.cartesian._
  import cats.syntax.validated._ //potrzebuje semigroup do laczenia!

  println(
    ("Blad".invalid[Int] |@| "Error".invalid[Int]).tupled
  )
  println(
    (List("Blad").invalid[Int] |@| List("Error").invalid[Int]).tupled
  )

  //Only first error
  println(
  "String".valid[List[String]]
      .ensure("To Short")(_.length > 10)
      .ensure("Doesn't contain A")(_.contains('a'))
  )
}
