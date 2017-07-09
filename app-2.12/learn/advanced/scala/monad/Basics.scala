package learn.advanced.scala.monad
import scala.language.higherKinds

trait Monad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(f: A => B): F[B] = flatMap(value)(x => pure(f(x)))
}
object Basics extends App {

  import cats.Monad
  import cats.instances.list._

  val in: Seq[Int] = Monad[List].pure(1)

  import cats.syntax.applicative._

  val coTo = 11.pure

  println(coTo)

}
