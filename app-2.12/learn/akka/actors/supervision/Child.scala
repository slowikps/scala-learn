package learn.akka.actors.supervision

import akka.actor.{Actor, Props}
import akka.event.Logging


class Child extends Actor {
  val log = Logging(context.system, this)
  log.info(s"Child constructor [$this]")
  override def receive: Receive = {
    case WakeUpMySon =>
      log.info("I am awake father!")
      sender() ! GotItFather
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"I am about to restart [reason: $reason, message: $message, this: $this]")
    super.preRestart(reason: Throwable, message: Option[Any])
  }

  override def postStop(): Unit =
    log.info(s"Child postStop [$this]")

}

object Child {
  def props = Props(new Child)
}
