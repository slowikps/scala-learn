package learn.scala.puzzlers


object Puzzler2UPSTAIRSDownstairs extends App{

  val HOUR = 13; val MINUTE, SECOND = 0;

  //This trickiness arises because multiple-variable assignments are based on pattern matching, and within a pattern match, variables starting with an uppercase letter take on a special meaning: they are stable identifiers.
  var (HOUR, MINUTE, SECOND) = (12, 0, 0)

}
