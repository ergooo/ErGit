package jp.ergo.ergit.core.infrastructure.exception

class RepositoryNotFoundException(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
