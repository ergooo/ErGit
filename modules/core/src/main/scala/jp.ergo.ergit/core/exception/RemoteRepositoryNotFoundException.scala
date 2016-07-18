package jp.ergo.ergit.domain.exception

class RemoteRepositoryNotFoundException(message: String = null, cause: Throwable = null) extends GitServiceException(message, cause)
