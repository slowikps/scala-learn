package learn.scala.puzzlers


object Puzzler4NowYouSeeMeNowYouDont extends App {

  /**
    * First, you should remember that every Scala class has a primary constructor that is not explicitly defined,
    * but interwoven with the class definition.
    * [1] All statements in the class definition form the body of the primary constructor,
    * and that includes field definitions (which is, by the way,
    * the reason Scala does not intrinsically differentiate between class fields and values local to the constructor).
    * Hence, all the code in trait A and classes B and C belongs to the constructor body.
    */
  trait A {
    val foo: Int
    val bar = 10
    println("In A: foo: " + foo + ", bar: " + bar)

    def print() = println(s"from A: $bar")
  }

  class B extends A {
    val foo: Int = 25
    println("In B: foo: " + foo + ", bar: " + bar)
  }

  class C extends B {
    override val bar = 99
    println("In C: foo: " + foo + ", bar: " + bar)
  }

  new C().print()

  class Dupa {
    val ten: Int = 10
    val eleven: Int = 11
    println(s"ten: $ten, eleven: $eleven")


  }

  class BigDupa(override val eleven: Int) extends Dupa {
    override val ten: Int = 23
    println(s"BigDupa ten: $ten, eleven: $eleven")

  }

  new BigDupa(23)

  trait Again {
    def a: Int
    lazy val b: Int = 10
    println(s"Again: $a, b: $b")
  }

  class AgainChild extends Again {
    val a = 11
    override lazy val b: Int = 112
  }
  new AgainChild

  println("\n\n\n")
  trait AllInConstructor {
    val a: Int
    println(s"AllInConstructor: $a")
  }
  class AllInConstructorChild(override val a: Int) extends AllInConstructor {
    println(s"AllInConstructorChild: $a")
    def printA() = println(s"From method: $a")
  }

  class AllInConstructorChildChild(override val a: Int) extends AllInConstructorChild(12) {
    println(s"AllInConstructorChildChild: $a")
  }

  new AllInConstructorChildChild(23).printA
}
