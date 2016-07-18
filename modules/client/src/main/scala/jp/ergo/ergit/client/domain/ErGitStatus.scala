package jp.ergo.ergit.client.domain

case class ErGitStatus(repositoryName: String, status: String) {

  override def toString = {
    "%s\n%s\n".format(repositoryName, status)
  }
}
