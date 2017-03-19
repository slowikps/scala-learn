package learn.fpscala.exercise.chapter3


sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


object Tree {
  def size[A](t: Tree[A]): Int = t match {
    case Branch(l, r) => 1 + size(l) + size(r)
    case Leaf(_) => 1
  }

  def maximum(t: Tree[Int]): Int = t match {
    case Branch(l, r) => maximum(l) max maximum(r)
    case Leaf(x) => x
  }

  def depth(t: Tree[Int]): Int = t match {
    case Branch(l, r) => 1 + (depth(l) max depth(r))
    case _ => 0
  }

  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
    case Leaf(x) => Leaf(f(x))
  }

  def fold[A,B](t: Tree[A])(f: A => B)(g: (B,B) => B): B = t match {
    case Leaf(x) => f(x)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

  def map1[A, B](t: Tree[A])(f: A => B): Tree[B] = fold(t)(x => Leaf(f(x)): Tree[B])(Branch(_, _))
//fold(t)(a => Leaf(f(a)): Tree[B])(Branch(_,_))
  def size1[A](t: Tree[A]): Int = fold(t)(_ => 1)(1 + _ + _)

  def maximum1(t: Tree[Int]): Int = fold(t)(x => x)(_ max _)

  def depth1(t: Tree[Int]): Int = fold(t)(_ => 0)((a, b) => (a max b) + 1)
}