package learn.fpscala.exercise.chapter2

import scala.annotation.tailrec

/**
  * Created by slowi on 18/02/2017.
  */
object IsSorted {
  def main(args: Array[String]): Unit = {
    println{
      isSorted(Array(1), (a: Int, b: Int) => a < b )
    }
    println{
      isSorted(Array(1,2,3), (a: Int, b: Int) => a < b )
    }
    println{
      isSorted(Array(1,2,3,4,1), (a: Int, b: Int) => a < b )
    }
  }

  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    @tailrec
    def loop(idx: Int): Boolean = {
      if (as.length == idx) true
      else if(ordered(as(idx -1), as(idx))) loop(idx + 1)
      else false
    }
    loop(1)
  }
}
