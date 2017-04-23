package learn.fpscala.exercise.chapter13.free

import learn.fpscala.exercise.chapter11.Monad
import learn.fpscala.exercise.chapter13._
import learn.fpscala.exercise.chapter7.Par.Par

import scala.annotation.tailrec


sealed trait Free[F[_], A] {
  def flatMap[B](f: A => Free[F,B]): Free[F,B] =
    FlatMap(this, f)
  def map[B](f: A => B): Free[F,B] =
    flatMap(f andThen (Return(_)))
}

//The Return and FlatMap constructors witness that this data type is a monad for any choice of F, and since they're exactly the operations required to generate a monad, we say that it's a free monad.
case class Return[F[_], A](a: A) extends Free[F, A]

case class Suspend[F[_], A](s: F[A]) extends Free[F, A]

case class FlatMap[F[_], A, B](s: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

object Free {
  type TailRec[A] = Free[Function0, A]
  type Async[A] = Free[Par, A]

  def freeMonad[F[_]]: Monad[({type f[a] = Free[F, a]})#f] = new Monad[({
    type f[a] = Free[F, a]
  })#f] {

    override def flatMap[A, B](fa: Free[F, A])(f: (A) => Free[F, B]): Free[F, B] = FlatMap(fa, f)
    override def unit[A](a: => A): Free[F, A] = Return(a)
  }

  @tailrec
  def runTrampoline[A](a: Free[Function0, A]): A = a match {
    case Return(a) => a
    case Suspend(s) => s()
    case FlatMap(s, f) => s match {
      case Return(a) => runTrampoline(f(a))
      case Suspend(s) => runTrampoline(f(s()))
      case FlatMap(a0,g) => runTrampoline { a0 flatMap { a0 => g(a0) flatMap f } }
    }
  }
}
