package learn.exercise

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ListTest {


  def main(args: Array[String]): Unit = {
    val input = List(1,2,3,4,5,6)

    println(
      input.sum
    )

    println(
      sum(input)
    )

    println(
      "Sum via fold: " + sumViaFold(input)
    )

    println(
      "greaterThan sum2: " + sum2(input, 4)
    )

    println(
      "greaterThan filter: " + sum(filter(input)(_ > 4))
    )

    println(
      "greaterThan filterViaFold: " + sum(filterViaFold(input)(_ > 4))
    )

    val input2 = List("AB", "cd", "e", "gh")

    println(
      "input2: " + sum(map(input2)(_.size))
    )


    println("encodeDirect1:            " + encodeDirect1(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e, 'f)))
    println("encodeDirect2:            " + encodeDirect2(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e, 'f)))
    println("encodeDirectViaFoldRight: " + encodeDirectViaFoldRight(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e, 'f)))
    println("encodeDirectViaFoldLeft:  " + encodeDirectViaFoldLeft(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e, 'f)))

    val futureInput: List[Future[Int]] = input.map(Future.successful(_))

    println {
      futureInput.foldLeft(Future.successful(0))((acc, x) => acc.flatMap(tmpSum => x.map(_ + tmpSum)))
    }
  }

  //Sum all values in the list (recursive):
  //def sum(in: List[Int]): Int = ???



  def sum(in: List[Int]): Int = in match {
    case x :: xs => x + sum(xs)
    case _ => 0
  }

  //Sum via foldLeft or foldRight
  def sumViaFold(in: List[Int]): Int = in.foldLeft(0)(_ + _)

  //Sum all values greater than in the list:
  //def sum2(in: List[Int], greaterThan: Int): Int = ???

  def sum2(in: List[Int], greaterThan: Int): Int = in match {
    case x :: xs => if(x > greaterThan) x + sum2(xs, greaterThan) else sum2(xs, greaterThan)
    case _ => 0
  }

//  def filter[A](in: List[A])(f: A => Boolean): List[A] = ???

  def filter[A](in: List[A])(f: A => Boolean): List[A] = in match {
    case x :: xs => if(f(x)) x :: filter(xs)(f) else filter(xs)(f)
    case _ => Nil
  }

  def filterViaFold[A](in: List[A])(f: A => Boolean): List[A] = in.foldLeft(Nil: List[A])((acc, x) => {
    if(f(x)) x :: acc
    else acc
  })

  //def map[A, B](in: List[A])(f: A => B): List[B] = ???

  def map[A, B](in: List[A])(f: A => B): List[B] = in match {
    case x :: xs => f(x) :: map(xs)(f)
    case _ => Nil
  }


  //Run-length encoding of a list.
  //Implement the so-called run-length encoding data compression method.
  //Example:
  //  scala> encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
  //  res0: List[(Int, Symbol)] = List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e))

//  def encodeDirect[A](ls: List[A]): List[(Int, A)] = ???

  def encodeDirect1[A](ls: List[A]): List[(Int, A)] = {
    def inner(elem: A, occ: Int, list: List[A]): List[(Int, A)] = list match {
      case x :: xs =>
        if(x == elem) inner(elem, occ + 1, xs)
        else (occ, elem) :: inner(x, 1, xs)
      case _ => List((occ, elem))
    }

    if (ls.isEmpty) Nil
    else inner(ls.head, 1, ls.tail)
  }


  def encodeDirectViaFoldRight[A](ls: List[A]): List[(Int, A)] = {
    if (ls.isEmpty) Nil
    else {
      val res = ls.dropRight(1).foldRight((Nil:  List[(Int, A)], (1, ls.last))) { (x, acc) =>
        if(x == acc._2._2) (acc._1, (acc._2._1 + 1, x))
        else (acc._2 :: acc._1, (1, x))
      }
      res._2 :: res._1
    }
  }

  def encodeDirectViaFoldLeft[A](ls: List[A]): List[(Int, A)] = {
    if (ls.isEmpty) Nil
    else {
      val res = ls.tail.foldLeft((Nil:  List[(Int, A)], (1, ls.head))) { (acc, x) =>
        if(x == acc._2._2) (acc._1, (acc._2._1 + 1, x))
        else (acc._2 :: acc._1, (1, x))
      }
      (res._2 :: res._1).reverse
    }
  }

  def encodeDirect2[A](ls: List[A]): List[(Int, A)] = {
    if (ls.isEmpty) Nil
    else {
      val (packed, next) = ls span {_ == ls.head}
      (packed.length, packed.head) :: encodeDirect2(next)
    }
  }
}