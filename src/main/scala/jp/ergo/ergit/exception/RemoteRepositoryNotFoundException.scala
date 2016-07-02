package jp.ergo.ergit.exception

class RemoteRepositoryNotFoundException(message: String = null, cause: Throwable = null) extends GitServiceException(message, cause)
