package learn.fpscala.exercise.chapter10

trait Monoid[A] {
  def op(a1: A, a2: A): A // op(op(a,b), c) == op(a, op(b,c))
  def zero: A //Satisfies op(x, zero) == x and op(zero, x) == x

}

object Monoid {

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
