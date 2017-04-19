package learn.fpscala.exercise.chapter11

trait Monad[F[_]] {

  def unit[A](a: => A): F[A]

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

  def sequenceBook[A](lma: List[F[A]]): F[List[A]] =
    lma.foldRight(unit(List[A]()))((ma, mla) => map2(ma, mla)(_ :: _))

  def map2[A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] = flatMap(ma)(a => map(mb)(b => f(a, b)))

  def map[A, B](ma: F[A])(f: A => B): F[B] = flatMap(ma)(a => unit(f(a)))

  def travers[A, B](la: List[A])(f: A => F[B]): F[List[B]] = sequence(la.map(f)) //I guess this should be implemented in terms of primitives, book implementation is efficient but mine is nice :)

  def sequence[A](lma: List[F[A]]): F[List[A]] =
    lma.foldRight(unit(List[A]()))((x, acc) => flatMap(acc)(xs => map(x)(_ :: xs))) //Ech I could use map2!!

  def traverseBook[A, B](la: List[A])(f: A => F[B]): F[List[B]] =
    la.foldRight(unit(List[B]()))((a, mlb) => map2(f(a), mlb)(_ :: _))

  def replicateM[A](n: Int, ma: F[A]): F[List[A]] = (1 until n).foldLeft(unit(List[A]()))((acc, _) => map2(ma, acc)(_ :: _))

  def forever[A, B](a: F[A]): F[B] = {
    lazy val t: F[B] = forever(a)
    flatMap(a)(_ => t)
  }

  //List.fill(n)(ma) better than my stupid 1 unit n

  // Recursive version:
  def _replicateMBook[A](n: Int, ma: F[A]): F[List[A]] =
    if (n <= 0) unit(List[A]()) else map2(ma, replicateM(n - 1, ma))(_ :: _)

  // Using `sequence` and the `List.fill` function of the standard library:
  def replicateMBook[A](n: Int, ma: F[A]): F[List[A]] =
    sequence(List.fill(n)(ma))

  def filterMBook[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
    ms match {
      case Nil => unit(Nil)
      case h :: t => flatMap(f(h))(b =>
        if (!b) filterM(t)(f)
        else map(filterM(t)(f))(h :: _))
    }

  def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
    ms.foldRight(unit(List[A]()))((x, acc) => map2(f(x), acc)((exp, xs) => if (exp) x :: xs else xs))

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(b => g(b))

  def composeBook[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(g)

  def flatMapInCompose[A, B](ma: F[A])(f: A => F[B]): F[B] = {
    def maToFun(a: Unit) = ma // _: Unit instead ; /
    compose(maToFun, f)(())
  }

  def flatMapBook[A,B](ma: F[A])(f: A => F[B]): F[B] =
    compose((_:Unit) => ma, f)(())

  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(ma => ma)
}
case class Id[A](value: A) {
  def flatMap[B](f: A => Id[B]): Id[B] = f(value)
  def map[B](f: A => B): Id[B] = Id(f(value))
}

object Monad {

  val idMonad = new Monad[Id] {
    override def unit[A](a: => A): Id[A] = Id(a)
    override def flatMap[A, B](ma: Id[A])(f: (A) => Id[B]): Id[B] = ma.flatMap(f)
  }

  val optionMonad = new Monad[Option] {

    override def unit[A](a: => A): Option[A] = Option(a)

    override def flatMap[A, B](ma: Option[A])(f: (A) => Option[B]): Option[B] = ma.flatMap(f)
  }

  val streamMonad = new Monad[Stream] {
    override def unit[A](a: => A): Stream[A] = Stream(a)

    override def flatMap[A, B](ma: Stream[A])(f: (A) => Stream[B]): Stream[B] = ma.flatMap(f)
  }

  val listMonda = new Monad[List] {
    override def unit[A](a: => A): List[A] = List(a)

    override def flatMap[A, B](ma: List[A])(f: (A) => List[B]): List[B] = ma.flatMap(f)
  }

  //  val stateMonad = new Monad[State] {
  //    override def unit[A, B](a: => A): State[A, B] = ???
  //    override def flatMap[A, B, C](ma: State[A, B])(f: (A) => State[A, B]): State[A, C] = ???
  //  }
}
