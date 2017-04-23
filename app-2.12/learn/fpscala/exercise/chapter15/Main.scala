package learn.fpscala.exercise.chapter15


object Main extends App {

  val liftOne = Process.liftOne((x: Int) => x * 2)
  val lift = Process.lift((x: Int) => x * 2)

  println(
    liftOne(Stream(1, 2, 3)).toList
  )

  println(
    lift(Stream(1, 2, 3)).toList
  )

  println(
    Process.sum(Stream(1, 2, 3)).toList
  )


}
