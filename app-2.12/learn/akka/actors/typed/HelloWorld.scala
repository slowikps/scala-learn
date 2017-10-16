package learn.akka.actors.typed

import akka.typed._
import akka.typed.scaladsl.Actor
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

object HelloWorld {

  val greeter = Actor.immutable[Greet] { (_, msg) ⇒
    println(s"Hello ${msg.whom}!")
    msg.replyTo ! Greeted(msg.whom)
    Actor.same
  }

  final case class Greet(whom: String, replyTo: ActorRef[Greeted])

  final case class Greeted(whom: String)
}

object Main extends App {

  import HelloWorld._
  // using global pool since we want to run tasks after system.terminate
  import scala.concurrent.ExecutionContext.Implicits.global

  val system: ActorSystem[Greet] = ActorSystem("hello", greeter)

  implicit val timeout = Timeout(3.seconds)
  implicit val scheduler = system.scheduler
  val future: Future[Greeted] = system ? (Greet("world", _))

  for {
    greeting ← future.recover { case ex ⇒ ex.getMessage }
    done ← {
      println(s"result: $greeting"); system.terminate()
    }
  } println("system terminated")

}