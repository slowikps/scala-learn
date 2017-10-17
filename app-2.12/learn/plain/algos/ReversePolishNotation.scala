package learn.plain.algos

import scala.collection.mutable


object ReversePolishNotation {

  def main(args: Array[String]): Unit = {
    println(naiveRecursive("512+4*+3-".toList))
    println(naiveStack("512+4*+3-".toList))
    //      println(recursive("10 20 + 3 * 80 -".toList)) //(10 + 20) * 3 - 80 = 10

    println(naiveStack("321+-".toList))
  }


  def recursive(input: List[Char]): Int = {
    def in(input: List[Char], stack: List[Int]): Int = input match {
      case x :: xs if x.isDigit => in(xs, x.asDigit :: stack)
      case x :: xs => in(xs, operation(x, stack.tail.head, stack.head) :: stack.drop(2))
      case _ if (stack.size == 1) => stack.head
      case er => sys.error(s"Wrong input: $er [input: $input, stack: $stack]")
    }

    in(input, Nil)
  }

  def operation(x: Char, first: Int, second: Int): Int = {
    println(s"$first $x $second");
    x match {
      case '+' => first + second
      case '-' => first - second
      case '*' => first * second
      case '/' => first / second
    }
  }


  def naiveRecursive(input: List[Char]): Int = {
    def in(input: List[Char], stack: List[Int]): Int = input match {
      case x :: xs if x.isDigit => in(xs, x.asDigit :: stack)
      case x :: xs => in(xs, operation(x, stack.tail.head, stack.head) :: stack.drop(2))
      case _ if (stack.size == 1) => stack.head
      case er => sys.error(s"Wrong input: $er [input: $input, stack: $stack]")
    }

    in(input, Nil)
  }

  def naiveStack(input: List[Char]): Int = {
    val st = new mutable.ArrayBuffer[Int]()
    for (x <- input) {
      if (x.isDigit) st.prepend(x.asDigit)
      else {
        st.prepend(operation(x, st.remove(1), st.remove(0)))
      }
    }
    if (st.size == 1) st.head
    else sys.error(s"Wrong input: $input [stack: $st]")
  }
}