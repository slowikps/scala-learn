package learn.plain

trait Convert[X]{
  def toInt(x: X): Int

}

object FuckUp extends App {

  def method[X: Convert](t: X): Unit = {
    println(s"Hej ho in method ${implicitly[Convert[X]].toInt(t)}")
  }

  implicit val a = new Convert[String] {
    def toInt(x: String) = x.length
  }

//  implicit val tt: String = "Pawel"
  method("whatEver")
}

