package learn.akka.streams
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
/**
  * Created by slowikps on 08/10/16.
  */
object Basic {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    source.runForeach(i => println(i))(materializer)
    println("The End")
  }
}
