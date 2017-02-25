package learn.fpscala.exercise.chapter4
import scala.{Option => _, Some => _, Either => _, _}

object Main {

  def main(args: Array[String]): Unit = {
    println(
      Some("Pawel").orElse(Some("Gawel"))
    )

    println(
      "No None in the sequence: " + Option.sequence(List(Some(1), Some(2), Some(3))) + ", " +
       Option.sequence4(List(Some(1), Some(2), Some(3)))
    )

    println(
      "None in the sequence: " + Option.sequence(List(Some(1), Some(2), Some(3), Some(4), None, Some(5))) + ", " +
       Option.sequence4(List(None, Some(1), Some(2), Some(3), Some(4), None, Some(5)))
    )

    println{
      "Traverse result: " + Option.traverse(List(1,2,3,4))(Some(_))
    }
  }
}
