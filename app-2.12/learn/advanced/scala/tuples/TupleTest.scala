package learn.advanced.scala.tuples

import cats.Cartesian

object TupleTest extends App {

  import cats.instances.tuple._
  import cats.instances.option._
  import cats.syntax.option._
  import cats.syntax.cartesian._

  val t2 = (1, "A")

  println(
    ("11".some |@| 12.some).tupled
  )
  val nullableBoolean: java.lang.Boolean = null
  if (nullableBoolean) {
    println("Hej ho")
  } else {
    println("No no ")
  }

  val sym = 'pawel


  import cats.instances.function._
  import cats.syntax.contravariant._

  val div2: Int => Double = _ / 2.0
  val add1: Int => Int = in => {
    println(s"Here we are: $in")
    in + 1
  }

  val contra: (Int) => Double = div2.contramap(add1)

  println("First: " + div2(2))
  println("Second: " + contra(2))

}
