package learn.fpscala.exercise.chapter6

import learn.fpscala.exercise.chapter6.RNG.Simple

object Main {

  def main(args: Array[String]): Unit = {
    println{
      RNG.nonNegativeInt(Simple(1))
    }
    println{
      RNG.double(Simple(1))
    }
    println{
      "intDouble: " + RNG.intDouble(Simple(1))
    }
    println{
      "doubleInt: " + RNG.doubleInt(Simple(1))
    }
    println{
      "ints: " + RNG.ints(10)(Simple(1))
    }
  }
}
