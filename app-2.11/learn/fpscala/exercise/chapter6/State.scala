package learn.fpscala.exercise.chapter6

trait RNG {
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.
}

object RNG {

  type Rand[+A] = RNG => (A, RNG)

  implicit class RNGOps(r: RNG) {
    def intDouble = RNG.intDouble(r)

  }

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed)
      // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }

  def nonNegativeIntMy(rng: RNG): (Int, RNG) = {
    // Rand[Int]
    val (res, gen) = rng.nextInt
    if (res == Int.MinValue) (-(Int.MinValue + 1), gen)
    else if (res < 0) (-res, gen)
    else (res, gen)
  }

  def nonNegativeInt: Rand[Int] = { rng =>
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

  def doubleOld(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    (i.toDouble / Int.MaxValue, r)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r2) = rng.nextInt
    val (d, r3) = double(r2)
    ((i, d), r3)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (id, r) = rng.intDouble
    (id.swap, r)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) =
    if (count == 0) (List(), rng)
    else {
      val (i, r) = rng.nextInt
      val (l, r2) = ints(count - 1)(r)
      (i :: l, r2)
    }

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = (a, _)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = rng => {
    val (a, r) = s(rng)
    (f(a), r)
  }

  def double: Rand[Double] = map(nonNegativeInt)(_.toDouble / Int.MaxValue)

  def nonNegativeEven: Rand[Int] = map(nonNegativeInt)(i => i - i % 2)

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    rng => {
      val (a, r1) = ra(rng)
      val (b, r2) = rb(r1)
      (f(a, b), r2)
    }

  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = map2(ra, rb)((_, _))

  def _intDouble: Rand[(Int, Double)] = both(int, double)

  def _DoubleInt: Rand[(Double, Int)] = map(_intDouble)(_.swap)

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
  //fs.foldLeft(unit(Nil: List[A]))(map2(_, _)(_ :+ _))
    fs.foldRight(unit(Nil: List[A]))(map2(_, _)(_ :: _))


  def _ints(count: Int): Rand[List[Int]] = sequence(List.fill(count)(int))

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = rng => {
    val (a, r1) = f(rng)
    g(a)(r1)
  }

  def nonNegativeLessThan(n: Int): Rand[Int] = {
    flatMap(nonNegativeInt) { i =>
      val mod = i % n
      if (i + (n - 1) - mod >= 0) unit(mod) else nonNegativeLessThan(n)
    }
  }

  def _map[A, B](r: Rand[A])(f: A => B): Rand[B] = flatMap(r)(a => unit(f(a)))

  def _map2[A, B, C](a: Rand[A], b: Rand[B])(f: (A, B) => C): Rand[C] = flatMap(a)(a => map(b)(b => f(a, b)))

  //flatMap(ra)(a => map(rb)(b => f(a, b)))


}


case class State[S, +A](run: S => (A, S)) {
  //map2 sequence

  def map[B](f: A => B): State[S, B] =
    flatMap(a => State.unit(f(a)))

  def map2[B, C](s: State[S, B])(f: (A, B) => C): State[S, C] =
      flatMap(a => s.map(b => f(a,b)))

  def _map2[B, C](rb: State[S, B])(f: (A, B) => C): State[S, C] =
    for {
      a <-  this
      b <-  rb
    }yield (f(a,b))

  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a, b) = run(s)
    f(a).run(b)
  })

}

object State {
  type Rand[A] = State[RNG, A]

  def unit[S, A](a: A): State[S, A] = State(s => (a, s))

  def sequence[S, A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.foldLeft(unit[S, List[A]](Nil))((acc, x) => x.map2(acc)(_ :: _))

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get // Gets the current state and assigns it to `s`.
    _ <- set(f(s)) // Sets the new state to `f` applied to `s`.
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

  sealed trait Input
  case object Coin extends Input
  case object Turn extends Input

  case class Machine(locked: Boolean, candies: Int, coins: Int)

  object Candy {
    def update: (Input) => (Machine) => Machine = (i: Input) => (s: Machine) =>
      (i, s) match {
        case (_, Machine(_, 0, _)) => s
        case (Coin, Machine(false, _, _)) => s
        case (Turn, Machine(true, _, _)) => s
        case (Coin, Machine(true, candy, coin)) => Machine(false, candy, coin + 1)
        case (Turn, Machine(false, candy, coin)) => Machine(true, candy - 1, coin)
      }

    def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = for {
      _ <- sequence(inputs map (modify[Machine] _ compose update))
      s <- get
    } yield (s.coins, s.candies)
  }
}

