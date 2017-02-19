package learn.fpscala.exercise.chapter2

import scala.annotation.tailrec

/**
  * Exercise 2.1
  */
object Fibonacci {

  def main(args: Array[String]): Unit = {
    def validateFibonacciSolutions(element: Int, solutions: Set[Int => Int]) = solutions.map(_(element)).size == 1

    val fibFunctions = List(fibTailRec _, fibNotTailRec _, fibBook _)

    (1 to 10).map(n => (n, fibFunctions.map(_(n)))).foreach {
      case (n, fibbElem) => println(s"${n}: ${fibbElem}")
      //      res => println(s"${res._1}: ${res._2}")
    }

    println {
      "All methods returned same value:" +
      (1 to 20).map(n => {
          (n, validateFibonacciSolutions(n, fibFunctions.toSet))
        }).filter(!_._2)
    }
  }

  def fibBook(n: Int): Int = {
    @annotation.tailrec
    def loop(n: Int, prev: Int, cur: Int): Int =
      if (n == 1) prev
      else loop(n - 1, cur, prev + cur)
    loop(n, 0, 1)
  }

  def fibTailRec(n: Int): Int = {
    @tailrec
    def loop(n: Int, secondLast: Int, last: Int): Int = {
      if(n == 1) secondLast + last
      else loop(n - 1, last, secondLast + last)
    }
    n match {
      case 1 => 0
      case 2 => 1
      case _ => loop(n - 2, 0, 1)
    }
  }

  def fibNotTailRec(n: Int): Int = n match {
    case 1 => 0
    case 2 => 1
    case _ => fibNotTailRec(n - 1) + fibNotTailRec(n - 2)
  }
}
