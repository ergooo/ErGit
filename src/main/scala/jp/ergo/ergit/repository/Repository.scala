package jp.ergo.ergit.repository

import better.files.File
import jp.ergo.ergit.ErGit
import jp.ergo.ergit.exception.NoSuchBranchException


case class Repository(path: String, name: String, branches: Seq[Branch], remoteBranches: Seq[Branch]) {


  def existsInLocal(branch: Branch): Boolean = {
    branches exists { b =>
      branch.name == b.name
    }
  }

  def existsInRemote(branch: Branch): Boolean = {
    remoteBranches exists { b =>
      branch.name == b.name
    }
  }

  @throws[NoSuchBranchException]
  def checkout(branch: Branch): Unit = {
    ErGit.checkout(path, branch.name)
  }

  def checkoutb(branch: Branch): Unit = {
    ErGit.checkoutb(path, branch.name)
  }

  def getCurrentBranch: Branch = {
    val currentBranchName = ErGit.getCurrentBranch(path)
    branches find { b =>
      b.name == currentBranchName
    } head
  }

  def getStatus: String = {
    ErGit.getStatus(path)
  }

}

object Repository {
  def apply(name: String, rootDirectory: File): Repository = {
    if (!ErGit.isUnderGit(rootDirectory.pathAsString)) {
      throw new RepositoryNotFoundException("No git repository specified in" + rootDirectory.pathAsString)
    }
    val path = rootDirectory.pathAsString
    val branches = ErGit.getLocalBranches(path) map { b =>
      Branch(b)
    }
    val remoteBranches = if (ErGit.hasRemote(path)) {
      ErGit.getRemoteBranches(path) map { b =>
        Branch(b)
      }
    } else {
      Seq()
    }
    Repository(path, name, branches, remoteBranches)
  }

  def apply(rootDirectory: File): Repository = {
    Repository(rootDirectory.name, rootDirectory)
  }
}