package learn.fpscala.exercise.chapter10


sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

trait Foldable[F[_]] { //F is a type constructor which takes one type argument - it is also called higher-order type or higher-kinded type
  import Monoid._

  def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B =
    foldMap(as)(f.curried)(endoMonoid[B])(z)

  def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B =
    foldMap(as)(a => (b: B) => f(b, a))(dual(endoMonoid[B]))(z)

  def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B = foldRight(as)(mb.zero)((a, b) => mb.op(f(a), b))

  def concatenate[A](as: F[A])(m: Monoid[A]): A = foldLeft(as)(m.zero)(m.op)


  def toList[A](fa: F[A]): List[A] = foldRight(fa)(List[A]())(_ :: _)

  def productMonoid[A, B](a: Monoid[A], b: Monoid[B]): Monoid[(A, B)] = new Monoid[(A, B)] {

    override def op(a1: (A, B), a2: (A, B)): (A, B) = (a.op(a1._1, a2._1), b.op(a1._2, a2._2))
    override def zero: (A, B) = (a.zero, b.zero)
  }
}


object FoldableOption extends Foldable[Option] {
  //Book is using patter matching here
  override def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B): B =
    as.map(a => f(a, z)).getOrElse(z)

  override def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B): B = foldRight(as)(z)((a, b) => f(b, a))

  override def foldMap[A, B](as: Option[A])(f: (A) => B)(mb: Monoid[B]): B = as.map(f).getOrElse(mb.zero)

}

object TreeFoldable extends Foldable[Tree] {
  //Not the best idea:
//  override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B): B = {
//    def toList(as: Tree[A]): List[A] = as match {
//      case Branch(l, r) => toList(l) ::: toList(r)
//      case Leaf(a) => List(a)
//    }
//    toList(as).foldRight(z)(f)
//  }

  override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B): B = as match {
    case Leaf(a) => f(a, z)
    case Branch(l, r) => foldRight(l)(foldRight(r)(z)(f))(f)
  }

  override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B) = as match {
    case Leaf(a) => f(z, a)
    case Branch(l, r) => foldLeft(r)(foldLeft(l)(z)(f))(f)
  }


  override def foldMap[A, B](as: Tree[A])(f: (A) => B)(mb: Monoid[B]): B = as match {
    case Leaf(a) => f(a)
    case Branch(l, r) => mb.op(foldMap(l)(f)(mb), foldMap(r)(f)(mb))

  }
}

object ListFoldable extends Foldable[List] {

  override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B = as match {
    case x :: xs => f(x, foldRight(xs)(z)(f))
    case _ => z
  }
  override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B = as match {
    case x :: xs => foldLeft(xs)(f(z, x))(f)
    case _ => z
  }

  override def foldMap[A, B](as: List[A])(f: (A) => B)(m: Monoid[B]): B = as match {
    case x :: xs => m.op(f(x), foldMap(xs)(f)(m))
    case _ => m.zero
  }
}

object IndexedSeqFoldable extends Foldable[IndexedSeq] {
  import Monoid._
  override def foldRight[A, B](as: IndexedSeq[A])(z: B)(f: (A, B) => B) =
    as.foldRight(z)(f)
  override def foldLeft[A, B](as: IndexedSeq[A])(z: B)(f: (B, A) => B) =
    as.foldLeft(z)(f)
  override def foldMap[A, B](as: IndexedSeq[A])(f: A => B)(mb: Monoid[B]): B =
    foldMapV(as, mb)(f)
}

object StreamFoldable extends Foldable[Stream] {
  override def foldRight[A, B](as: Stream[A])(z: B)(f: (A, B) => B) =
    as.foldRight(z)(f)
  override def foldLeft[A, B](as: Stream[A])(z: B)(f: (B, A) => B) =
    as.foldLeft(z)(f)
}
