package learn.advanced.scala

import cats.{Functor, Monad}

import scala.util.Success

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object FunctorsExericse extends App {

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  def treeMonad = new Monad[Tree] {

    override def pure[A](x: A): Tree[A] = leaf(x)

    override def flatMap[A, B](fa: Tree[A])(f: (A) => Tree[B]): Tree[B] =
      fa match {
        case Leaf(a) => f(a)
        case Branch(a, b) => branch(flatMap(a)(f), flatMap(b)(f))
      }

    override def tailRecM[A, B](a: A)(f: (A) => Tree[Either[A, B]]): Tree[B] =
      f(a) match {
        case Leaf(Right(b)) => leaf(b)
        case Leaf(Left(a)) => tailRecM(a)(f)
        case Branch(l, r) =>
          Branch(
            flatMap(l) {
              case Left(l)  => tailRecM(l)(f)
              case Right(l) => pure(l)
            },
            flatMap(r) {
              case Left(r)  => tailRecM(r)(f)
              case Right(r) => pure(r)
            }
          )
      }
  }

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



  import cats.syntax.functor._

  println(
    leaf(11).map(_ * 2)
  )
}
