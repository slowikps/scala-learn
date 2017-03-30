package learn.fpscala.exercise.chapter10

trait Monoid[A] {
  def op(a1: A, a2: A): A // op(op(a,b), c) == op(a, op(b,c))
  def zero: A //Satisfies op(x, zero) == x and op(zero, x) == x

}

object Monoid {

  //TODO: Interesting
  def mapMergeMonoid[K,V](V: Monoid[V]): Monoid[Map[K,V]] =
    new Monoid[Map[K, V]] {
      override def op(a: Map[K, V], b: Map[K, V]): Map[K, V] =
        (a.keySet ++ b.keySet).foldLeft(zero){ (acc, k) =>
          acc.updated(k, V.op(a.getOrElse(k ,V.zero),
            b.getOrElse(k, V.zero)))

        }

      override def zero: Map[K, V] = Map()
    }

  def functionMonoid[A, B](B: Monoid[B]): Monoid[A => B] = new Monoid[(A) => B] {
    override def op(f1: (A) => B, f2: (A) => B): (A) => B = a => B.op(f1(a), f2(a))
    override def zero: (A) => B = _ => B.zero
  }

  def bag[A](as: IndexedSeq[A]): Map[A, Int] =
    foldMapV(as, mapMergeMonoid[A, Int](intAddition))(key => Map(key -> 1))





  //End Interesting


  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.map(f).foldLeft(m.zero)((acc, x) => m.op(acc, x))

  def foldRightViaFoldMap[A, B](zero: B)(as: List[A])(f: (A, B) => B): B = {
    //(mindblown)
    type C = B => B
    val monoid: Monoid[C] = endoMonoid[B]
    val fun: (A) => C = f.curried
    val tmp: C = foldMap(as, monoid)(fun)
    tmp(zero) //<- use of zero element!
  }

  def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B = foldMap(as, dual(endoMonoid[B]))(a => b => f(b, a))(z)


  //10.7
  def foldMapV[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): B =
    if(v.size > 10) {
      val (left, right) = v.splitAt(v.size / 2)
      m.op(foldMapV(left, m)(f), foldMapV(right, m)(f))
    } else v.foldLeft(m.zero)((acc, x) => m.op(acc, f(x)))

  //10.9
  def isOrderedMonoid = new Monoid[(Boolean, Int)] { //But this is not monoid according to monoid law!!!
    override def op(a1: (Boolean, Int), a2: (Boolean, Int)): (Boolean, Int) = (a1._1 && a1._2 <= a2._2, a2._2)
    override def zero: (Boolean, Int) = (true, Int.MinValue)
  }

  val isOrderedMonoidBook = new Monoid[Option[(Int, Int, Boolean)]] {
    def op(o1: Option[(Int, Int, Boolean)], o2: Option[(Int, Int, Boolean)]) = //This is not monoid either!!
      (o1, o2) match {
        // The ranges should not overlap if the sequence is ordered.
        case (Some((x1, y1, p)), Some((x2, y2, q))) =>
          Some((x1 min x2, y1 max y2, p && q && y1 <= x2))
        case (x, None) => x
        case (None, x) => x
      }

    val zero = None
  }

  def isOrdered(in: List[Int]): Boolean = foldMap(in, isOrderedMonoid)(x => (true, x))._1

  def intAddition: Monoid[Int] = new Monoid[Int] {
    override def op(a1: Int, a2: Int): Int = a1 + a2

    override def zero: Int = 0
  }

  def intMultiplication: Monoid[Int] = new Monoid[Int] {
    override def op(x: Int, y: Int): Int = x * y

    override def zero = 1
  }

  def booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(x: Boolean, y: Boolean): Boolean = x || y

    override def zero = false
  }

  def booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(x: Boolean, y: Boolean): Boolean = x && y

    override def zero = true
  }

  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    override def op(x: Option[A], y: Option[A]): Option[A] = x orElse y

    override def zero: Option[A] = None
  }

  // We can get the dual of any monoid just by flipping the `op`.
  def dual[A](m: Monoid[A]): Monoid[A] = new Monoid[A] {
    def op(x: A, y: A): A = m.op(y, x)

    val zero = m.zero
  }

  // Now we can have both monoids on hand:
  def firstOptionMonoid[A]: Monoid[Option[A]] = optionMonoid[A]

  def lastOptionMonoid[A]: Monoid[Option[A]] = dual(firstOptionMonoid)


  def endoMonoid[A]: Monoid[A => A] = new Monoid[(A) => A] {

    override def op(f: (A) => A, g: (A) => A) = f andThen g

    override def zero: (A) => A = a => a
  }

}
