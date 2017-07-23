package learn.advanced.scala.state

import cats.data.EitherT




object PostOrderCalculator extends App {

  import cats.data.State

  type CalcState[A] = State[List[Int], A]

  import cats.syntax.either._
  def evalOneSimple(sym: String): CalcState[Int] = State{
    in => {
      Either.catchOnly[NumberFormatException](sym.toInt) match {
        case Right(asInt) => (asInt:: in, 0)
        case Left(_) => (in.tail.tail, in.head + in.tail.head)
      }
    }
  }


  println(
    evalOneSimple("+").run(List(1, 2)).value
  )

  //Book

  def evalOne(sym: String): CalcState[Int] =
    sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }

  def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
      (num :: stack, num)
    }

  def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
      case a :: b :: tail =>
        val ans = func(a, b)
        (ans :: tail, ans)

      case _ =>
        sys.error("Fail!")
    }

  def evalAll(input: List[String]): CalcState[Int] =
    input match {
      case x :: xs => {
        for {
          _ <- evalOne(x)
          r <- evalAll(xs)
        } yield r
      }
      case Nil => State.pure[List[Int], Int](0)
    }

  println(
    evalAll(List("1", "2", "3", "+", "-")).run(Nil).value
  )

  import cats.syntax.applicative._
  // import cats.syntax.applicative._

  def evalAllBook(input: List[String]): CalcState[Int] =
  input.foldLeft(0.pure[CalcState]) { (a, b) =>
    a flatMap (_ => evalOne(b))
  }

  println(
    evalAllBook(List("1", "2", "3", "+", "-")).run(Nil).value
  )


}
