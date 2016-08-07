package jp.ergo.ergit.core.domain.status

import better.files.File
import jp.ergo.ergit.core.domain.status.Position.{Both, Index, WorkingTree}

object StateParser {

  def parse(line: String): State = {
    val pattern = """(.{2}) (.*)""".r
    line match {
      case pattern(flags, filePart) =>
        flags.trim.headOption match {
          case Some('M') => Modified(File(filePart.trim), parsePosition(flags.toList))
          case Some('A') => Added(File(filePart.trim), parsePosition(flags.toList))
          case Some('D') => Deleted(File(filePart.trim), parsePosition(flags.toList))
          case Some('R') => Renamed(retrieveFrom(filePart), retrieveTo(filePart), parsePosition(flags.toList))
          case Some('C') => Copied(retrieveFrom(filePart), retrieveTo(filePart), parsePosition(flags.toList))
          case Some('?') => Untracked(File(filePart.trim), parsePosition(flags.toList))
          case _ => throw new IllegalArgumentException(line + "is invalid.")
        }
      case _ => throw new IllegalArgumentException(line + "is invalid.")
    }
  }

  private def parsePosition(flags: List[Char]): Position = {
    flags match {
      case List(' ', _) => WorkingTree
      case List(_, ' ') => Index
      case _ => Both
    }
  }

  private def retrieveFrom(filePart: String): File = {
    val Array(from, _*) = filePart.split("->") map { e => File(e.trim) }
    from
  }

  private def retrieveTo(filePart: String): File = {
    val Array(_, to, _*) = filePart.split("->") map { e => File(e.trim) }
    to
  }

}



