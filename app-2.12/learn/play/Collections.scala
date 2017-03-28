package learn.play


object Collections extends App {

  println {
    List(1,2,3,4,1,2,3,4,1,2,3).partition(_ < 2)
  }


}
