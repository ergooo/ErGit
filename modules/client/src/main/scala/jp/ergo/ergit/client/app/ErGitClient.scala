package jp.ergo.ergit.client.app

import better.files.File
import jp.ergo.ergit.client.domain.manage.ErGitManager
import jp.ergo.ergit.client.domain.multi.exception.{BranchAlreadyExistException, BranchNotExistException, RepositoryWorkingInProgressException}
import jp.ergo.ergit.client.domain.multi.service.MultiRepositoryService
import jp.ergo.ergit.core.domain.{Branch, Repository}

object ErGitClient {

  val currentDirectory = File("./")

  def main(args: Array[String]): Unit = {


    val parser = new scopt.OptionParser[Config]("ErGit") {
      head("ErGit", "0.1")

      cmd("init").action((_, c) => c.copy(command = Command.Init)).text("init is a command.")

      cmd("repository").action((_, c) => c.copy(command = Command.Repository)).text("repository is a command.").
        children(
          opt[Unit]("v").abbr("v").action((_, c) => c.copy(verbose = true)).text("v is a verbose."),
          cmd("add").action((_, c) => c.copy(action = Action.Add)).text("add is an action").
            children(
              arg[String]("<name>").required().action((x, c) => c.copy(repositoryName = x)),
              arg[String]("<path>").required().action((x, c) => c.copy(repositoryPath = x))
            ),
          cmd("remove").action((_, c) => c.copy(action = Action.Remove)).text("add is an action").
            children(
              arg[String]("<name>").required().action((x, c) => c.copy(repositoryName = x))
            )
        )

      // TODO: get repository name.
      cmd("status").action((_, c) => c.copy(command = Command.Status)).text("status is a command.")

      cmd("checkout").action((_, c) => c.copy(command = Command.Checkout)).text("checkout is a command.").
        children(
          arg[String]("<branch>").required().action((x, c) => c.copy(branchName = x)),
          opt[Unit]("b").abbr("b").action((_, c) => c.copy(createBranch = true)).text("b is an option.")
        )

      cmd("branch").action((_, c) => c.copy(command = Command.Branch)).text("branch is a command.").
        children(
          opt[Unit]("a").abbr("a").action((_, c) => c.copy(allBranches = true)).text("a is an option.")
        )
    }

    // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
        config.command match {
          case Command.Init => ErGitManager.init(currentDirectory)
          case Command.Repository =>
            config.action match {
              case Action.Add =>
                ErGitManager.addRepository(currentDirectory, Repository(config.repositoryName, File(config.repositoryPath)))
              case Action.Remove =>
                ErGitManager.removeRepository(currentDirectory, config.repositoryName)
              case _ =>
                if (config.verbose) {
                  val repositories = ErGitManager.getRepositories(currentDirectory)
                  repositories.foreach(r => println("%s %s".format(r.name, r.path)))
                }
            }
          case Command.Status =>
            val repositories = ErGitManager.getRepositories(currentDirectory)
            repositories foreach { x =>
              println("%s\n%s".format(x.name, x.getStatus.displayableString))
            }

          case Command.Checkout =>
            if (config.createBranch) {
              val repositories = ErGitManager.getRepositories(currentDirectory)
              try {
                MultiRepositoryService.checkoutb(repositories, Branch(config.branchName))
              } catch {
                case e: RepositoryWorkingInProgressException =>
                  val message = "the following repositories is working in progress:\n%s".format(e.repositories.map(f => "%s\n%s".format(f.name, f.getStatus.porcelainString)).mkString("\n"))
                  println(message)

                case e: BranchAlreadyExistException =>
                  val message = "the following repository already has the specified branch: \n%s".format(e.repositories.map(f => f.name).mkString("\n"))
                  println(message)
              }
            } else {
              val repositories = ErGitManager.getRepositories(currentDirectory)
              try {
                MultiRepositoryService.checkout(repositories, Branch(config.branchName))
              } catch {
                case e: BranchNotExistException =>
                  val message = "the following repositories doesn't have the specified branch: %s\n%s".format(config.branchName, e.repositories.map(f => f.name).mkString("\n"))
                  println(message)

                case e: RepositoryWorkingInProgressException =>
                  val message = "the following repositories is working in progress:\n%s".format(e.repositories.map(f => "%s\n%s".format(f.name, f.getStatus.porcelainString)).mkString("\n"))
                  println(message)
              }
            }

          case Command.Branch =>
            if (config.allBranches) {
              val repositories = ErGitManager.getRepositories(currentDirectory)
              repositories foreach { x =>
                val localBranches = x.branches.map(b => "%s %s".format(if (x.getCurrentBranch == b) "*" else " ", b.name)).mkString("\n")
                val remoteBranches = x.remoteBranches.map(r => "remotes/%s".format(r.name)).mkString("\n")
                println("%s:\n%s%s\n".format(x.name, localBranches, remoteBranches))
              }
            } else {
              val repositories = ErGitManager.getRepositories(currentDirectory)
              repositories foreach { x =>
                val localBranches = x.branches.map(b => "%s %s".format(if (x.getCurrentBranch == b) "*" else " ", b.name)).mkString("\n")
                println("%s:\n%s\n".format(x.name, localBranches))
              }
            }

          case _ =>
        }

      case None =>
      // arguments are bad, error message will have been displayed
    }
  }
}
