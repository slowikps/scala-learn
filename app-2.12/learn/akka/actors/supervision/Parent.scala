package learn.akka.actors.supervision

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging



class Parent extends Actor {
  val log = Logging(context.system, this)
  log.info(s"Parent constructor [$this]")

  private var son: ActorRef = _


  override def preStart(): Unit = {
    log.info("Bang in preStart method")
    this.son = context.actorOf(Child.props)
  }

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
//    self ! PreRestart
    postStop()
  }
  override def postRestart(reason: Throwable): Unit = ()
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
