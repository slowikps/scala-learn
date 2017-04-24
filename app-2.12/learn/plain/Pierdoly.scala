package learn.plain

/**
  * Created by slowi on 05/02/2017.
  */
object Pierdoly {
  def main(args: Array[String]): Unit = {
    println {
      java.util.UUID.fromString("ca3-f02-3-3-39")
    }
    println{
      "123456789".substring(0, 9)
    }

    println {
      List(1,2,3,4) zip List("A" , "B")
    }
    println("End")

    println("Start")
  }
}
