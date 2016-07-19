package jp.ergo.ergit.core.domain

class Status(index: Seq[State], workingTree: Seq[State])
object Status {
  def apply(statusAsString: String) = {
    new Status(Seq(), Seq())
  }
}

sealed trait State

case class Modified(fileName: String) extends State
case class Added(fileName: String) extends State
case class Deleted(fileName: String) extends State
case class Renamed(from: String, to: String) extends State
case class Copied(from: String, to: String) extends State
case class Unmerged(fileName: String) extends State
case class Untracked(fileName: String) extends State

