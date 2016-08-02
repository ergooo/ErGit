package jp.ergo.ergit.core.domain

import better.files.File
import jp.ergo.ergit.core.infrastructure.{EnumCompanion, StringEnumLike}


sealed trait State

case class Modified(file: File, position: Position) extends State
case class Added(file: File, position: Position) extends State
case class Deleted(file: File, position: Position) extends State
case class Renamed(from: File, to: File, position: Position) extends State
case class Copied(from: File, to: File, position: Position) extends State
case class Unmerged(file: File, position: Position) extends State
case class Untracked(file: File, position: Position) extends State

