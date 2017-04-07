package learn.plain

/**
  * Created by slowikps on 15/10/16.
  */
object OptionTest {

  def getOption(con: Boolean) = if(con) Some(11) else None

  def main(args: Array[String]): Unit = {
    println(
      "Option of null: " + Option(null)
    )

    println("With <- and j " +
      (for {
        i <- Option(11)
        j <- getOption(false)
      } yield (i))
    )

    println("With <- and _ " +
      (for {
        i <- Option(11)
        _ <- getOption(false)
      } yield (i))
    )

    println("With <- and _ (and success) " +
      (for {
        i <- Option(11)
        _ <- getOption(true)
      } yield (i))
    )



//    for {
//      input <- List(Some("SomeTesxt"), None)
//    } yield {
//      println(
//        input + " " +
//          input.collect {
//            case in => ":" + in + ":"
//          }
//      )
//    }
  }
//
//    implicit val ev: Int <:< Option[String] = { def apply(x: Int): Option[String] = Some("Input: " + x)  }
//    val none: Some[String] = none
////    println {
////            Some("Flatten Test").flatten
////    }
//    //    val ev: Int <:< Option[String] = in => Some("Test")
////    val fun: (Object) => Option[String] = (a: Int) => Option("Test")
//  }

}
