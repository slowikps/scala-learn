package learn.advanced.scala.monad

import cats.Id


object IdOps {

  def pure[A](a: A): Id[A] = a
  def map[A, B](a: Id[A])(f: A => B): Id[B] = f(a)
  def flatMap[A, B](a: Id[A])(f: A => Id[B]): Id[B] = f(a)

}
