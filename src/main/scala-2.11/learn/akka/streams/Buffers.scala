package learn.akka.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.concurrent.duration._
case class Tick() {
  println("in tick constructor")
}

object Buffers {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer(
    ActorMaterializerSettings(system)
      .withInputBuffer(
      initialSize = 1,
      maxSize = 1)
  )

  def main(args: Array[String]): Unit = {
    val rG = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      // this is the asynchronous stage in this graph
      val zipper = b.add(ZipWith[Tick, String, String]((tick, count) => count).async)

      Source.tick(initialDelay = 3.second, interval = 3.second, {println("tick called"); Tick()}) ~> zipper.in0

      Source.tick(initialDelay = 1.second, interval = 1.second, "message!")
        .conflateWithSeed(seed = (_) => {println("seed called"); "1"})((count, _) => {println(s"Count called: $count"); count + 1}) ~> zipper.in1

      zipper.out ~> Sink.foreach(println)
      ClosedShape
    })
    rG.run()
  }
}
