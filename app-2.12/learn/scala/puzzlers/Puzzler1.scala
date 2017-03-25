package learn.scala.puzzlers

object Puzzler1 extends App {


  List(1, 2).map { i => println("Hi"); i + 1 }
  List(1, 2).map {
    //It's easy to think that these curly braces represent an anonymous function,
    //but instead they delimit a block expression: one or multiple statements, with the last determining the result of the block.
    //
    // The key lesson here is that the scope of an anonymous function defined using placeholder
    // syntax stretches only to the expression containing the underscore (_).
    // This differs from a "regular" anonymous function, whose body contains everything from the rocket symbol (=>) to the end of the code block. H
    println("Hi")
    _ + 1
  }

}
