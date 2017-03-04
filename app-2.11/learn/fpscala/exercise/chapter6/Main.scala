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
    println("-----------------------------------")
    println{
      "doubleViaMap: " + RNG.doubleViaMap(Simple(1))
    }
    println{
      "nonNegativeLessThan: " + RNG.nonNegativeLessThan(24)(Simple(1))
    }
    val machine = Machine(true, 5, 10)
    val st: State[Machine, (Int, Int)] = State.simulateMachine(List[Input](Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn))

    println{
      "machineTest: " + st.run(machine)
    }
  }
}
