package learn.play.controllers

import akka.actor.ActorSystem
import play.api.mvc.{AbstractController, Action, Controller, ControllerComponents}

import scala.concurrent.Future

class Greetings(components: ControllerComponents)(implicit actorSystem: ActorSystem)  extends AbstractController(components) {

  import actorSystem.dispatcher

  // Essentially copied verbatim from the SIRD example
  def hello(to: String) = Action {
    Ok(s"Hello $to")
  }

  /*
     Use Action.async to return a Future result (sqrt can be intense :P)
     Note the use of double(num) to bind only numbers (built-in :)
   */
  def sqrt(num: Double) = Action.async {
    Future {
      Ok(Math.sqrt(num).toString)
    }
  }
}