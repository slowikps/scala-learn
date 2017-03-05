package learn.scala_z

import scalaz._
import Scalaz._

object ApplicativeTest {

  def main(args: Array[String]): Unit = {
    1.point[List].myPrint
  }

  implicit class Printable[A](in: A) {
    def myPrint() = println(in)
  }

}
