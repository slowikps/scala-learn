package learn.advanced.scala

import cats.Functor

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object FunctorsExericse extends App {
  implicit val treeFunctor = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: (A) => B): Tree[B] =
      fa match {
        case Leaf(a) => Leaf(f(a))
        case Branch(l, r) => Branch(map(l)(f), map(r)(f))
      }
  }

  val myTree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))

  println(myTree)
  println(treeFunctor.map(myTree)(_ + 10))

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  import cats.syntax.functor._

  println(
    leaf(11).map(_ * 2)
  )
}
