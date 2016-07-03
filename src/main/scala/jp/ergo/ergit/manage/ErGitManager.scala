package jp.ergo.ergit.manage

import better.files._
import jp.ergo.ergit.manage.exception.{ErGitManageException, ErGitNotInitializedException, ErGitRepoFileNotFoundException}

import scala.annotation.tailrec


/**
  * manages the config files in .ergit
  */
class ErGitManager {

}

object ErGitManager {


  /**
    * initialize ergit project.
    * this method attempts to create .ergit directory
    *
    * @param directory
    * @throws ErGitManageException if it has already initialized.
    */
  def init(directory: File): Unit = {
    if (isUnderErGit(directory)) {
      throw new ErGitManageException("already initialized.")
    }
    val ergitDirectory = directory / ".ergit"
    ergitDirectory.createDirectory()
    ergitDirectory.createChild("repos")
  }

  /**
    * check recursively
    *
    * @param directory
    * @return true if the directory is under ErGit
    */
  @tailrec
  def isUnderErGit(directory: File): Boolean = {
    directory.children.find(p => p.name == ".ergit") match {
      case None => directory.parentOption match {
        case None => false
        case Some(x) => isUnderErGit(x)
      }
      case _ => true
    }
  }

  /**
    * find repo file recursively and add the repository to the repo file.
    *
    * @param directory
    * @param name
    * @throws ErGitManageException         if the repository already exists
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  def addRepository(directory: File, name: String) = {
    verifyUnderGit(directory)
    val reposFile = getRepoFile(directory)
    val lines = reposFile.lines

    lines find (p => p == name) match {
      case None => reposFile.appendLine(name)
      case _ => throw new ErGitManageException("%s already exists.".format(name))
    }
  }


  /**
    * remove the repository form the repo file. Do nothing if the repository cannot be found.
    *
    * @param directory
    * @param name
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  def removeRepository(directory: File, name: String) = ???

  /**
    * find repo file recursively.
    *
    * @param directory
    * @throws ErGitRepoFileNotFoundException if no repos file found.
    * @throws ErGitNotInitializedException   if no .ergit found.
    * @return the repo file if it found. Throws ErGitManageException otherwise.
    */
  def getRepoFile(directory: File): File = {
    verifyUnderGit(directory)
    val ergitRoot = getErGitRoot(directory)
    ergitRoot.children.find(p => p.name == "repos") match {
      case Some(x) => x
      case _ => throw new ErGitRepoFileNotFoundException("cannot found repos file.")
    }
  }

  /**
    * find the ergit root directory recursively. the returned file's path includes .ergit directory.
    *
    * @param directory
    * @throws ErGitNotInitializedException if no .ergit found.
    * @return the ergit root directory. throws ErGitNotInitializedException otherwise.
    */
  @tailrec
  def getErGitRoot(directory: File): File = {
    directory.children.find(p => p.name == ".ergit") match {
      case None => directory.parentOption match {
        case None => throw new ErGitNotInitializedException()
        case Some(x) => getErGitRoot(x)
      }
      case Some(x) => x
    }
  }

  /**
    *
    * @param directory
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  private def verifyUnderGit(directory: File): Unit = {
    if (!isUnderErGit(directory)) throw new ErGitNotInitializedException()
  }

}