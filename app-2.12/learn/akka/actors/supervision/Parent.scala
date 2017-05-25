package learn.akka.actors.supervision

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging



class Parent extends Actor {
  val log = Logging(context.system, this)
  log.info(s"Parent constructor [$this]")

  private val son = context.actorOf(Child.props)

  override def receive: Receive = {
    case Start =>
      log.info("Starting - sending message to my son")
      son ! WakeUpMySon

    case GotItFather =>
      log.info("Got a message from children")
      throw new IllegalArgumentException("Restart")

    case PreRestart =>
      log.info(s"Got a preRestart message [$this]")
//      son ! WakeUpMySon  - endless recursion ;)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("preRestart: Parent - doing nothing")
    self ! PreRestart
  }

}

case object Start
case object WakeUpMySon
case object GotItFather
case object PreRestart

object Parent {
  def props() = Props(new Parent)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")
    system.actorOf(props()) ! Start
  }
}
