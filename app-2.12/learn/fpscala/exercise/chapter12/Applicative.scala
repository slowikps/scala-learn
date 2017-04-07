package learn.fpscala.exercise.chapter12


trait Applicative[F[_]] {

  def map2[A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C]

  def unit[A](a: => A): F[A] //Remember: call by name

  def map[A, B](ma: F[A])(f: A => B): F[B] =
    map2(ma, unit(()))((a, _) => f(a))

  def traverse[A, B](ll: List[A])(f: A => F[B]): F[List[B]] =
    ll.foldRight(unit(List[B]()))((x, acc) => map2(f(x), acc)(_ :: _))

  //12.01
  def sequence[A](fas: List[F[A]]): F[List[A]] = traverse(fas)(a => a)

  //    fas.foldRight(unit(List[A]()))((x, acc) => map2(x, acc)(_ :: _))

  def replicateM[A](n: Int, fa: F[A]): F[List[A]] = sequence(List.fill(n)(fa))

  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = map2(fa, fb)((_, _))
}

trait Applicative2[F[_]] {

  //12.02
  //map2 and unit
  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B] =
    map2(fab, fa)((ab, a) => ab(a))

  //Book map2(fab, fa)(_(_))
  def unit[A](a: => A): F[A]


  //apply and unit
  //  def map2[A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] = {
  //    val curried: F[((A, B)) => C] = unit(f.tupled)
  //    val prd: F[(A, B)] = product(ma, mb)
  //    apply(curried)(prd)
  //  }
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
  apply(map(fa)(f.curried))(fb) //It is fine - Intellij is crap ; /

  def map[A, B](ma: F[A])(f: A => B): F[B] = {
    val tmp: F[A => B] = unit(f)
    apply(tmp)(ma)
  }

  //12.3 unit apply and curried only
  def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = {
    val c: F[(A) => (B) => (C) => D] = unit(f.curried)

    apply(apply(apply(c)(fa))(fb))(fc)
  }

  def map4[A, B, C, D, E](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => E): F[E] =
    apply(apply(apply(apply(unit(f.curried))(fa))(fb))(fc))(fd) //Stupid intellij



}

object Main extends App {
  println("Start")
  println("End")
}
