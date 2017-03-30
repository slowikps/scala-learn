package learn.akka.actors


import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef}
import learn.akka.actors.Greeter.WaitingForReplay

import scala.concurrent.duration._
import scala.language.postfixOps

object Greeter {
  case object Greet
  case object Tick
  case object Done
  case object GotIt
  case object WaitingForReplay
}

class Greeter(val parent: ActorRef) extends Actor {
  import context.dispatcher

  var maxTicks = 15

  def receive = {
    case Greeter.Greet =>
      TimeUnit.MILLISECONDS.sleep(300)
      println("Hello World!")
      sender() ! Greeter.GotIt

    case Greeter.Tick =>
      maxTicks -= 1
      if(maxTicks > 0) {
        context.system.scheduler.scheduleOnce(50 millis, self, Greeter.Tick)
        println(s"Ticks left: $maxTicks")
      } else {
        parent ! Greeter.Done
      }

    case WaitingForReplay => sender() ! 11.0
  }
}