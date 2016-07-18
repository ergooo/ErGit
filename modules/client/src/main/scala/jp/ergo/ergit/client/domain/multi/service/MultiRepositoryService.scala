package jp.ergo.ergit.client.domain.multi.service

import jp.ergo.ergit.domain.{Repository, ErGitStatus, Branch}
import jp.ergo.ergit.domain.multi.exception.BranchNotExistException


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

  /**
    * checkout the branch of all the repositories. if there are some repositories that have no such branch, throw BranchNotExistException.
    *
    * @param repositories the repositories.
    * @param branch the branch to checkout.
    * @throws BranchNotExistException thrown if there are some repositories that have no such branch.
    *
    */
  def checkout(repositories: Seq[Repository], branch: Branch): Unit = {

    val repositoryNoBranch = repositories filter (p => !p.existsInLocal(branch))
    if (repositoryNoBranch.nonEmpty) {
      throw new BranchNotExistException(repositoryNoBranch, branch)
    } else {
      repositories.foreach(p => p.checkout(branch))
    }
  }
}
