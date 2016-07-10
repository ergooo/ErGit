package jp.ergo.ergit.multi.exception

import jp.ergo.ergit.repository.{Branch, Repository}

/**
  * @param repositories the list of the repositories that have no branch.
  * @param branch the branch.
  */
case class BranchNotExistException(repositories: Seq[Repository], branch: Branch) extends Exception