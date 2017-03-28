package learn.fpscala.exercise.chapter9

import learn.fpscala.exercise.chapter8.{Gen, Prop}

import scala.util.matching.Regex

trait Parsers[ParseError, Parser[+ _]] { self =>
  //Parser[+_] ????? Scala syntax for a type parameter that is itself a type constructor.?????
  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  //Recognizes and return a single String
  implicit def string(s: String): Parser[String]

  implicit def operators[A](p: Parser[A]) = ParserOps[A](p)

  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  def char(c: Char): Parser[Char] = string(c.toString).map(_.charAt(0))

  def succeed[A](a: A): Parser[A] = string("").map(_ => a)

  def or[A](s1: Parser[A], s2: => Parser[A]): Parser[A]

  //Zero or more
  def many[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p))(_ :: _) or succeed(List())
  //many in terms of or,map2 and succeed

  //One or more
  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p))(_ :: _)

  //Sequences two parser, running p1 and then p2, and return the pair of their results if both succeed
  def product[A,B](p: Parser[A], p2: => Parser[B]): Parser[(A,B)] = flatMap(p)(a => map(p2)(b => (a,b)))

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] = if(n <= 0) succeed(List()) else map2(p, listOfN(n -1, p))(_ :: _)

  //Apply f to result of p, if successful
  def map[A,B](p: Parser[A])(f: A => B): Parser[B] = p.flatMap(a => succeed(f(a)))

  //Returns portion of the String examined by the parser if successful
  def slice[A](p: Parser[A]): Parser[String]

  def zeroOrMore(c: Char): Parser[Int] = many(char(c)).map(_.length)

  def numA: Parser[Int] = zeroOrMore('a')

  def map2[A, B, C](p: Parser[A], p2: => Parser[B])(f: (A,B) => C): Parser[C] =
    for { a <- p; b <- p2 } yield f(a,b)
//    (p ** p2).map(f.tupled)

  def flatMap[A,B](p: Parser[A])(f: A => Parser[B]): Parser[B]


  implicit def regex(r: Regex): Parser[String]

  //TODO: how it works? Czy to jest to samo co z mapami?
  def contextSensitive: Parser[Int] = /* We'll just have this parser return the number of `"a"` characters read. Note that we can declare a normal value inside a for-comprehension. */
//    for {
//      digit <- "[0-9]+".r
//      n = digit.toInt // we really should catch exceptions thrown by toInt and convert to parse failure
//      _ <- listOfN(n, char('a'))
//    } yield n
    regex("[0-9]+".r).map(_.toInt).map(nr => (nr, listOfN(nr, char('a')))).map(_._1)




  case class ParserOps[A](p: Parser[A]) {
    def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)

    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)

    def map[B](f: A => B): Parser[B] = self.map(p)(f)

    def many() = self.many(p)

    def **[B](p2: Parser[B]) = self.product(p, p2)

    def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)
  }

  case object Laws {
    val str = "str"
    run(char(str.charAt(0)))(str.charAt(0).toString) == Right('s')
    run(string(str))(str) == Right(str)

    run(listOfN(3, "ab" | "cad"))("ababcad") == Right("ababcad")
    run(listOfN(3, "ab" | "cad"))("cadabab") == Right("cadabab")

    run(numA)("aaa") == Right(3)
    run(numA)("b") == Right(0)

    run(succeed("ABC"))("sdfsdfsd") == Right("ABC")

    run(slice((char('a') | char('b')).many))("aaba") == Right("aaba")

    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop =
      Prop.forAll(in)(s => run(p1)(s) == run(p2)(s))

    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop =
      equal(p, p.map(a => a))(in)
  }

}
