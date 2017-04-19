package learn.fpscala.exercise.chapter13


object Main {
  import IO._


  def main(args: Array[String]): Unit = {
    val p = IO.forever(PrintLine("Hej Hop"))
    p.run //Never ends
  }
}
