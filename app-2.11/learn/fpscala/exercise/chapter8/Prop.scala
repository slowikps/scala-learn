package learn.fpscala.exercise.chapter8




trait Prop {
  def check: Either[(Prop.FailedCase, Prop.SuccessCount), Prop.SuccessCount]

//  def &&(other: Prop) = new Prop {
//    def check = Prop.this.check && other.check
//  }
}

object Prop {
  type SuccessCount = Int
  type FailedCase = String
}
