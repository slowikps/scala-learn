package learn.akka.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}

import scala.concurrent.duration._

/**
  * Created by slowikps on 16/10/16.
  *
  * How many times consumer is faster than producer?
  *
  */
object ZipTest {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val rG = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val source = Source(0 to 100)
      val slowDown = Flow[Int].throttle(1, 1.second, 1, ThrottleMode.shaping)

      val zipWithIdx = Flow[Int].zip(Source.fromIterator(() => Iterator.from(1).map(_ => System.currentTimeMillis())))

      //First element is not going do be returned by stream
      val timeBetweenEvents = Flow[(Int, Long)].sliding(2)
        .map(seq => {
          (seq(1)._1, (seq(0)._2 - seq(1)._2) / -1000d)
        })


      //Grouped
//      val grouped: Flow[(Int, Int), Seq[(Int, Int)], NotUsed] = Flow[(Int, Int)].grouped(2)

      source ~> slowDown ~> zipWithIdx ~>
        timeBetweenEvents ~>
        Sink.foreach(println)
      ClosedShape
    })
    rG.run()
    ()
  }
}
