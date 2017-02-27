package learn.fpscala.exercise.chapter6

trait RNG {
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.
}

object RNG {
  // NB - this was called SimpleRNG in the book text

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (nextVal, nextRng) = rng.nextInt
    (Math.abs(nextVal), nextRng)
  }

  def nonNegativeIntBook(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (x, gen) = nonNegativeIntBook(rng)
    (x / (Int.MaxValue.toDouble + 1), gen)
  }

  def doubleViaMap: Rand[Double] = map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))


  def intDouble(rng: RNG): ((Int,Double), RNG) = {
    val (first, gen1) = rng.nextInt
    val (second, gen2) = double(gen1)
    ((first, second), gen2)
  }

  def doubleInt(rng: RNG): ((Double,Int), RNG) = {
    val ((newInt, newDouble), newGen) = intDouble(rng)
    ((newDouble, newInt), newGen)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    (0 until count).foldLeft((List[Int](), rng))((acc, _) => {
      val (i, rng) = acc._2.nextInt
      (i :: acc._1, rng)
    })
  }

  def map2[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    (rng1) => {
      val (a, rng2) = ra(rng1)
      val (b, rng3) = rb(rng2)
      (f(a, b), rng3)
    }

  def both[A,B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = map2(ra, rb)((_, _))

  def randIntDouble: Rand[(Int, Double)] = both(int, double)

  def sequenceRec[A](fs: List[Rand[A]]): Rand[List[A]] = (rng1) => fs match {
    case x :: xs => {
      val (v, rng2) = x(rng1)
      val tmp = sequenceRec(xs)(rng2)
      (v :: tmp._1, tmp._2)
    }
    case _ => (Nil, rng1)
  }

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = (rng1) => fs.foldLeft((Nil: List[A], rng1)){
    case ((res, rng2), x) => {
      val (v, rng3) = x(rng2)
      (v :: res, rng3)
    }
  }

  //Clever stuff:
  def sequenceBook[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight(unit(List[A]()))((f: Rand[A], acc: Rand[List[A]]) => map2(f, acc)(_ :: _))

  def intsViaSequence(count: Int): Rand[List[Int]] = sequence(List.fill(count)(int))

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = ???
}

case class State[S,+A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] =
    ???
  def map2[B,C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    ???
  def flatMap[B](f: A => State[S, B]): State[S, B] =
    ???
}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object State {
  type Rand[A] = State[RNG, A]
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???
}