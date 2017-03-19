/*Cannot belong to any package if one want to be able to use:
*   scala ReplTest.scala
* or
*   1 start sbt: sbt
*   2 load module: :load ReplTest.scala
*   3 run main method:  ReplTest.main(Array[String]())
*
*   As you can see it doesn't require compilation of the class
*/

object ReplTest {

  def main(args: Array[String]): Unit = {
    println("In main class")
  }
}
