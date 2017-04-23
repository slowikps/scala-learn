package learn.fpscala.exercise.chapter13

import learn.fpscala.exercise.chapter11.Monad


trait IoOld[A] {
  self =>
  def run: A

  def map[B](f: A => B): IoOld[B] = new IoOld[B] {
    override def run: B = f(self.run)
  }

  def flatMap[B](f: A => IoOld[B]): IoOld[B] = new IoOld[B] {
    override def run: B = f(self.run).run
  }
}

object IoOld extends Monad[IoOld] {
  override def flatMap[A, B](ma: IoOld[A])(f: (A) => IoOld[B]): IoOld[B] = {
    ma flatMap f
  }

  def PrintLineOld(msg: String): IoOld[Unit] = IoOld {
    println(msg)
  }

  def apply[A](a: => A): IoOld[A] = unit(a) //This makes MyIO a worse version of above

  override def unit[A](a: => A): IoOld[A] = new IoOld[A] {
    override def run: A = a
  }
}

class MyIO[A](run: => A) {
  self =>
  def map[B](f: A => B): MyIO[B] = new MyIO(f(self.run))

  def flatMap[B](f: A => IoOld[B]): MyIO[B] = new MyIO(f(self.run).run)
}