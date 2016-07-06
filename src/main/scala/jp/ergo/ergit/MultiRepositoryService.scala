package jp.ergo.ergit

import jp.ergo.ergit.repository.{Branch, Repository}


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
  def getStatus(repositories: Seq[Repository]): Seq[(String, String)] = {
    repositories map { r =>
      (r.name, r.getStatus)
    }
  }
}
