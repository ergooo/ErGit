package jp.ergo.ergit

import jp.ergo.ergit.repository.{ErGitStatus, Branch, Repository}


class MultiRepositoryService(repositories: Seq[Repository]) {
  def checkout(branch: Branch): Unit = {
    repositories foreach { repository =>
      repository.checkout(branch)
    }
  }

  def checkoutb(branch: Branch): Unit = {
    repositories foreach { repository =>
      repository.checkoutb(branch)
    }
  }


}

object MultiRepositoryService {
  def getStatuses(repositories: Seq[Repository]): Seq[ErGitStatus] = {
    repositories map { r =>
      ErGitStatus(r.name, r.getStatus)
    }
  }
}
