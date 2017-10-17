package property.based.testing

import org.scalacheck.Gen

object OwnGenerator extends App {

  val simpleGen: Gen[Option[Int]] = Gen.option(Gen.choose(400, 599))

  println(
    List.fill(10)(simpleGen.sample).mkString(", ")
  )

  val nullable: java.lang.Boolean = null
  if (nullable) {
    println("Not null and true")
  } else {
    println("Something else: " + nullable)
  }


  def dupa(a: Nothing) = {}
//  val stringGen: Gen[Option[Int]] = Gen.
}
