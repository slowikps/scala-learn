package learn.plain.algos

object LetterCount {

  //1 Scala List(Some(1), Some(2)) ...
  //2 Letter count
  //3 Reverse Polish notation
  //4 Cache - how to implement?

  //Hash map - know the answer
  def main(args: Array[String]): Unit = {
    val sets = Set(1, 2, 3, 4).size
    println("Decoding: " + recursive("aaBaacccccccddefagagaa"))
    println(
      "Most letters: " + mostLetters("aaBaacccccccddefagagaa".toList)
    )

  }

  def mostLetters(letters: List[Char]): Any = {
    def go(letters: List[Char], letterToLength: Map[Char, Int], winner: Char): (Char, Int) = letters match {
      case x :: xs => {
        val winnerCount = letterToLength(winner)
        val currentCount = letterToLength.getOrElse(x, 0) + 1

        if (currentCount > winnerCount) go(xs, letterToLength.updated(x, currentCount), x)
        else go(xs, letterToLength.updated(x, currentCount), winner)

      }
      case _ => (winner, letterToLength(winner))
    }

    go(letters.tail, Map(letters.head -> 1), letters.head)
  }

  def recursive(sentence: String): String = {
    def innerRecursive(sentence: List[Char], letter: Char, occurrences: Int): String = sentence match {
      case x :: xs if x == letter => innerRecursive(xs, x, occurrences + 1)
      case x :: xs => s"$letter$occurrences" + innerRecursive(xs, x, 1)
      case _ => s"$letter$occurrences"
    }

    if (sentence.length > 0) innerRecursive(sentence.tail.toList, sentence.head, 1)
    else ""
  }
}
