package jp.ergo.scala.checkoutkun

import jp.ergo.scala.checkoutkun.repository.{Branch, Repository}

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

}
