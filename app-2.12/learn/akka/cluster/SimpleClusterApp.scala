package learn.akka.cluster

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.util.Random

object SimpleClusterApp {
  def main(args: Array[String]): Unit = {
    println(s"Args: '${args.mkString(" ")}'")
    if (args.isEmpty)
      startup(Seq("2551", "2552", "0"))
    else
      startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(ConfigFactory.load("simple-cluster.conf"))

      println("user.dir=" + config.getString("pawel.user.dir"))
      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      // Create an actor that handles cluster domain events
      system.actorOf(Props[SimpleClusterListener], name = "clusterListener")
      system.actorOf(PingActor.props, name = s"pingActor${Random.nextInt(100)}")
    }
  }

}

