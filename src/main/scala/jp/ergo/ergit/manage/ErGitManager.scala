package jp.ergo.ergit.manage

import java.io.File
import java.nio.charset.StandardCharsets
import java.util

import jp.ergo.ergit.manage.exception.{ErGitRepoFileNotFoundException, ErGitNotInitializedException, ErGitManageException}
import org.apache.commons.io.FileUtils

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
    if (isUnderErGit(directory.getAbsoluteFile)) {
      throw new ErGitManageException("already initialized.")
    }
    val ergitDirectory = new File(directory, ".ergit")
    ergitDirectory.mkdir()

    new File(ergitDirectory, "repos").createNewFile()
  }

  /**
    * check recursively
    *
    * @param directory
    * @return true if the directory is under ErGit
    */
  @tailrec
  def isUnderErGit(directory: File): Boolean = {
    directory.listFiles() find (p => p.getName == ".ergit") match {
      case None => directory.getParentFile match {
        case null => false
        case _ => isUnderErGit(directory.getParentFile)
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
    FileUtils.readLines(reposFile, StandardCharsets.UTF_8).contains(name) match {
      case true => throw new ErGitManageException("%s already exists.".format(name))
      case _ => FileUtils.writeStringToFile(reposFile, "%s\n".format(name), StandardCharsets.UTF_8, true)
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
    ergitRoot.listFiles() find (p => p.getName == "repos") match {
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
    directory.listFiles() find (p => p.getName == ".ergit") match {
      case None => directory.getParentFile match {
        case null => throw new ErGitNotInitializedException()
        case _ => getErGitRoot(directory.getParentFile)
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