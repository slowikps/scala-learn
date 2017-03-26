//package learn.fpscala.exercise.chapter9
//
//
//trait JSON {
//
//}
//
//object JSON {
//
//  def jsonParser[Error, Parser[+ _]](P: Parsers[Error, Parser]): Parser[JSON] = {
//    ???
//  }
//
//  case class JNumber(get: Double) extends JSON
//
//  case class JString(get: String) extends JSON
//
//  case class JBool(get: Boolean) extends JSON
//
//  case class JArray(get: IndexedSeq[JSON]) extends JSON
//
//  case class JObject(get: Map[String, JSON]) extends JSON
//
//  case object JNull extends JSON
//}
//
///**
//  * JSON parsing example.
//  */
//object JSONExample extends App {
//  val jsonTxt =
//    """
//{
//  "Company name" : "Microsoft Corporation",
//  "Ticker"  : "MSFT",
//  "Active"  : true,
//  "Price"   : 30.66,
//  "Shares outstanding" : 8.38e9,
//  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
//}
//"""
//
//  val malformedJson1 =
//    """
//{
//  "Company name" ; "Microsoft Corporation"
//}
//"""
//
//  val malformedJson2 =
//    """
//[
//  [ "HPQ", "IBM",
//  "YHOO", "DELL" ++
//  "GOOG"
//  ]
//]
//"""
//
////  val P = fpinscala.parsing.Reference
////  val json: Parser[JSON] = JSON.jsonParser(P)
////
////  def printResult[E](e: Either[E, JSON]) =
////    e.fold(println, println)
////  printResult {
////    P.run(json)(jsonTxt)
////  }
////  println("--")
////  printResult {
////    P.run(json)(malformedJson1)
////  }
////  println("--")
////  printResult {
////    P.run(json)(malformedJson2)
////  }
//}