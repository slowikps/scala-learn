package learn.scala.puzzlers

/**
  * Created by slowikps on 25/03/17.
  */
object LocationScalaValInheritance extends App {

  {
    trait A {
      val audience: String
      println("Hello " + audience)
    }

    trait AfterA {
      val introduction: String
      println(introduction)
    }

    class BEvery(val audience: String) extends {
      val introduction =
      { println("Evaluating early def"); "Are you there?" }
    } with A with AfterA {
      println("I repeat: Hello " + audience)
    }
    new BEvery("inTheBlock")
  }

  println("\n\n")
  trait A {
    val audience: String
    println("Hello " + audience)
  }

  class BMember(a: String = "World") extends A {
    val audience = a
    println("I repeat: Hello " + audience)
  }

  class BConstructor(val audience: String = "World") extends A {
    println("I repeat: Hello " + audience)
  }

  class Something extends {
    //Error:(23, 12) only concrete field definitions allowed in early object initialization section
    //This allows you to perform additional computations on the constructor arguments (e.g., normalizing the case of string arguments), or to create anonymous classes with correctly initialized values
    //Early definitions define and assign member values before the supertype constructor is called.
    val audience = "initRequiredField"
  } with A {

  }

  new BMember("Readers")
  new BConstructor("Readers")

  val b = new B(10,20)
  println(
    b.b
  )
  b.printB()
}


class A(val a: Int = 11) {
  val b = 19
}

class B(override val a: Int, b: Int) extends A {
  println(s"b: $b")

  def printB() = println(s"b: '$b'")
}




