package jp.ergo.ergit.core.domain.status

import jp.ergo.ergit.core.infrastructure.{EnumCompanion, StringEnumLike}

sealed abstract class Position(val value:String) extends StringEnumLike

object Position extends EnumCompanion[Position]{
  case object Index extends Position("index")
  case object WorkingTree extends Position("workingTree")
  case object Both extends Position("Both")

  lazy val values = Seq(Index, WorkingTree, Both)
}