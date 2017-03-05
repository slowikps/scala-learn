package learn.fpscala.exercise.chapter7

import java.util.concurrent.{Executors, TimeUnit}

object Main {

  def main(args: Array[String]): Unit = {
    val ex = Executors.newFixedThreadPool(4)

    val echoer = Actor[String](ex) {
      msg => println(s"Got message: '$msg'")
    }
    println("About to send the message")
    echoer ! {TimeUnit.MILLISECONDS.sleep(2000); "Dupa"}
  }
}
