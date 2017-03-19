package learn.fpscala.exercise.chapter8

import learn.fpscala.exercise.chapter6.RNG
import learn.fpscala.exercise.chapter6.RNG.Simple

/**
  * Created by slowikps on 17/03/17.
  */
object Main {

  def main(args: Array[String]): Unit = {
    println(
      Gen.listOfN(10, Gen.boolean).sample.run(Simple(1))
    )
  }
}
