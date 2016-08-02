package jp.ergo.ergit.core.infrastructure

trait EnumLike {
  type Value
  def value: Value
}
trait StringEnumLike extends EnumLike {
  type Value = String
}


trait EnumCompanion[A <: EnumLike] {

  def values: Seq[A]

  def valueOf(value: A#Value): Option[A] = values.find(_.value == value)

}
