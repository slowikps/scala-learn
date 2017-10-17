package learn.magnet.pattern


object Main extends App {

  val myService = new MyService()


  val res: Int = myService.doSomething(1)

  println(res)
  println(myService.doSomething("Hi there"))
  /**
    * W teorii to powinno być automatycznie zamienione na tuple?
    * Chyba zostało to wywalone?
    * -Ywarn-adapted-args, that warns in case of autotupling
    * -Yno-adapted-args, that gives an error under the same circumstances
    */
  //   println(myService.doSomething(11, "Hi there"))


//  val test = getMe(11)

//  def getMe(in: Int): Int = in
//  def getMe(in: Int): String = s"$in"
}
