package learn.fpscala.exercise.chapter11

import learn.fpscala.exercise.chapter6.RNG.Simple
import learn.fpscala.exercise.chapter6.{RNG, State}


object StateMonad extends App {

  def stateMonad[S] = new Monad[({type f[x] = State[S, x]})#f] {
    def unit[A](a: => A): State[S, A] = State(s => (a, s))
    override def flatMap[A,B](st: State[S, A])(f: A => State[S, B]): State[S, B] =
      st flatMap f
  }

  println("Start")

  val intMonad = stateMonad[RNG]

  println(
    intMonad.replicateM(10, State(RNG.nonNegativeLessThan(24))).run(Simple(1))
  )
  println(
    intMonad.sequence(List.fill(10)(State(RNG.nonNegativeLessThan(24)))).run(Simple(1))
  )
  println("End")
}
