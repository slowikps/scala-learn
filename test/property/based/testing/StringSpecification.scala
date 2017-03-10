package property.based.testing

import org.scalatest._
import prop._
import scala.collection.immutable._

class StringSpecification extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  property("concatenation of two strings should starts with first one and include second") {
    forAll { (a: String, b: String) =>
      (a + b) should (startWith(a) and include(b))
    }
  }

    property("concatenate") {
      forAll { (a: String, b: String) =>
        (a+b).length > a.length && (a+b).length > b.length
      }
    }

    property("substring") {
      forAll { (a: String, b: String, c: String) =>
        (a + b + c).substring(a.length, a.length + b.length) == b
      }
    }
}
