package learn.fpscala

import java.util.concurrent.{Executors, TimeUnit}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Pierdoly {

  def makeSomeString(txt: String, in: Int) = s"$txt: $in"

  def curriedTest: (String) => (Int) => String = (makeSomeString _).curried
  def unCurriedTest: (String, Int) => String = Function.uncurried(curriedTest)
  def unCurriedTest2 = Function.uncurried((st: String) => curriedTest)


  def add(x:Int, y:Int, z:Int) = x + y + z

  val addFive = add(_: Int, 5, _:Int)

  def intToString(in: Int): String = s"$in"

  def repeat(in: String): String = s"$in$in"

  def main(args: Array[String]): Unit = {
    val singleThread = Executors.newSingleThreadScheduledExecutor()
    singleThread.submit(new Runnable{
      override def run(): Unit = {
        println(s"Run and throw exception ${Thread.currentThread().getName}:${Thread.currentThread().getId}")
        throw new RuntimeException("Kaboom")
      }
    })

    singleThread.submit(new Runnable{
      override def run(): Unit = {
        println(s"Run another one and throw exception ${Thread.currentThread().getName}:${Thread.currentThread().getId}")
        throw new RuntimeException("Kaboom2")
      }
    })

    def andThenTest = intToString _ andThen repeat
    def composeTest = (repeat _) compose intToString
    println(andThenTest(23))
    println(composeTest(23))

    curringTest()
  }

  private def curringTest() = {
    val curriedTest = List(1, 2, 3, 4) map {
      (_: Int) * (_: Int)
    }.curried
    println {
      curriedTest.foldLeft(1)((acc, f) => f(acc))
    }
  }
}
