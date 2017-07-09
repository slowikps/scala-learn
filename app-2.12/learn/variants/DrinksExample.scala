package learn.variants

trait Drink

trait SoftDrink extends Drink

trait Juice extends Drink

class Cola extends SoftDrink

class TonicWater extends SoftDrink

class OrangeJuice extends Juice

class AppleJuice extends Juice

class CovarianceVendingMachine[+A] {
  // .. don't worry about implementation yet
}
class ContravariantVendingMachine[-A] {
  // .. don't worry about implementation yet
}

object DrinksExample {
  {
    //Covariance
    def install(softDrinkVM: CovarianceVendingMachine[SoftDrink]): Unit = {
      // Installs the soft drink vending machine
    }
//    install(new CovarianceVendingMachine[Drink]) //Will not compile
    install(new CovarianceVendingMachine[SoftDrink])
    install(new CovarianceVendingMachine[Cola])// +A
    install(new CovarianceVendingMachine[TonicWater])// +A
  }
  {
    //Contravariant
    def install(softDrinkVM: ContravariantVendingMachine[SoftDrink]): Unit = {
      // Installs the soft drink vending machine
    }
//    install(new CovarianceVendingMachine[Drink]) //Will not compile
    install(new ContravariantVendingMachine[SoftDrink])
    install(new ContravariantVendingMachine[Drink])// -A
//    install(new ContravariantVendingMachine[TonicWater])// -A
  }


}
