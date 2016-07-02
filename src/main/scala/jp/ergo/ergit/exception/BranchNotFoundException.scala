package jp.ergo.ergit.exception

class BranchNotFoundException(message: String, cause: Throwable = null) extends GitServiceException(message, cause)
