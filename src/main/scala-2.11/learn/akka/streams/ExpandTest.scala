package learn.akka.streams


import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.concurrent.duration._
/**
  * Created by slowikps on 16/10/16.
  */
object ExpandTest {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val rG = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val source = Source(0 to 100)

      source ~> Sink.foreach(println)
      ClosedShape
    })
    rG.run()
  }
}
