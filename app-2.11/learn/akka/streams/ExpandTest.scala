package learn.akka.streams


import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.collection.immutable.Seq
import scala.concurrent.duration._
/**
  * Created by slowikps on 16/10/16.
  *
  * How many times consumer is faster than producer?
  *
  */
object ExpandTest {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val rG = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val source = Source(0 to 100)
      val slowDown = Flow[Int].throttle(1, 1.second, 1, ThrottleMode.shaping)
      val driftFlow: Flow[Int, (Int, Int), NotUsed] = Flow[Int]
        .expand(streamElement => Iterator.from(0).map(streamElement -> _))
//          .buffer(10, OverflowStrategy.backpressure)


      //Stateful Map
      val firstValueUnique = Flow[(Int, Int)].statefulMapConcat(() => {
            var previousValue: Option[(Int, Int)] = None
            (in) => {
              val result = previousValue match {
                case Some(x) if x._1 != in._1 => List(x)
                case _ => List()
              }
              previousValue = Some(in)
              result
            }
      }
      )

      //Grouped
//      val grouped: Flow[(Int, Int), Seq[(Int, Int)], NotUsed] = Flow[(Int, Int)].grouped(2)

      source ~> slowDown ~> driftFlow ~> firstValueUnique ~> Sink.foreach(println)
      ClosedShape
    })
    rG.run()
    ()
  }
}
