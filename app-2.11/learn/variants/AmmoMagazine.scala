package learn.variants

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

final class AmmoMagazine[+A <: Bullet](
                                        private[this] var bullets: List[A]) {

  def hasBullets: Boolean = !bullets.isEmpty

  def giveNextBullet(): Option[A] =
    bullets match {
      case Nil => {
        None
      }
      case t :: ts => {
        bullets = ts
        Some(t)
      }
    }

  def addBullets(newBullets: List[ExplosiveBullet]): Unit = {
//    bullets = bullets ::: newBullets
  }
}