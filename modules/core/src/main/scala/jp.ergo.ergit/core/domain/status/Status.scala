package jp.ergo.ergit.core.domain.status

case class Status(states: Seq[State])

object Status {
  var Empty = new Status(Seq())

  def apply(porcelain: String) = {
    porcelain match {
      case "" => Empty
      case _ => {
        val states = porcelain.split("\n") map { line =>
          StateParser.parse(line)
        }
        new Status(states)
      }
    }
  }
}
