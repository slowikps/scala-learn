package learn.advanced.scala.monoids.and.semigroups

//object MonoidAndSemiGroup {
//  trait Semigroup[A] {
//    def combine(x: A, y: A): A
//  }
//  trait Monoid[A] extends Semigroup[A] {
//    def empty: A
//  }
//  object Monoid {
//    def apply[A](implicit monoid: Monoid[A]) =
//      monoid
//  }
//}


object Definitions extends App {

  import cats.kernel.Monoid
  import cats.instances.int._

  val myList = List(1, 2, 3, 4)

  println(
    myList.reduceLeft(Monoid[Int].combine)
  )

}
