package learn.advanced.scala.cartesian


object Exercies {

  import scala.language.higherKinds
  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def product[M[_]: Monad, A, B](
                                  fa: M[A],
                                  fb: M[B]
                                ): M[(A, B)] = {

    fa.flatMap(a => fb.map(b => (a, b)))
  }
}
