package learn.play.wiring

import play.api.ApplicationLoader.Context
import play.api._
import com.softwaremill.macwire._
import learn.play.controllers.{AsyncController, Greetings}
import router.Routes

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context) {

  private implicit def as = actorSystem

  // Controllers
  private lazy val greetings = wire[Greetings]
  private lazy val asyncController = wire[AsyncController]

  // Router
  private lazy val routePrefix: String = "/"
  lazy val router = wire[Routes]

}