package jp.ergo.scala.checkoutkun.app

import java.io.File

case class Config(repositories: File = new File("./repositories.txt"), branch: String = "", status: Boolean = false,
                  checkout: Boolean = false)
