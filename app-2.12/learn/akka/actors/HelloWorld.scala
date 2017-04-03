package learn.akka.actors

import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps

class HelloWorld extends Actor {
  import context.dispatcher

  lazy val greeter = context.actorOf(Props(new Greeter(self)), "greeter")

  implicit val timeout = Timeout(5 seconds) // needed for `?` below

  override def preStart(): Unit = {
    // create the greeter actor
    // tell it to perform the greeting
    greeter ! Greeter.Tick
    greeter ! Greeter.Greet

    val res = greeter ? Greeter.WaitingForReplay //This is not the preferable way of sending messages
    res.mapTo[Double].map(in => println(s"The result is: $in"))
    () //Achh 
  }

  def receive = {
    // when the greeter is done, stop this actor and with it the application
    case Greeter.GotIt =>
      greeter ! Greeter.Greet

    case Greeter.Done =>
      println("Done")
      context.stop(self)
  }
}