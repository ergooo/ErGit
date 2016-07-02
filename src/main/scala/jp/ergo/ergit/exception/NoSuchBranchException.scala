package jp.ergo.ergit.exception

class NoSuchBranchException(message: String = null, cause: Throwable = null) extends GitServiceException(message, cause)
