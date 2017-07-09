package learn.advanced.scala

import cats.Show

final case class Cat(name: String, age: Int, color: String)

object CatsShow extends App {

  val myInt = 23
  val myString = "Pawel's String"

  import cats.instances.int._
  import cats.instances.string._

  val showString = Show[String]
  val showInt = Show[Int]

  println(showString.show(myString))
  println(showInt.show(myInt))


  import cats.syntax.show._

  println(myString.show)
  println(myInt.show)

  implicit val catShow = Show.show[Cat] { cat =>
    //Which one do you like more? with or without variables
    val name = cat.name.show
    s"$name is a ${cat.age.show} year-old ${cat.color} cat."
  }

  2 +: Nil

  case class Test(number: Int) {
    import language.postfixOps

    def +:(in: Int) = Test(in + number)
    def plus(in: Int) = +: _
  }

  implicit val showTest = Show.show[Test](in => s"Test[number: ${in.number}]")

  println((13 +: Test(10)).show)

  println(Cat("Natka", 31, "White").show)
}
