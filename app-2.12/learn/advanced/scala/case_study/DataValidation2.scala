package learn.advanced.scala.case_study

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

object DataValidation2 extends App {

  import cats.Semigroup
  import cats.syntax.validated._
  import cats.syntax.semigroup._ // |+| syntax
  import cats.instances.list._ // Semigroup for List
  import cats.syntax.cartesian._ // |@| syntax

  //Second

  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] =
      And(this, that)

    def or(that: Check[E, A]): Check[E, A] =
      Or(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
      this match {
        case Pure(func) =>
          func(a)

        case And(left, right) => (left(a) |@| right(a)).map((_, _) => a)
        case Or(left, right) =>
          left(a) match {
            case Valid(a)    => Valid(a)
            case Invalid(e1) =>
              right(a) match {
                case Valid(a)    => Valid(a)
                case Invalid(e2) => Invalid(e1 |+| e2)
              }
          }
      }
  }

  final case class And[E, A](left: Check[E, A], right: Check[E, A])
      extends Check[E, A]

  final case class Or[E, A](left: Check[E, A], right: Check[E, A])
      extends Check[E, A]

  final case class Pure[E, A](func: A => Validated[E, A]) extends Check[E, A]

  val a: Check[List[String], Int] =
    Pure { v =>
      if (v > 2) v.valid
      else List("Must be > 2").invalid
    }
  // a: wrapper.Check[List[String],Int] = Pure(<function1>)

  val b: Check[List[String], Int] =
    Pure { v =>
      if (v < -2) v.valid
      else List("Must be < -2").invalid
    }
  // b: wrapper.Check[List[String],Int] = Pure(<function1>)

  val check = a and b

  println("Second")
  println(check(1))
  println("Second End")

}
