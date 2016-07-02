package jp.ergo.ergit.ergit.exception

class RemoteRepositoryNotFoundException(message: String = null, cause: Throwable = null) extends GitServiceException(message, cause)
