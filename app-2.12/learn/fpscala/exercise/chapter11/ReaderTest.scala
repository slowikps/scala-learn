package learn.fpscala.exercise.chapter11

//11.20
object ReaderTest extends App {

  case class Reader[R, A](run: R => A)

  object Reader {
    def readerMonad[R] = new Monad[({type f[x] = Reader[R, x]})#f] {

      override def unit[A](a: => A): Reader[R, A] = Reader(_ => a)

      override def flatMap[A, B](ma: Reader[R, A])(f: (A) => Reader[R, B]): Reader[R, B] =
        Reader((r: R) => f(ma.run(r)).run(r))

    }
  }

  val intReader: Reader[String, Int] = Reader((in: String) => Integer.parseInt(in))

  println(
    Reader.readerMonad.flatMap(intReader)(in => Reader(_ => in + 10)).run("23")
  )

  println(intReader.run("11") + 5)
  println("End")
}
