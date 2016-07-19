package jp.ergo.ergit.domain.exception

class NoSuchBranchException(message: String = null, cause: Throwable = null) extends GitServiceException(message, cause)
