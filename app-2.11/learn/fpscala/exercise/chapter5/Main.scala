package learn.fpscala.exercise.chapter5

/**
  * Created by slowi on 25/02/2017.
  */
object Main {

  def main(args: Array[String]): Unit = {
    println {
      Stream(1, 2, 3, 4).toList
    }
    println {
      Stream(1, 2, 3, 4, 5).take(2).toList
    }
    println {
      Stream(1, 2, 3, 4, 5).drop(2).toList
    }

    println {
      "Take while via fold: " + Stream(1, 2, 3, 4, 5).takeWhileViFold(_ < 5).toList
    }
    println {
      "HeadOption: " + Stream(1, 2, 3, 4, 5).headOption
    }
    println {
      "HeadOption: " + Stream().headOption
    }

    println {
      "Map: " + (Stream(1, 2, 3, 4, 5).map(_ + 5).toList == Stream(1, 2, 3, 4, 5).mapUnfold(_ + 5).toList)
    }
    println {
      "Map: " + Stream(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0).toList
    }
    println {
      "Constant: " + (Stream.constant("Pawel").take(10).toList == Stream.constant("Pawel").takeUnfold(10).toList)
    }

    println {
      "From: " + (Stream.from(23).take(10).toList == Stream.fromUnfold(23).take(10).toList)
    }

    println {
      "Fibonacci: " + ( Stream.fibs().take(30).toList == Stream.fibsUnfold().take(30).toList)
    }
    println {
      "Tails: " + Stream(1,2,3).tails.map(_.toList).toList
    }
    println {
      "Scan Right: " + Stream(1,2,3).scanRight(0)(_ + _).toList
    }
  }
}
