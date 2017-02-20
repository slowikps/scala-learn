package learn.fpscala.exercise.chapter4
import scala.{Option => _, Some => _, Either => _, _}

object Main {

  def main(args: Array[String]): Unit = {
    println(
      Some("Pawel").orElse(Some("Gawel"))
    )
  }
}
