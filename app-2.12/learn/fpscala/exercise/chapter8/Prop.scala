package learn.fpscala.exercise.chapter8


import learn.fpscala.exercise.chapter6.{RNG, State}
import learn.fpscala.exercise.chapter8.Prop._
import learn.fpscala.exercise.chapter5.Stream._

case class Prop(run: (MaxSize,TestCases,RNG) => Result) {
  def &&(p: Prop) = Prop {
    (max,n,rng) => run(max,n,rng) match {
      case Passed | Proved => p.run(max, n, rng)
      case x => x
    }
  }

  def ||(p: Prop) = Prop {
    (max,n,rng) => run(max,n,rng) match {
      // In case of failure, run the other prop.
      case Falsified(msg, _) => p.tag(msg).run(max,n,rng)
      case x => x
    }
  }

  /* This is rather simplistic - in the event of failure, we simply prepend
   * the given message on a newline in front of the existing message.
   */
  def tag(msg: String) = Prop {
    (max,n,rng) => run(max,n,rng) match {
      case Falsified(e, c) => Falsified(msg + "\n" + e, c)
      case x => x
    }
  }
}
case class SGen[A](forSize: Int => Gen[A]) {

  def apply(n: Int): Gen[A] = forSize(n)
}

case class Gen[A](sample: State[RNG, A]) {

  def unsized: SGen[A] = SGen(_ => this)

  def map[B](f: A => B): Gen[B] = sample.map(f)

  def _map[B]  = (sample.map(_: A => B)) andThen Gen.asGen
  def _2map[B]: ((A) => B) => Gen[B] = sample.map(_: A => B)

  def map2[B, C](gen2: Gen[B])(f: (A, B) => C): Gen[C] = sample.map2(gen2.sample)(f) //asGen called

  //flatMap(a => gen2.map(b => f(a,b)))

  def flatMap[B](f: A => Gen[B]): Gen[B] = sample.flatMap(f.andThen(_.sample)) //Moje lepsze
  def flatMapBook[B](f: A => Gen[B]): Gen[B] = Gen(sample.flatMap(a => f(a).sample))

}

object Gen {

  implicit def asGen[B](s: State[RNG, B]): Gen[B] = Gen(s)

  def nonNegativeInt: Gen[Int] = State(RNG.nonNegativeInt)

  def choose(start: Int, stopExclusive: Int): Gen[Int] = nonNegativeInt.map(n => start + n % (stopExclusive - start))

  //Book: Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive-start)))
  //My:   Gen(State(RNG.map(RNG.nonNegativeLessThan(stopExclusive - start))(_ + start)))

  def unit[A](a: A): Gen[A] = Gen(State.unit(a))

  def boolean: Gen[Boolean] = Gen(State(RNG.boolean))

  def listOf[A](g: Gen[A]): SGen[List[A]] = SGen(n => listOfN(n, g))
  def listOf1[A](g: Gen[A]): SGen[List[A]] = SGen(n => listOfN((n max 1), g))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen(State.sequence(List.fill(n)(g.sample)))

  def listOfNBoolean(n: Int): Gen[List[Boolean]] = Gen(State.sequence(List.fill(n)(boolean.sample)))

  def unit[A](g1: Gen[A], g2: Gen[A]): Gen[A] = boolean.flatMap(b => if(b) g1 else g2)

  def weighted[A](g1: (Gen[A],Double), g2: (Gen[A],Double)): Gen[A] = {
    /* The probability we should pull from `g1`. */
    val g1Threshold = g1._2.abs / (g1._2.abs + g2._2.abs) //!!!!! Sprytne - wartość < 1 więć można użyc normalnego generatora double

    Gen(State(RNG.double).flatMap(d =>
    if (d < g1Threshold) g1._1.sample else g2._1.sample))
  }
}


object Prop {
  import learn.fpscala.exercise.chapter5.Stream

  type SuccessCount = Int
  type TestCases = Int
  type MaxSize = Int
  type FailedCase = String


  sealed trait Result {
    def isFalsified: Boolean
  }

  case object Passed extends Result {
    override def isFalsified: Boolean = false
  }

  case object Proved extends Result {
    def isFalsified = false
  }

  case class Falsified(failure: FailedCase, successes: SuccessCount) extends Result {
    override def isFalsified = true
  }


  /* Produce an infinite random stream from a `Gen` and a starting `RNG`. */
  def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] =
    Stream.unfold(rng)(rng => Some(g.sample.run(rng)))

  def forAll[A](as: Gen[A])(f: A => Boolean): Prop = Prop {
    (n,rng) => randomStream(as)(rng).zip(Stream.from(0)).take(n).map {
      case (a, i) => try {
        if (f(a)) Passed else Falsified(a.toString, i)
      } catch { case e: Exception => Falsified(buildMsg(a, e), i) }
    }.find(_.isFalsified).getOrElse(Passed)
  }


  // String interpolation syntax. A string starting with `s"` can refer to
  // a Scala value `v` as `$v` or `${v}` in the string.
  // This will be expanded to `v.toString` by the Scala compiler.
  def buildMsg[A](s: A, e: Exception): String =
  s"test case: $s\n" +
    s"generated an exception: ${e.getMessage}\n" +
    s"stack trace:\n ${e.getStackTrace.mkString("\n")}"

  def apply(f: (TestCases,RNG) => Result): Prop =
    Prop { (_,n,rng) => f(n,rng) }

  def forAll[A](g: SGen[A])(f: A => Boolean): Prop =
    forAll(g(_))(f)

  def forAll[A](g: Int => Gen[A])(f: A => Boolean): Prop = Prop {
    (max,n,rng) => {
      val casesPerSize = (n - 1) / max + 1
      val props: Stream[Prop] =
        Stream.from(0).take((n min max) + 1).map(i => forAll(g(i))(f))
      val prop: Prop =
        props.map(p => Prop { (max, n, rng) =>
          p.run(max, casesPerSize, rng)
        }).toList.reduce(_ && _)
      prop.run(max, n, rng)
    }
  }
  def run(p: Prop,
          maxSize: Int = 100,
          testCases: Int = 100,
          rng: RNG = RNG.Simple(System.currentTimeMillis)): Unit =
    p.run(maxSize, testCases, rng) match {
      case Falsified(msg, n) =>
        println(s"! Falsified after $n passed tests:\n $msg")
      case Passed =>
        println(s"+ OK, passed $testCases tests.")
      case Proved =>
        println(s"+ OK, proved property.")
    }
}
