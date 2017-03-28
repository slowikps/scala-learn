package learn.fpscala.exercise.chapter10

sealed trait WC

case class Stub(chars: String) extends WC //We haven't seen any complete words yet
case class Part(lStub: String, words: Int, rStub: String) extends WC

//Number of complete words we've seen so far


object ParallelParsin {

  val wcMonoid: Monoid[WC] = new Monoid[WC] {
    //ugly crap:
    def op(a1: WC, a2: WC): WC = (a1, a2) match {
      case (Stub(" "), Stub(y)) => Part(" ", 0, y)
      case (Stub(x), Stub(" ")) => Part(x, 0, " ")
      case (Stub(x), Stub(y)) => Stub(x + y)
      case (Stub(" "), Part(_, words, rStub)) => Part(" ", words + 1, rStub)
      case (Stub(x), Part(lStub, words, rStub)) => Part(x + lStub, words, rStub)
      case (Part(lStub, words, _), Stub(" ")) => Part(lStub, words + 1, " ")
      case (Part(lStub, words, rStub), Stub(x)) => Part(lStub, words, rStub + x)
      case (Part(lStub1, words1, _), Part(_, words2, rStub2)) => Part(lStub1, words1 + words2 + 1 /*rStub1 + lStub2*/ , rStub2)
    }

    override def zero: WC = Stub("")
  }

  def countWords(in: String): Int = {
    //FoldMap usage is more elegant for this use-case at it abstract split "in half part", and brings nice char after char processing
    def inner(in: String): WC = {
      if (in.length > 1) {
        val (left, right) = in.splitAt(in.length / 2)
        wcMonoid.op(inner(left), inner(right))
      } else Stub(in)
    }

    inner(in) match {
      case Part(" ", words, " ") => words
      case Part(" ", words, _) => words + 1
      case Part(_, words, " ") => words + 1
      case Part(_, words, _) => words + 2
      case _ => 1
    }
  }

  val wcMonoidBook: Monoid[WC] = new Monoid[WC] {

    override def op(a: WC, b: WC) = (a, b) match {
      case (Stub(c), Stub(d)) => Stub(c + d)
      case (Stub(c), Part(l, w, r)) => Part(c + l, w, r)
      case (Part(l, w, r), Stub(c)) => Part(l, w, r + c)
      case (Part(l1, w1, r1), Part(l2, w2, r2)) =>
        Part(l1, w1 + (if ((r1 + l2).isEmpty) 0 else 1) + w2, r2)
    }

    override def zero: WC = Stub("")
  }

  def countBook(s: String): Int = {
    def wc(c: Char): WC =
      if (c.isWhitespace)
        Part("", 0, "") //to jest sprytne, dzięki temu nie trzeba mieć 8 case in match!
      else
        Stub(c.toString)

    def unstub(s: String) = s.length min 1

    Monoid.foldMapV(s.toIndexedSeq, wcMonoid)(wc) match {
      case Stub(s) => unstub(s)
      case Part(l, w, r) => unstub(l) + w + unstub(r)
    }
  }

}