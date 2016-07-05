package jp.ergo.ergit.app

import better.files.File

case class Config(command: Command = Command.None, action: Action = Action.None, verbose: Boolean = false, repositoryPath: String = "", repositoryName: String = "")




