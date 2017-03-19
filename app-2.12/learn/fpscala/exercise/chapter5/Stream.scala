package learn.fpscala.exercise.chapter5

import Stream._
trait Stream[+A] {

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  def toList: List[A] = foldRight(List[A]())(_ :: _)

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }
  def take(n: Int): Stream[A] = {
    def go(s: Stream[A], n: Int): Stream[A] = s match {
      case Cons(h, t) if(n > 0) => cons(h(), go(t(), n -1))
      case _ => Empty
    }
    go(this, n)
  }

  def takeUnfold(n: Int): Stream[A] = unfold((n, this)){
    case (n, Cons(x, xs)) if(n > 0) => Some((x(), (n - 1, xs())))
    case _ => None
  }

  def drop(n: Int): Stream[A] = this match {
    case Cons(_, xs) if n > 0 => xs().drop(n-1)
//    case Cons(_, xs) => xs()
//    case _ => Empty
    case _ => this
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(x, xs) if p(x()) => cons(x(), xs() takeWhile p)
    case _ => empty
  }

  def takeWhileViFold(p: A=> Boolean): Stream[A] =
    foldRight(empty[A])((x, acc) => {
      if(p(x)) cons(x, acc)
      else empty
    })

  def takeWhileUnfold(f: A => Boolean): Stream[A] = unfold(this) {
    case Cons(x ,xs) if(f(x())) => Some((x(), xs()))
    case _ => None
  }

  def forAll(p: A => Boolean): Boolean = foldRight(true)(p(_) && _)

  def headOption: Option[A] = foldRight(None: Option[A])((x, _) => Some(x))

  // 5.7 map, filter, append, flatmap using foldRight. Part of the exercise is
  // writing your own function signatures.

  def map[B](f: A => B): Stream[B] =
    foldRight(empty[B])((x, acc) => cons(f(x), acc))

  def mapUnfold[B](f: A => B): Stream[B] = unfold(this){
    case Cons(x, xs) => Some((f(x()), xs()))
    case _ => None
  }

  def filter(f: A => Boolean): Stream[A] =
    foldRight(empty[A])((x, acc) => if(f(x)) cons(x , acc) else acc)

  def append[B>:A](s: => Stream[B]): Stream[B] =
    foldRight(s)((x, acc) => cons(x, acc))

  def flatMap[B](f: A => Stream[B]): Stream[B] =
    foldRight(empty[B])((x, acc) => f(x) append acc)


  def zipWith[B,C](s2: Stream[B])(f: (A,B) => C): Stream[C] =
    unfold((this, s2)) {
      case (Cons(x1, xs1), Cons(x2, xs2)) => Some((f(x1(), x2()), (xs1(), xs2())))
      case _ => None
    }

  def zipAll[B](s2: Stream[B]): Stream[(Option[A],Option[B])] = unfold((this, s2)) {
    case (Cons(x1, xs1), Cons(x2, xs2)) => Some(((Option(x1()), Option(x2())), (xs1(), xs2())))
    case (Cons(x1, xs1), _) => Some(((Some(x1()), None), (xs1(), empty)))
    case (_, Cons(x2, xs2)) => Some(((None, Some(x2())), (empty, xs2())))
    case _ => None
  }

  // special case of `zipWith`
  def zip[B](s2: Stream[B]): Stream[(A,B)] = zipWith(s2)((_,_))

  def startsWith[B](s: Stream[B]): Boolean = zipAll(s).takeWhile(_._2.isEmpty).forAll{
    case(a, b) => a == b
  }

  def tailsMy: Stream[Stream[A]] = unfold(this){
    case s@ Cons(_, xs) => Some((s, xs()))
    case _ => None
  } append(Stream(empty))

  def tails: Stream[Stream[A]] = unfold(this){
    case Empty => None
    case s => Some((s, s drop 1))
  } append(Stream(empty))

  def scanRightRec[B](z: => B)(f: (A, => B) => B): Stream[B] = this match {
    case Cons(x ,xs) => {
      val sub: Stream[B] = xs().scanRight(z)(f)
      cons(f(x(), sub.headOption.get), sub)
    }
    case _ => Stream(z)
  }

  def scanRight2[B](z: => B)(f: (A, => B) => B): Stream[B] = {
    foldRight(Stream(z)){
      (x, acc) => cons(f(x, acc.headOption.get), acc)
    }
  }
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    foldRight((z, Stream(z)))((a, lazyAcc) => {
      // p0 is passed by-name and used in by-name args in f and cons. So use lazy val to ensure only one evaluation...
      val acc = lazyAcc
      val b2 = f(a, acc._1)
      (b2, cons(b2, acc._2))
    })._2
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)

  def constant[A](a: A): Stream[A] = cons(a, constant(a))

  // This is more efficient than `cons(a, constant(a))` since it's just
  // one object referencing itself.
  def constant2[A](a: A): Stream[A] = {
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
  }

  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  def fibs(): Stream[Int] = {
    def go(f0: Int, f1: Int): Stream[Int] = cons(f0, go(f1, f0 + f1))
    go(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z).map{
    case (a, s) => cons(a, unfold(s)(f))
  } getOrElse empty

  def fibsUnfold(): Stream[Int] = unfold((0, 1)){case (f0, f1) => Option((f0, (f1, f0 + f1)))}

  def fromUnfold(n: Int): Stream[Int] = unfold(n)(x => Some((x, x + 1)))

  def constantUnfold[A](a: A): Stream[A] = unfold(a)(_ => Some((a, a)))

  def onceUnfold: Stream[Int] = constantUnfold(1)

}