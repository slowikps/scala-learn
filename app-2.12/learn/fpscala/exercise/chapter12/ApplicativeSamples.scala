package learn.fpscala.exercise.chapter12

import learn.fpscala.exercise.chapter12.ApplicativeSamples.Validation


object ApplicativeSamples {

  val streamApplicative = new Applicative[Stream] {
    override def map2[A, B, C](ma: Stream[A], mb: Stream[B])(f: (A, B) => C): Stream[C] =
      (ma zip mb).map(f.tupled)

    override def unit[A](a: => A): Stream[A] = Stream.continually(a)
//    override def sequence[A](a: List[Stream[A]]): Stream[List[A]] = //aha it is already implemented
//      a.foldRight(Stream.empty[List[A]])((s, acc) => map2(s, acc)(_ :: _))
  }

  sealed trait Validation[+E, +A]

  case class Failure[E](head: E, tail: Vector[E] = Vector()) extends Validation[E, Nothing]
  case class Success[A](a: A) extends Validation[Nothing, A]


  def validationApplicative[E]: Applicative[({type f[x] = Validation[E,x]})#f] = {
    new Applicative[({type f[x] = Validation[E,x]})#f] {

      override def map2[A, B, C](ma: Validation[E, A], mb: Validation[E, B])(f: (A, B) => C): Validation[E, C] =
        (ma, mb) match {
          case (Success(s1), Success(s2)) => Success(f(s1, s2))
          case (Failure(h1, t1), Failure(h2, t2)) => Failure(h1, t1 ++ (h2 +: t2))
//          case ((f@Failure(_, _), _) | (_, f@Failure(_, _))) => f  Not possible ; /
          case (f@Failure(_, _), _) => f
          case (_, f@Failure(_, _)) => f
        }

      override def unit[A](a: => A): Validation[E, A] = Success(a)
    }
  }

}

object Test extends App {
  val ints = Stream(1,2,3,4)
  val strings = Stream("jeden", "dwa", "trzy", "cztery")

  println {
    ApplicativeSamples.streamApplicative.sequence(List(ints, strings)).toList
  }
}
