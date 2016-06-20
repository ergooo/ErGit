package jp.ergo.scala.checkoutkun.ergit

import java.io.File

import jp.ergo.scala.checkoutkun.ergit.exception.{RemoteRepositoryNotFoundException, NoSuchBranchException, BranchNotFoundException, GitServiceException}

import scala.sys.process._


object ErGit {

  private object Proc {
    def brancha(implicit path: String): ProcessBuilder = proc(Seq("git", "branch", "-a"), path)

    def branch(implicit path: String): ProcessBuilder = proc(Seq("git", "branch"), path)
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
    try {
      proc(Seq("git", "status"), path) !! ProcessLogger(_ => {}, _ => {})
      true
    } catch {
      case _: Throwable => false
    }
  }

  private def has(path: String, branch: String): Boolean = {
    getLocalBranches(path) ++ getRemoteBranches(path) contains branch
  }

  private def proc(command: Seq[String], path: String): ProcessBuilder = {
    Process(command, new File(path))
  }

}
