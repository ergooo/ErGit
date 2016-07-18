package jp.ergo.ergit.client.domain.multi.exception

import jp.ergo.ergit.domain.{Repository, Branch}

/**
  * @param repositories the list of the repositories that have no branch.
  * @param branch the branch.
  */
case class BranchNotExistException(repositories: Seq[Repository], branch: Branch) extends Exception