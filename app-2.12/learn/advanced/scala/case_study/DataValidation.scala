package learn.advanced.scala.case_study

object DataValidation extends App {

  import cats.Semigroup
  import cats.syntax.either._ // asLeft and asRight syntax
  import cats.syntax.semigroup._ // |+| syntax
  { //First
    final case class CheckF[E, A](func: A => Either[E, A]) {
      def apply(a: A): Either[E, A] =
        func(a)

      def and(that: CheckF[E, A])(implicit s: Semigroup[E]): CheckF[E, A] =
        CheckF { a =>
          (this(a), that(a)) match {
            case (Left(e1), Left(e2))   => (e1 |+| e2).asLeft
            case (Left(e), Right(a))    => e.asLeft
            case (Right(a), Left(e))    => e.asLeft
            case (Right(a1), Right(a2)) => a.asRight
          }
        }
    }

    import cats.instances.list._ // Semigroup for List

    val a: CheckF[List[String], Int] =
      CheckF { v =>
        if (v > 2) v.asRight
        else List("Must be > 2").asLeft
      }
    // a: CheckF[List[String],Int] = CheckF(<function1>)

    val b: CheckF[List[String], Int] =
      CheckF { v =>
        if (v < -2) v.asRight
        else List("Must be < -2").asLeft
      }
    // b: CheckF[List[String],Int] = CheckF(<function1>)

    val check = a and b

    println("First")
    println(check.func(0))
    println(check.func(10))
    println("First End")

  }

    //Second
  {
    import cats.instances.list._ // Semigroup for List

    sealed trait Check[E, A] {
      def and(that: Check[E, A]): Check[E, A] =
      And(this, that)

      def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] =
      this match {
        case Pure(func) =>
          func(a)

        case And(left, right) =>
          (left(a), right(a)) match {
            case (Left(e1), Left(e2))   => (e1 |+| e2).asLeft
            case (Left(e), Right(a))    => e.asLeft
            case (Right(a), Left(e))    => e.asLeft
            case (Right(a1), Right(a2)) => a.asRight
          }
      }
    }

    final case class And[E, A](left: Check[E, A], right: Check[E, A])
    extends Check[E, A]

    final case class Pure[E, A](func: A => Either[E, A]) extends Check[E, A]

    val a: Check[List[String], Int] =
    Pure { v =>
      if(v > 2) v.asRight
      else List("Must be > 2").asLeft
    }
    // a: wrapper.Check[List[String],Int] = Pure(<function1>)

    val b: Check[List[String], Int] =
    Pure { v =>
      if(v < -2) v.asRight
      else List("Must be < -2").asLeft
    }
    // b: wrapper.Check[List[String],Int] = Pure(<function1>)

    val check = a and b

    println("Second")
    println(check(1))
    println("Second End")
  }

}
