package learn.fpscala.exercise.chapter13

import learn.fpscala.exercise.chapter11.Monad


trait IO[A] { self =>
  def run: A
  def map[B](f: A => B): IO[B] = new IO[B] {
    override def run: B = f(self.run)
  }

  def flatMap[B](f: A => IO[B]): IO[B] = new IO[B] {
    override def run: B = f(self.run).run
  }
}
object IO extends Monad[IO] {
  override def unit[A](a: => A): IO[A] = new IO[A] {
    override def run: A = a
  }

  override def flatMap[A, B](ma: IO[A])(f: (A) => IO[B]): IO[B] = {
    ma flatMap f
  }

  def apply[A](a: => A): IO[A] = unit(a) //This makes MyIO a worse version of above

  def PrintLine(msg: String): IO[Unit] = IO{println(msg)}
}

class MyIO[A](run: => A) { self =>
  def map[B](f: A => B): MyIO[B] = new MyIO(f(self.run))
  def flatMap[B](f: A => IO[B]): MyIO[B] = new MyIO(f(self.run).run)
}