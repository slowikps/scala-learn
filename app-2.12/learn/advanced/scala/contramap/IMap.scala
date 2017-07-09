package learn.advanced.scala.contramap

object IMap extends App {

  trait Codec[A] {
    def encode(value: A): String

    def decode(value: String): Option[A]

    def imap[B](dec: A => B, enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        override def encode(value: B): String =
          (enc andThen self.encode) (value)

        override def decode(value: String): Option[B] =
          self.decode(value).map(dec)
      }
    }
  }

  def encode[A](value: A)(implicit c: Codec[A]): String =
    c.encode(value)

  def decode[A](value: String)(implicit c: Codec[A]): Option[A] =
    c.decode(value)


  case class Box[A](value: A)

  implicit val stringCodec = new Codec[String] {

    import cats.syntax.option._

    override def encode(value: String): String = value

    override def decode(value: String): Option[String] = value.some
  }

  implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] =
    codec.imap[Box[A]](Box.apply, _.value)

  val encodedBox = encode(Box("Pawel"))
  println(encodedBox)
  println(decode[Box[String]](encodedBox))
}
