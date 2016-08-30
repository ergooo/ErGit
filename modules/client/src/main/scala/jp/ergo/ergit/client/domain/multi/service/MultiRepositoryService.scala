package jp.ergo.ergit.client.domain.multi.service

import jp.ergo.ergit.client.domain.multi.exception.{BranchAlreadyExistException, BranchNotExistException, RepositoryWorkingInProgressException}
import jp.ergo.ergit.core.domain.{Branch, Repository}


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

  /**
    * checkout the branch of all the repositories. if there are some repositories that have no such branch, throw BranchNotExistException.
    *
    * @param repositories the repositories.
    * @param branch       the branch to checkout.
    * @throws BranchNotExistException              thrown if there are some repositories that have no such branch.
    * @throws RepositoryWorkingInProgressException thrown if the repository working in progress exists.
    *
    */
  def checkout(repositories: Seq[Repository], branch: Branch): Unit = {
    checkIfRepositoryNoBranchExists(repositories, branch)
    checkIfRepositoryWorkingInProgressExists(repositories, branch)

    repositories.foreach(p => p.checkout(branch))

  }

  def checkoutb(repositories: Seq[Repository], branch: Branch): Unit = {
    checkIfRepositoryWorkingInProgressExists(repositories, branch)
    checkIfBranchAlreadyExists(repositories, branch)

    repositories.foreach(p => p.checkoutb(branch))
  }


  private def checkIfRepositoryNoBranchExists(repositories: Seq[Repository], branch: Branch) = {
    val repositoryNoBranch = repositories filter (p => !p.existsInLocal(branch))
    if (repositoryNoBranch.nonEmpty) {
      throw new BranchNotExistException(repositoryNoBranch, branch)
    }
  }

  private def checkIfRepositoryWorkingInProgressExists(repositories: Seq[Repository], branch: Branch) = {
    val repositoryWorkingInProgress = repositories filter (p => !p.getStatus.hasNoChange)
    if (repositoryWorkingInProgress.nonEmpty) {
      throw new RepositoryWorkingInProgressException(repositoryWorkingInProgress, branch)
    }
  }

  private def checkIfBranchAlreadyExists(repositories: Seq[Repository], branch: Branch) = {
    val repositoryBranchAlreadyExists = repositories filter (p => p.existsInLocal(branch))
    if (repositoryBranchAlreadyExists.nonEmpty) {
      throw new BranchAlreadyExistException(repositoryBranchAlreadyExists, branch)
    }
  }
}
