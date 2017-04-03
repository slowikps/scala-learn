package learn.fpscala.exercise.chapter10

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pslowinski on 27/03/2017.
  */
object Main {

  def monadMain() = {
    println("Enter monad part")
    println(
      Monad.optionMonad.filterMBook(List(1,2,3,4))(_ => Some(true)) == Monad.optionMonad.filterM(List(1,2,3,4))(_ => Some(true))
    )
    println(
      Monad.optionMonad.filterMBook(List(1,2,3,4))(_ => Some(true))
    )
    println(
        Monad.listMonda.filterMBook(List(1,2,3,4))(_ => List(true, false)) == Monad.listMonda.filterM(List(1,2,3,4))(_ => List(true, false))
    )
    println(
        Monad.listMonda.filterMBook(List(1,2,3,4))(_ => List(false, true))
    )
    println("Exit monad part")
  }

  def main(args: Array[String]): Unit = {
    monadMain()

    val in = List(1,2,3)

    println(
      Monoid.foldRightViaFoldMap(0)(in.toList)(_ + _)
    )

    println(
      "is List(1,2,3) ordered: " + Monoid.isOrdered(in)
    )

    println(
      "is List(0, 4, 8, 8, 11, 15) ordered: " + Monoid.isOrdered(List(0, 4, 8, 8, 11, 15))
    )
    println(
      "is List(0, 4, 8, 11, 15, 3 ordered: " + Monoid.isOrdered(List(0, 4, 8, 11, 15, 3))
    )

    println(
      "Count words: (abc def k ghi j): " + ParallelParsin.countWords("abc def k ghi j")
    )

    println(
      "Count words book: (abc def k ghi j): " + ParallelParsin.countBook("abc def k ghi j")
    )


  }
}
