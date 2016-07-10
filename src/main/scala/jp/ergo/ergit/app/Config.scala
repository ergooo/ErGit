package jp.ergo.ergit.app

case class Config(
                   command: Command = Command.None,
                   action: Action = Action.None,
                   verbose: Boolean = false,
                   repositoryPath: String = "",
                   repositoryName: String = "",
                   branchName: String = ""
                 )




