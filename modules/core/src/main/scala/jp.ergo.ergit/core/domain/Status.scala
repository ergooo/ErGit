package jp.ergo.ergit.core.domain

case class Status(states: Seq[State])

object Status {
  def apply(statusAsString: String) = {
    val states = statusAsString.split("\n") map { line =>
      StateParser.parse(line)
    }

    new Status(states)
  }
}
