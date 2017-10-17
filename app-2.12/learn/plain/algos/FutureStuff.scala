package learn.plain.algos

import java.util.UUID
import java.util.concurrent.TimeUnit

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureStuff extends App {
  println("Start")

  def sum(in: List[Future[Int]]): Future[Int] = {
    in.foldLeft(Future.successful(0)) {
      case (facc, ft) => facc.flatMap(acc => ft.map(_ + acc))
    }
  }

  var in: List[Future[Int]] = List(Future.successful(1), Future.failed(new RuntimeException("Boom")), Future.successful(2), Future.successful(3))

  in = in.map(_.recover { case _ => 0 })

  Future.traverse(List(1, 2, 3, 4))(in => Future.successful(in)).map(_.sum).map(println)
  Future.sequence(in).map(_.sum).map(in => println(s"ho ho ho: $in"))

  println {
    "Some res: " + List[Option[Int]](Some(1), Some(2), Some(3)).reduce((x, y) => x.flatMap(a => y.map(_ + a)))
  }
  in.reduce((f1, f2) => f1.flatMap(r1 => f2.map(_ + r1))).map(println)
  in.foldLeft(Future.successful(0))((acc, x) => acc.flatMap(acc1 => x.map(_ + acc1))).map(println)

  println(
    "Diff test: " + (List(1, 2, 3, 1) diff List(2, 1))
  )
  println(
    "Diff set test: " + (List(1, 2, 3, 1).toSet diff List(2, 1).toSet)
  )
  val testUid = UUID.randomUUID()
  println(
    "Set test on testClass: " + (List(DiffTestCase(testUid, "Pawel"), DiffTestCase(UUID.randomUUID(), "Pawel"), DiffTestCase(testUid, "Pawel")).toSet)
  )


  TimeUnit.SECONDS.sleep(2)

}

case class DiffTestCase(id: UUID, test: String)