package learn.play.wiring

import com.softwaremill.macwire._
import learn.play.controllers.{AsyncController, Greetings}
import play.api.ApplicationLoader.Context
import play.api._


//import router.Routes

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context) with play.filters.HttpFiltersComponents {

  private implicit def as = actorSystem

  lazy val router = null
  // Controllers
  private lazy val greetings = wire[Greetings]
  private lazy val asyncController = wire[AsyncController]
  // Router
  private lazy val routePrefix: String = "/"

}