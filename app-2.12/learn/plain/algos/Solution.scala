package learn.plain.algos

object Solution {

  //a - weights
  //b - target floor
  //m - floors
  //x - people capacity
  //y - weight capacity
  def solution(a: Array[Int], b: Array[Int], m: Int, x: Int, y: Int): Int = {
    def countStops(i: Int, people: Int, weight: Int, floors: Set[Int], stops: Int, acc: Int): Int = {
      if (i == a.length)
        acc + stops + 1
      else if (people == x || weight + a(i) > y)
        countStops(i + 1, 1, a(i), Set(b(i)), 1, acc + stops + 1)
      else if (floors contains b(i))
        countStops(i + 1, people + 1, weight + a(i), floors, stops, acc)
      else
        countStops(i + 1, people + 1, weight + a(i), floors + b(i), stops + 1, acc)
    }

    countStops(0, 0, 0, Set.empty[Int], 0, 0)
  }


  def solution(n: Int, s: String, t: String): String = {
    def coords(s: String): (Int, Char) = {
      val split: Array[String] ="""\d+|\D+""".r.findAllIn(s).toArray
      (split(0).toInt, split(1).head)
    }

    val ships = s.split(",").foldLeft(List.empty[Set[String]]) {
      case (acc, pos) if pos.isEmpty => acc
      case (acc, pos) =>
        val shipPosition = pos.split(" ")
        val begin: (Int, Char) = coords(shipPosition(0))
        val end: (Int, Char) = coords(shipPosition(1))
        val ship = (
          for {
            row <- begin._1 to end._1
            col <- begin._2 to end._2
          } yield row.toString + col
          ).toSet
        ship :: acc
    }
    val hits = t.split(" ").toSet

    def collectHits(ships: List[Set[String]], sunk: Int, hit: Int): String = ships match {
      case Nil => s"$sunk,$hit"
      case ship :: rest =>
        val diff = ship diff hits
        if (diff == Set()) collectHits(rest, sunk + 1, hit) else if (diff == ship) collectHits(rest, sunk, hit) else collectHits(rest, sunk, hit + 1)
    }

    collectHits(ships, 0, 0)
  }
}