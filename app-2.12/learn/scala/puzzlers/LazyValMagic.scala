package learn.scala.puzzlers


object LazyValMagic extends App {

  class Value
  case class Values(val first: Value, second: () => Value) extends Value

  def forever(v: Value): Value = {
    lazy val tmp = forever(v)
    new Values(v, () => tmp)
  }

  def run(v: Value): Unit = v match {
    case Values(a, b) => {
      println("forever alone")
      run(b())
    }
  }

  run(forever(new Value()))
}