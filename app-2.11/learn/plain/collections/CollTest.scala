package learn.plain.collections

/**
  * Created by slowikps on 05/02/17.
  */
object CollTest {

  def main(args: Array[String]): Unit = {
    println {
      List(1, 2, 3, 4, 5).foldLeft(0) {
        (acc, x) => {
          println(s"acc: $acc, x: $x")
          acc + x
        }
      }
    }
    println("-----------------------------------")
    println {
      List(1, 2, 3, 4, 5).foldRight(0) {
        (x, acc) => {
          println(s"acc: $acc, x: $x")
          acc + x
        }
      }
    }


    println{
      (1 to 10) + " " + (1 until 10)
    }
  }
}
