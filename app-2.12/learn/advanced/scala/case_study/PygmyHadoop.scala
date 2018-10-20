package learn.advanced.scala.case_study

import cats.{Functor, Monoid}

import scala.concurrent.Future

object PygmyHadoop extends App {

  import cats.syntax.semigroup._
  import cats.instances.int._
  import cats.instances.string._
  import cats.instances.future._

  def foldMapBook[A, B: Monoid](values: Vector[A])(func: A => B): B =
    values.map(func).foldLeft(Monoid[B].empty)(_ |+| _)

  def foldMapBook2[A, B: Monoid](values: Vector[A])(func: A => B): B =
    values.foldLeft(Monoid[B].empty)(_ |+| func(_))

  def foldMap[A, B](in: Vector[A])(f: A => B)(implicit mB: Monoid[B]): B =
    in.map(f).foldLeft(mB.empty)(mB.combine)

  def foldMapContext[A, B: Monoid](in: Vector[A])(f: A => B): B =
    in.map(f).foldLeft(Monoid[B].empty)(Monoid[B].combine)

  println(
    foldMap(Vector(1, 2, 3))(identity)
  )
  println(
    foldMapContext(Vector(1, 2, 3))(_.toString + "! ")
  )

  println(
    foldMap("Hello world!".toVector)(_.toString.toUpperCase)
  )

  import cats.Monoid
  import cats.instances.int._
  import scala.concurrent.ExecutionContext.Implicits.global

  val combineResult: Future[Int] =
    Monoid[Future[Int]].combine(Future(1), Future(2))

  def parallelFoldMap[A, B: Monoid](values: Vector[A])(
      func: A => B): Future[B] = {

    val numCores = Runtime.getRuntime.availableProcessors
    val groupSize = (1.0 * values.size / numCores).ceil.toInt

    values
      .grouped(groupSize)
      .map(subSeq => Future(foldMap(subSeq)(func)))
      .reduce(_ |+| _)
  }

  def parallelFoldMapBook[A, B: Monoid](values: Vector[A])(
      func: A => B): Future[B] = {
    // Calculate the number of items to pass to each CPU:
    val numCores = Runtime.getRuntime.availableProcessors
    val groupSize = (1.0 * values.size / numCores).ceil.toInt

    val groups: Iterator[Vector[A]] =
      values.grouped(groupSize)

    val futures: Iterator[Future[B]] =
      groups.map(group => Future(foldMap(group)(func)))

    Future.sequence(futures) map { iterable =>
      iterable.foldLeft(Monoid[B].empty)(_ |+| _)
    }
  }

  //Cats only
  {
    import cats.Monoid
    import cats.Foldable
    import cats.Traverse

    import cats.instances.int._ // for Monoid
    import cats.instances.future._ // for Applicative and Monad
    import cats.instances.vector._ // for Foldable and Traverse

    import cats.syntax.monoid._ // for |+|
    import cats.syntax.foldable._ // for combineAll and foldMap
    import cats.syntax.traverse._ // for traverse

    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    def parallelFoldMap[A, B: Monoid](values: Vector[A])(
        func: A => B): Future[B] = {
      val numCores = Runtime.getRuntime.availableProcessors
      val groupSize = (1.0 * values.size / numCores).ceil.toInt

      values
        .grouped(groupSize)
        .toVector
        .traverse(group => Future(group.toVector.foldMap(func)))
        .map(_.combineAll)
    }

    val future: Future[Int] =
      parallelFoldMap((1 to 1000).toVector)(_ * 1000)

    println(
      Await.result(future, 1.second)
    )

    println(
      Vector(1, 2, 3).foldMap(_ * 2)
    )
  }

}
