package learn.plain.collections

import scala.collection.mutable.ArrayBuffer

object PatternMatching extends App {
  val myList: Seq[String] = ArrayBuffer("abc")

  {
//    import scala.collection.immutable._
    myList match {
      case a@Seq("abc") => println(s"It match: Seq(abc), created seq: $a")
      case _ => println("Doesn't!!!")
    }
  }

  {
    import scala.collection.immutable._
    (Seq("abc")) match {
      //    case "abc" :: Nil => println("It match: abc :: Nil")
      case a@Seq("abc") => println(s"It match: Seq(abc), created seq: $a")
      case _ => println("Doesn't!!!")
    }
  }
}
