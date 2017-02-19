package learn.plain.algos

/**
  * Created by slowi on 01/02/2017.
  */
object LetterCount {

  def main(args: Array[String]): Unit = {
    println(recursive("aaBaacccccccddefgg"))
  }

  def recursive(sentence: String): String = {
    def innerRecursive(sentence: List[Char], letter: Char, occurencies: Int): String = sentence match {
      case x :: xs  if x == letter => innerRecursive(xs, x, occurencies + 1)
      case x :: xs => s"$letter$occurencies"  + innerRecursive(xs, x, 1)
      case _ => s"$letter$occurencies"
    }

    if(sentence.length > 0) innerRecursive(sentence.tail.toList, sentence.head,1)
    else ""
  }
}
