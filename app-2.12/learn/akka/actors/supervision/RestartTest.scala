package learn.akka.actors.supervision

import java.util.UUID
import java.util.concurrent.RejectedExecutionException

import akka.actor.{Actor, ActorSystem, Props}

object RestartTest extends App {

  val system = ActorSystem("mySystem")

  val actor = system.actorOf(Props(new Restart))

  actor ! "Throw"
  actor ! "Say hello"

  println("The end?")
}

class Restart extends Actor {
  val id = UUID.randomUUID()
  println(s"Actor starting [id: $id]")

  override def receive: Receive =  {
    case "Throw" => throw new RejectedExecutionException("I am thorowing exception")
    case m => println(s"I am logging message: $m")
  }

}
