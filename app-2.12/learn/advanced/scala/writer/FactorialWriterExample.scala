package learn.advanced.scala.writer


object FactorialWriterExample extends App {

  import cats.syntax.writer._
  import cats.syntax.applicative._
  import cats.data.Writer
  import cats.instances.vector._

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Logged[Int] =
    for {
      ans <- if (n == 0) {
        1.pure[Logged]
      } else {
        slowly(factorial(n - 1).map(_ * n))
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans

  println(
    factorial(10)
  )
}
