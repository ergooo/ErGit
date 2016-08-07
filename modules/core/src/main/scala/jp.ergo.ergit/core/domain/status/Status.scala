package jp.ergo.ergit.core.domain.status

case class Status(states: Seq[State])

object Status {
  def apply(porcelain: String) = {
    val states = porcelain.split("\n") map { line =>
      StateParser.parse(line)
    }

    new Status(states)
  }
}
