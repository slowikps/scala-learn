package learn.fpscala.exercise.chapter13

import learn.fpscala.exercise.chapter11.Monad



sealed trait IO[A] {

  def map[B](f: A => B): IO[B] =
    flatMap(f andThen (Return(_)))

  def flatMap[B](f: A => IO[B]): IO[B] =
    FlatMap(this, f)

}

case class Return[A](a: A) extends IO[A]

case class Suspend[A](resume: () => A) extends IO[A]

case class FlatMap[A, B](sub: IO[A], k: A => IO[B]) extends IO[B]

object IO extends Monad[IO] {

  def printLine(msg: String): IO[Unit] =
    Suspend(() => {
      Return(println(msg))
      ()
    })

  override def flatMap[A, B](ma: IO[A])(f: (A) => IO[B]): IO[B] =
    ma flatMap f

  def apply[A](a: => A): IO[A] = unit(a)

  override def unit[A](a: => A): IO[A] =
    Return(a)


  @scala.annotation.tailrec
  def run[A](io: IO[A]): A = io match {
    case Return(a) => a
    case Suspend(f) => f()
    case FlatMap(sub, f) => sub match { //Stupid intellij
      case Return(a) => run(f(a))
      case Suspend(f2) => run(f(f2()))
      case FlatMap(sub2, f2) => run(sub2 flatMap (a => f2(a) flatMap f))
    }
  }
}

//object IO extends Monad[IO] {
//  override def flatMap[A, B](ma: IO[A])(f: (A) => IO[B]): IO[B] = {
//    ma flatMap f
//  }
//
//  def PrintLine(msg: String): IO[Unit] = IO {
//    println(msg)
//  }
//
//  def apply[A](a: => A): IO[A] = unit(a) //This makes MyIO a worse version of above
//
//  override def unit[A](a: => A): IO[A] = new IO[A] {
//    override def run: A = a
//  }
//}