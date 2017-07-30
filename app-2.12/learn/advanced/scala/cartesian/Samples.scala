package learn.advanced.scala.cartesian
import cats.Cartesian

import scala.concurrent.{Await, Future}


object Samples extends App {

  import cats.instances.option._
  import cats.syntax.option._

  //Product
  val product: Option[(Int, Int)] = Cartesian[Option].product(1.some, 2.some)
  println(
    product
  )

  println(
    Cartesian.tuple3(Option(1), Option(2), Option(3))
  )

  println(
    Cartesian.map3(
      Option(1),
      Option(2),
      Option.empty[Int]
    )(_ + _ + _)
  )

  import cats.instances.option._
  import cats.syntax.cartesian._

  val coTo = (Option(123) |@| Option("abc"))

  println(
    coTo.map(_ + _)
  )

  case class Cat(name: String, born: Int, color: String)

  //Ciekawe:
  (
    Option("Garfield") |@|
      Option(1978) |@|
      Option("Orange and black")
    ).map(Cat.apply)

  import cats.Id


  val tmp = (11: Id[Int]) |@| 12

  val dupa: (String, Int, String) => Cat = (Cat.apply _)


  val concat = tmp |@| List(1,2,3)

  val myCat = Cat("MyCat", 11, "black")
  println(
    "A: " + myCat
  )

  val unapply: (String, Int, String) = Cat.unapply(myCat).get

  case class CatWithId(id: String, name: String, born: Int, color: String)

  def withId(id: Id[String], cat: Cat): (String, (String, Int, String)) =
    (id |@| Cat.unapply(cat).get).tupled

//  def withid2(id: Id[String], cat: Cat) =
  println("Concat: " + concat.tupled)


  println(tmp.tupled)
  import cats.syntax.applicative._
  import cats.instances.future._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val res = Cartesian[Future].product(
    11.pure[Future],
    12.pure[Future]
  )
  Await.result(res, 1.second)
  println(
    res
  )

}
