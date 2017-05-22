package learn.presentations.futureandpromise

import java.util.concurrent.TimeUnit


case class FridayTreat(name: String) {
  def and(other: FridayTreat) = FridayTreat(s"$name and ${other.name}")
}

object FridayEvening extends App {
  val start = System.currentTimeMillis

  def info(msg: String) = printf("%.2f: %s\n", (System.currentTimeMillis - start) / 1000.0, msg)

  def provide(name: String) = {
    TimeUnit.SECONDS.sleep(1)
    info(s"Friday treat done: $name")
    FridayTreat(name)
  }

  def consume(treat: FridayTreat) = info(s"About to consume: $treat")

  val bear = provide("beer")
  val pizza = provide("pizza")


  consume(bear and pizza)
}
