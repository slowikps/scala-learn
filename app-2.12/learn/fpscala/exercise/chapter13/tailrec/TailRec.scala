package learn.fpscala.exercise.chapter13.tailrec

import learn.fpscala.exercise.chapter13.tailrec

trait TailRec[A] {

  def flatMap[B](f: A => TailRec[B]): TailRec[B] = tailrec.FlatMap(this, f)

  def map[B](f: A => B): TailRec[B] =
    flatMap(f andThen (tailrec.Return(_)))
//    flatMap(a => Return(f(a)))

}
case class Return[A](a: A) extends TailRec[A]
case class Suspend[A](resume: () => A) extends TailRec[A]
case class FlatMap[A, B](sub: TailRec[A], k: A => TailRec[B]) extends TailRec[B]

