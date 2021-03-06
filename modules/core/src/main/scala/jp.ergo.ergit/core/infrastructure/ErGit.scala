package jp.ergo.ergit.core.infrastructure

import java.io.File

import jp.ergo.ergit.core.domain.status.Status
import jp.ergo.ergit.core.infrastructure.exception.RepositoryNotFoundException
import jp.ergo.ergit.domain.exception.{BranchNotFoundException, GitServiceException, NoSuchBranchException, RemoteRepositoryNotFoundException}

import scala.sys.process._


object ErGit {

  private object Proc {
    def brancha(implicit path: String): ProcessBuilder = proc(Seq("git", "branch", "-a"), path)

    def branch(implicit path: String): ProcessBuilder = proc(Seq("git", "branch"), path)

    def branch(path: String, branch: String): ProcessBuilder = proc(Seq("git", "branch", branch), path)
  }

  @throws(classOf[NoSuchBranchException])
  def checkout(path: String, branch: String): Unit = {
    if (!has(path, branch)) throw new NoSuchBranchException("branch not found: " + branch)
    proc(Seq("git", "checkout", branch), path) ! ProcessLogger(str => ())
  }

  def checkoutb(path: String, branch: String): Unit = {
    proc(Seq("git", "checkout", "-b", branch), path) ! ProcessLogger(str => ())
  }

  def fetch(path: String): Unit = {
    try {
      proc(Seq("git", "fetch"), path) !! ProcessLogger(_ => {}, _ => {})
    } catch {
      case _: RuntimeException => throw new RemoteRepositoryNotFoundException("No remote repository specified.")
      case _: Throwable => throw new GitServiceException()
    }
  }

  def hasRemote(path: String): Boolean = {
    val result = proc(Seq("git", "remote", "-v"), path) !!

    !result.isEmpty
  }

  def fetchAndGetAllBranches(path: String): Seq[String] = {
    fetch(path)
    getLocalBranches(path)
  }

  def getCurrentBranch(path: String): String = {
    Proc.branch(path).lineStream find { branch =>
      branch.contains("*")
    } match {
      case Some(f) => f.replace("*", "").trim()
      case _ => throw new BranchNotFoundException("no current branch.")
    }
  }

  def verifyUnderGit(path: String): Unit = {
    if (!isUnderGit(path)) {
      throw new RepositoryNotFoundException("No git repository specified in" + path)
    }
  }

  /**
    * ローカルに存在するブランチをすべて取得する
    *
    * @param path
    * @return
    */
  def getLocalBranches(path: String): Seq[String] = {
    Proc.branch(path).lineStream_!.toList map { branch =>
      branch.replace("*", "").trim()
    }
  }

  def getRemoteBranches(path: String): Seq[String] = {
    Proc.brancha(path).lineStream_!.toList filter { branch =>
      branch.startsWith("remotes/")
    } map { branch =>
      branch.trim()
    }
  }

  def isUnderGit(path: String): Boolean = {
    new File(path, ".git").exists()
  }

  /**
    * get the result of "git status".
    *
    * @param path to the directory to execute git command.
    * @return the result of "git status"
    */
  def getStatus(path: String): String = {
    Process(Seq("git", "status"), new File(path)) !!
  }


  def hasDifferenceBetweenIndexAndWorkingTree(path: String): Boolean = {
    (Process(Seq("git", "diff"), new File(path)) !!) != ""

  }

  def hasDifferenceBetweenIndexAndHead(path: String): Boolean = {
    (Process(Seq("git", "diff", "--cached"), new File(path)) !!) != ""

  }

  def getPorcelainStatus(path: String): String = {
    Process(Seq("git", "status", "--porcelain"), new File(path)) !!
  }

  def createBranch(path:String, branch: String): Unit = {
    Proc.branch(path, branch) !!
  }

  private def has(path: String, branch: String): Boolean = {
    getLocalBranches(path) ++ getRemoteBranches(path) contains branch
  }

  private def proc(command: Seq[String], path: String): ProcessBuilder = {
    Process(command, new File(path))
  }

}
