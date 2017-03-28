package property.based.testing.fps

import learn.fpscala.exercise.chapter10.{Monoid, ParallelParsin}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

/**
  * Created by slowikps on 26/03/17.
  */
class MonoidSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {
  import learn.fpscala.exercise.chapter10.Monoid._
  property("intAddition monoid laws test") {

    def monoid = intAddition
    forAll{ a: Int =>
      monoid.op(a, monoid.zero) should equal (monoid.op(monoid.zero, a))
      monoid.op(a, monoid.zero) should equal (a)

    }

    monoid.op(monoid.zero, monoid.zero) should equal(monoid.zero)

    forAll { (a: Int, b: Int) =>
      monoid.op(a, b) should equal (monoid.op(b, a))
    }

    forAll { (a: Int, b: Int, c: Int) =>
      monoid.op(monoid.op(a, b), c) should equal (monoid.op(a, monoid.op(b, c)))
    }
  }

  val intAdditionLawTest = monoidLaws(intAddition)
  val intMultiplicationLawTest = monoidLaws(intMultiplication)
  val booleanAndLawTest = monoidLaws(booleanAnd)
  val booleanOrLawTest = monoidLaws(booleanOr)

  //Start Not Monoids
//  val isOrderedMonoidLawTest = monoidLaws(isOrderedMonoid)
//  val isOrderedMonoidBookLawTest = monoidLaws(isOrderedMonoidBook)
  //End Not Monoids
//  val wcMonoidLawTest = monoidLaws(ParallelParsin.wcMonoid)

  def monoidLaws[A](m: Monoid[A])(implicit g: Arbitrary[A]) = {
    print("Opppa")
    forAll{ a: A =>
      m.op(a, m.zero) should equal (m.op(m.zero, a))
      m.op(a, m.zero) should equal (a)
    }

    m.op(m.zero, m.zero) should equal(m.zero)

    forAll { (a: A, b: A) =>
      m.op(a, b) should equal (m.op(b, a))
    }

    forAll { (a: A, b: A, c: A) =>
      m.op(m.op(a, b), c) should equal (m.op(a, m.op(b, c)))
    }
  }

}
