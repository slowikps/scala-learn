package learn.akka.streams

import java.util.concurrent.TimeUnit

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import org.reactivestreams.{Publisher, Subscriber, Subscription}

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by slowikps on 08/10/16.
  */

object ReactiveTweets {

  final case class Author(handle: String)

  final case class Hashtag(name: String)

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashtags: Set[Hashtag] =
      body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
  }

  val akka = Hashtag("#akka")

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  object Pub extends Publisher[Tweet] {
    val subscribers = scala.collection.mutable.Set[Subscriber[_ >: Tweet]]()
    val random = new Random(System.currentTimeMillis)
    val subscription = new Subscription {

      override def cancel(): Unit = println("Subscription is cancelled")
      override def request(n: Long): Unit = {
//        println(s"requested: $n, produced: $produced, consumed: $consumed")
      }
    }
    override def subscribe(s: Subscriber[_ >: Tweet]): Unit = {
      subscribers.add(s)
      s.onSubscribe(subscription)
      run()
    }

    def getTweet: Tweet = {
      if (random.nextInt(10) != 12) Tweet(Author("Akka author"), System.currentTimeMillis(), "#akka and something else")
      else Tweet(Author("Some author"), System.currentTimeMillis(), "#and #something #else")
    }

    var a = 15
    var produced = 0L
    private def run(): Unit = {
      if(subscribers.size == 0) println("No subscribers, the end")
      else {
        val tweet = getTweet
        subscribers.foreach(s => {
          for {
            i <- 0 to 1//a
          } yield {
            produced += 1
            s.onNext(tweet)
          }
        })
//        println(s"Tweet produced: $tweet")

        Future {
          TimeUnit.MILLISECONDS.sleep(200)
          a = a + 1
          run()
        }
        ()
      }
    }

  }
  val tweets: Source[Tweet, NotUsed] = Source.fromPublisher(Pub)

  var consumed = 0L
  def main(args: Array[String]): Unit = {
    Future {
      val authors: Source[Author, NotUsed] =
        tweets
          .filter(_.hashtags.contains(akka))
          .map(in => {consumed += 1; in})
          .map(_.author)

      authors.runWith(Sink.foreach(println))
    }
    TimeUnit.SECONDS.sleep(100)
  }

  val writeAuthors: Sink[Author, Future[Done]] = Sink.foreach[Author](println)
  val writeHashtags: Sink[Hashtag, Future[Done]] = Sink.foreach[Hashtag](println)

  def runGraph(): Unit = {
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[Tweet](2))
      tweets ~> bcast.in
      bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
      bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags
      ClosedShape
    })
    g.run()
    ()
  }
}
