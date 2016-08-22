package jp.ergo.ergit.core.domain.status

case class Status(states: Seq[State], displayableString: String, porcelainString: String) {
  def hasNoChange = states.isEmpty
}

object Status {

  def apply(porcelain: String, displayableString: String) = {
    porcelain match {
      case "" => new Status(Seq(), displayableString, porcelain)
      case _ => {
        val states = porcelain.split("\n") map { line =>
          StateParser.parse(line)
        }
        new Status(states, displayableString, porcelain)
      }
    }
  }
}
