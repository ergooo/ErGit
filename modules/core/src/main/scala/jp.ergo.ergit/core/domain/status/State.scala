package jp.ergo.ergit.core.domain.status

import better.files.File


sealed trait State

case class Modified(file: File, position: Position) extends State
case class Added(file: File, position: Position) extends State
case class Deleted(file: File, position: Position) extends State
case class Renamed(from: File, to: File, position: Position) extends State
case class Copied(from: File, to: File, position: Position) extends State
case class Unmerged(file: File, position: Position) extends State
case class Untracked(file: File, position: Position) extends State

