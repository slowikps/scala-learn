package learn.parprog

/**
  * Created by slowikps on 22/01/17.
  */
object AggregateTest {

  def main(args: Array[String]): Unit = {
    //Number of numbers greater than two
    println {
      List(1, 2, 3, 4, 5, 6, 1, 2, 3).par.aggregate(0)((acc, nbr) => if (nbr > 2) acc + 1 else acc, _ + _)
    }
  }
}
