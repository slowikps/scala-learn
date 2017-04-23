package learn.fpscala.exercise.chapter13


object Main {

  import IoOld._


  def main(args: Array[String]): Unit = {
    val p = IoOld.forever(PrintLineOld("Hej Hop"))
    //    p.run //Never ends

    println("The end")

    IO.run(IO.forever(IO.printLine("Still going ..."))) //Never ends
  }
}
