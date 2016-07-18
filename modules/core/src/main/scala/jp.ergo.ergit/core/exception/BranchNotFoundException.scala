package jp.ergo.ergit.domain.exception

class BranchNotFoundException(message: String, cause: Throwable = null) extends GitServiceException(message, cause)
