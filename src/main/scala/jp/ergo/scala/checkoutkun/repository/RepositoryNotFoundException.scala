package jp.ergo.scala.checkoutkun.repository

import jp.ergo.scala.checkoutkun.ergit.exception.GitServiceException

class RepositoryNotFoundException(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
