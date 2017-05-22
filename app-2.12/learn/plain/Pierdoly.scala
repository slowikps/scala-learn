package learn.plain

import java.util

//import collection.JavaConversions._
import scala.collection.{Iterable, mutable}

/**
  * Created by slowi on 05/02/2017.
  */
object Pierdoly {

  def main(args: Array[String]): Unit = {
    val javaList = new util.ArrayList[String]()

//    val scalaSeq: mutable.Seq[String] = javaList

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
