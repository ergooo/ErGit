package jp.ergo.ergit.client.domain.multi.exception

import jp.ergo.ergit.core.domain.{Branch, Repository}

/**
  * @param repositories the list of the repositories that have no branch.
  * @param branch the branch.
  */
case class BranchAlreadyExistException(repositories: Seq[Repository], branch: Branch) extends Exception