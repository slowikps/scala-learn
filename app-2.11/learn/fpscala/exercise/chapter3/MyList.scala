package learn.fpscala.exercise.chapter3

package fpinscala.datastructures

import sun.font.TrueTypeFont

import scala.annotation.tailrec

sealed trait MyList[+A]

// `MyList` data type, parameterized on a type, `A`
case object Nil extends MyList[Nothing]

// A `MyList` data constructor representing the empty MyList
/* Another data constructor, representing nonempty MyLists. Note that `tail` is another `MyList[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {
  // `MyList` companion object. Contains functions for creating and working with MyLists.
  def sum(ints: MyList[Int]): Int = ints match {
    // A function that uses pattern matching to add up a MyList of integers
    case Nil => 0 // The sum of the empty MyList is 0.
    case Cons(x, xs) => x + sum(xs) // The sum of a MyList starting with `x` is `x` plus the sum of the rest of the MyList.
  }

  def product(ds: MyList[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): MyList[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def append[A](a1: MyList[A], a2: MyList[A]): MyList[A] =
    a1 match {
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))
    }

  def foldRight[A, B](as: MyList[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: MyList[Int]) =
    foldRight(ns, 0)((x, y) => x + y)

  def sum3(ints: MyList[Int]): Int = foldLeft(ints, 0)(_ + _)

  def product2(ns: MyList[Double]) =
    foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar

  def product3(ns: MyList[Double]) = foldLeft(ns, 1.0)(_ * _)

  def tail[A](l: MyList[A]): MyList[A] = l match {
    case Cons(_, xs) => xs
    case Nil => sys.error("tail of empty MyList")
  }

  def setHead[A](l: MyList[A], h: A): MyList[A] = //Cons(h, l) - This was my answer, but appartntly it is wrong
    l match {
      case Nil => sys.error("setHead on empty MyList")
      case Cons(_,t) => Cons(h,t)
    }

  def drop[A](l: MyList[A], n: Int): MyList[A] = {
    // if(n == 0) l else drop(tail(l), n - 1) apparently not desire solution as we don't want to throw exception when n > l.length
    if (n <= 0) l
    else l match {
      case Nil => Nil
      case Cons(_, t) => drop(t, n - 1)
    }
  }

  def dropWhile[A](l: MyList[A], f: A => Boolean): MyList[A] = l match {
    case Cons(x, xs) if(f(x)) => dropWhile(xs, f)
    case _ => l
  }

  def init[A](l: MyList[A]): MyList[A] = l match {
//    case Nil | Cons(_ , Nil) => Nil now we want to throw an error :-)
    case Nil => sys.error("init of empty MyList")
    case Cons(_,Nil) => Nil
    case Cons(x, xs) => Cons(x, init(xs))
  }

  def length[A](l: MyList[A]): Int = foldRight(l, 0)((_, acc) => acc + 1)

  def length2[A](l: MyList[A]): Int = foldLeft(l, 0)((acc, _) => acc + 1)

  @tailrec
  def foldLeft[A, B](l: MyList[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
  }

  def revers[A](l: MyList[A]): MyList[A] = foldLeft(l, Nil: MyList[A]) {
    (acc, x) => Cons(x, acc)
  }

  def reversUgly[A](l: MyList[A]): MyList[A] = foldRight(l, Nil: MyList[A]) {
    (x, xs) => append(xs, Cons(x, Nil))
  }

  def append2[A](a1: MyList[A], a2: MyList[A]): MyList[A] = foldRight(a1, a2)(Cons(_, _))
  def appendVerbose[A](a1: MyList[A], a2: MyList[A]): MyList[A] = foldRight(a1, a2){
    (x, acc) =>
      println(s"x: $x, acc: $acc")
      Cons(x, acc)
  }

  def concat[A](l: MyList[MyList[A]]): MyList[A] = foldRight(l, Nil:MyList[A])(append)

  def flatten[A](a1: MyList[MyList[A]]): MyList[A] = {
    foldRight(a1, Nil: MyList[A]){
      (x, acc) => {
//        println(s"about to append: x: $x, acc: $acc")
        appendVerbose(x, acc)
      }
    }
  }

  def flatten2[A](a1: MyList[MyList[A]]): MyList[A] = foldRight(a1, Nil: MyList[A])(append)

  def plusOne(l: MyList[Int]) = mapFold(l)(_ + 1)
  def doubleToString(l: MyList[Double]) = map(l)(_.toString)

  def map[A, B](l: MyList[A])(f: A => B): MyList[B] = l match {
    case Cons(x, xs) => Cons(f(x), map(xs)(f))
    case _ => Nil
  }

  def mapFold[A, B](l: MyList[A])(f: A => B): MyList[B] = {
    foldRight(l, Nil: MyList[B]) (//(Cons(f(_),_))??
      (x, acc) => Cons(f(x), acc))
  }

  def map_2[A,B](l: MyList[A])(f: A => B): MyList[B] = {
    val buf = new collection.mutable.ListBuffer[B]
    def go(l: MyList[A]): Unit = l match {
      case Nil => ()
      case Cons(h,t) => buf += f(h); go(t)
    }
    go(l)
    MyList(buf.toList: _*) // converting from the standard Scala MyList to the MyList we've defined here
  }

  def filter[A](as: MyList[A])(f: A => Boolean): MyList[A] = foldRight(as, Nil: MyList[A]) {
    (x, acc) => if(f(x)) Cons(x, acc) else acc
  }

  def flatMap[A,B](as: MyList[A])(f: A => MyList[B]): MyList[B] =
    concat(map(as)(f))
    //flatten(map(as)(f))

  def filter2[A](as: MyList[A])(f: A => Boolean): MyList[A] = //Bez sensu:
    flatMap(as)(a => if (f(a)) MyList(a) else Nil)

  def addPairwise(a: MyList[Int], b: MyList[Int]): MyList[Int] = (a, b) match {
    case (Cons(x1, xs1), Cons(x2, xs2)) => Cons(x1 + x2, addPairwise(xs1, xs2))
    case _ => Nil
  }

  def zipWith[A, B, C](a: MyList[A], b: MyList[B])(f: (A,B) => C): MyList[C] = (a, b) match {
    case (Cons(x1, xs1), Cons(x2, xs2)) => Cons(f(x1, x2), zipWith(xs1, xs2)(f))
    case _ => Nil
  }

  def myHasSubsequence[A](sup: MyList[A], sub: MyList[A]): Boolean = (sup, sub) match {
    case (Cons(_, _), Nil) => true
    case (Cons(x1, xs1), Cons(x2, xs2)) => {
      if(x1 == x2) hasSubsequence(xs1, xs2) || hasSubsequence(xs1, sub)
      else hasSubsequence(xs1, sub)
    }
    case _ => false
  }
  //Book answer:
  @annotation.tailrec
  def startsWith[A](l: MyList[A], prefix: MyList[A]): Boolean = (l,prefix) match {
    case (_,Nil) => true
    case (Cons(h,t),Cons(h2,t2)) if h == h2 => startsWith(t, t2)
    case _ => false
  }
  @annotation.tailrec
  def hasSubsequence[A](sup: MyList[A], sub: MyList[A]): Boolean = sup match {
    case Nil => sub == Nil
    case _ if startsWith(sup, sub) => true
    case Cons(h,t) => hasSubsequence(t, sub)
  }
}
