package learn.fpscala.exercise.chapter8
import Prop._
import Gen._
object Usage extends App {

  val smallInt = Gen.choose(-10, 10)

  val maxProp = forAll(listOf1(smallInt)) { ns =>
    val max = ns.max
    !ns.exists(_ > max)
  }

  val sortedProp = forAll(listOf(smallInt)) { ns =>
    val nss = ns.sorted
    // We specify that every sorted list is either empty, has one element,
    // or has no two consecutive elements `(a,b)` such that `a` is greater than `b`.
    (nss.isEmpty || nss.tail.isEmpty || !nss.zip(nss.tail).exists {
      case (a,b) => a > b
    }) &&
       !ns.exists(!nss.contains(_)) && // Also, the sorted list should have all the elements of the input list,
       !nss.exists(!ns.contains(_)) // and it should have no elements not in the input list.
  }

  run(maxProp)
  run(sortedProp)

}
