package learn.fpscala

object Collections {

  def main(args: Array[String]): Unit = {
    unzipTest()
    println("----------------------------------")
    scanTest()
  }

  def scanTest() = {
    println { "Scan left test: " +
      List(1, 2, 3, 4).scanLeft(0)(_ + _)
    }
  }

  def unzipTest(): Unit = {
    val numbers = List(1,2,3)
    val names = List("jeden", "dwa", "trzy")

    val zipped: Seq[(Int, String)] = numbers zip names
    println(s"zipped: $zipped")
    val unzipped = zipped.unzip
    println(s"un-zipped: $unzipped")

  }
}
