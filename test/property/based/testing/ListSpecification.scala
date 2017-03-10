package property.based.testing

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class ListSpecification extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  property("sum: List[Int] properties") { //This line is critical - otherwise Intellij doesn't see any tests
    forAll { l: List[Int] =>
      (l.sum) should equal(l.reverse.sum)
    }

    forAll { l: List[Int] =>
      (l.sum) should equal(l.foldLeft(0)(_ + _))
    }

    forAll { l: List[Int] =>
      if(l.toSet.size == 1) (l.sum) should equal (l.head * l.length)
    }
  }

}
