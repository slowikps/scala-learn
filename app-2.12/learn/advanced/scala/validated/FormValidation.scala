package learn.advanced.scala.validated

import cats.Cartesian

object FormValidation extends App {

  type FormData = Map[String, String]

  /**
    * Our goal is to implement code that parses the incoming data enforcing the following rules:
    * the name and age must be specified;
    * the name must not be blank;
    * the the age must be a valid non-negative integer.
    */

  import cats.data.Validated
  import cats.syntax.either._

  type ErrorsOr[A] = Either[List[String], A]
  type AllErrorsOr[A] = Validated[List[String], A]
  val getName = getValue("name") _

  def parseIntMy(name: String)(data: String): ErrorsOr[Int] =
    Either.catchOnly[NumberFormatException](data.toInt)
      .leftMap(_ => List(s"$name is not integer value [data: $data]"))

  import cats.instances.list._

  def readUser(data: FormData): AllErrorsOr[User] =
    Cartesian[AllErrorsOr].product(
      readName(data).toValidated,
      readAge(data).toValidated
    ).map(User.tupled)

  import cats.syntax.cartesian._

  def readUser2(data: FormData): AllErrorsOr[User] =
    (
      readName(data).toValidated |@|
        readAge(data).toValidated
      ).map(User.apply)

  def readName(data: FormData): ErrorsOr[String] =
    getValue("name")(data).
      flatMap(nonBlank("name"))

  def getValue(name: String)(data: FormData): ErrorsOr[String] =
    data.get(name).
      toRight(List(s"$name field not specified"))

  def nonBlank(name: String)(data: String): ErrorsOr[String] =
    Right(data).ensure(List(s"$name must not be blank"))(_.nonEmpty)

  def readAge(data: FormData): ErrorsOr[Int] =
    getValue("age")(data).
      flatMap(nonBlank("age")).
      flatMap(parseInt("age")).
      flatMap(nonNegative("age"))

  def parseInt(name: String)(data: String): ErrorsOr[Int] =
    Right(data).
      flatMap(s => Either.catchOnly[NumberFormatException](s.toInt)).
      leftMap(_ => List(s"$name must be an integer"))

  def nonNegative(name: String)(data: Int): ErrorsOr[Int] =
    data.asRight[List[String]]
      .ensure(List(s"$name must not be negative"))(_ >= 0)

  case class User(name: String, age: Int)

  println(
    "readUser: " + readUser(Map())
  )
  println(
    "readUser: " + readUser(Map("name" -> "", "age" -> "s"))
  )
  println(
    parseInt("age")("11")
  )
  println(
    parseInt("age")("foo")
  )

  println(
    nonBlank("name")("")
  )


}
