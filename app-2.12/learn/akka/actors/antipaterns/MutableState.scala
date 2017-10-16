package learn.akka.actors.antipaterns

import akka.actor.{Actor, ActorSystem, DeadLetter, Props}

import scala.collection.mutable.ArrayBuffer

class DeadLetterListener extends Actor {
  println("Dead letter actor started")

  def receive = {
    case d: DeadLetter => println(s"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Hop hop, deadLetter: $d>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    case x => println(s"<<<<<Got $x>>>>>>")
  }
}

class MutableState extends Actor {
  println("Initialized")

  val arr = new ArrayBuffer[String]() //Same array is passed
  arr.append("Nic tu nie ma")

  val mutator = context.actorOf(Props(new MutateOtherActorState))

  mutator ! arr

  override def receive: Receive = {
    case x =>
      println(s"Got something: $x")
      println(s"My array is: $arr")
      mutator ! "Bye"
      mutator ! arr
  }

}

class MutateOtherActorState extends Actor {

  println("Starting MutateOtherActorState")

  override def receive: Receive = {
    case ArrayBuffer() => println("Empty list not doing anything ")

    case state@ArrayBuffer(_*) =>
      println(state)
      state.clear()
      sender() ! "Done"

    //    case x => println(s"WRONG MESSAGE $x")
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")
    val listener = system.actorOf(Props[DeadLetterListener])
    system.eventStream.subscribe(listener, classOf[DeadLetter])
    system.actorOf(Props[MutableState])
    ()
  }
}
