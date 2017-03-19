package learn.fpscala.exercise.chapter3

import learn.fpscala.exercise.chapter3.fpinscala.datastructures._
import learn.fpscala.exercise.chapter3.fpinscala.datastructures.MyList._
import learn.fpscala.exercise.chapter3.Tree._

object Questions {

  def main(args: Array[String]): Unit = {
    println(s"x: $x")

    println(init(MyList(1,2,3)))
    println("reverse: " + revers(MyList(1,2,3)))
    println("append: " + append2(MyList(1,2,3), MyList(3,4,5)))
    println("----------------------------------------------")
    println("flatten: " + flatten(MyList(MyList(1,2), MyList(3), MyList(4,5,6), MyList(7))))
    println("plus1: " + plusOne(MyList(1,2,3,4)))
    println("filter: " + filter(MyList(1,2,3,4))(_ % 2 == 0))
    println("addPairwise: " + addPairwise(MyList(1,2,3,4), MyList(1,2,3,4)))
    println("zipWith: " + zipWith(MyList(1,2,3,4), MyList(1,2,3,4))(_ + _))
    println("hasSubsequence: " + hasSubsequence(MyList(1,2,2,3,2,3,4,5,6), MyList(2,3,4)))

    println("----------------------------------------------")
    println("depth1: " + depth1(Branch(Branch(Leaf(1), Branch(Leaf(2), Leaf(5))), Leaf(3))))
  }

  val x = MyList(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }
}
