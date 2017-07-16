package learn.advanced.scala.eval

import cats.{Eval, Now}


object StackSafeFactorial extends App {

  def factorial(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(n)
    } else {
      Eval.defer(factorial(n - 1).map(_ * n))
    }

  println(
//    factorial(50000).value
    factorial(5).value
  )


  def foldRightNotSafe[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    as match {
      case head :: tail =>
        fn(head, foldRightNotSafe(tail, acc)(fn))
      case Nil =>
        acc
    }

  def foldRightSafe[A, B](as: List[A], acc: B)(fn: (A, B) => B): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(foldRightSafe(tail, acc)(fn)).map(fn(head, _))
      case Nil =>
        Eval.now(acc)
    }

  def foldRightEval[A, B](as: List[A], acc: Eval[B])
                         (fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
      case Nil =>
        acc
    }

  println(
    foldRightNotSafe((1 to 5000).toList, 0)(_ + _)
  )
  println(
    foldRightSafe((1 to 5000).toList, 0)(_ + _).value
  )
}
