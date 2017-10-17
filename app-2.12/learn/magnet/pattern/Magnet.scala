package learn.magnet.pattern

sealed trait Magnet {
  type Result
  def apply(): Result
}

/**
  * Apparently this patter resolve an issue with type erasure
  * Collisions caused by type-erasure probably pose the most severe problem of “traditional” method overloading on the JVM, since there is no clean work-around.
  * It actually prevents us from implementing our complete overloads in the usual fashion, as can be seen by the following error the Scala compiler produces when we try to:
  */
object Magnet {

  implicit def fromInt(in: Int) =
    new Magnet {

      override def apply(): Int = in
      override type Result = Int
    }

//  implicit def fromInt2(in: Int) =
//    new Magnet {
//
//      override def apply(): String = s"$in"
//      override type Result = String
//    }

  implicit def fromString(in: String) =
    new Magnet {

      override def apply(): String = in
      override type Result = String
    }

  implicit def fromIntAndString(in: (Int, String)) =
    new Magnet {

      override def apply(): String = s"The string is: ${in._2}, and the int is: ${in._1}"
      override type Result = String
    }


}
