package jp.ergo.ergit.manage

import java.io.{OutputStream, FileOutputStream, InputStream}
import java.util.Properties

import better.files._
import jp.ergo.ergit.manage.exception.{ErGitManageException, ErGitNotInitializedException, ErGitRepoFileNotFoundException}
import jp.ergo.ergit.repository.Repository

import scala.annotation.tailrec
import scala.collection.JavaConverters._

import jp.ergo.ergit.utils.Using.using
/**
  * manages the config files in .ergit
  */
class ErGitManager {

}

object ErGitManager {

  val repoFileName = "repos.properties"

  /**
    * initialize ergit project.
    * this method attempts to create .ergit directory
    *
    * @param directory the directory to init.
    * @throws ErGitManageException if it has already initialized.
    */
  def init(directory: File): Unit = {
    if (isUnderErGit(directory)) {
      throw new ErGitManageException("already initialized.")
    }
    val ergitDirectory = directory / ".ergit"
    ergitDirectory.createDirectory()
    ergitDirectory.createChild(repoFileName)
  }

  /**
    * check recursively
    *
    * @param directory the directory to check.
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
    * @param directory  the ergit managed directory.
    * @param repository the repository to add.
    * @throws ErGitManageException         if the repository already exists
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  def addRepository(directory: File, repository: Repository) = {
    verifyUnderGit(directory)
    val repoFile = getRepoFile(directory)
    using[Unit, InputStream](repoFile.newInputStream) { i =>
      val p = new Properties()
      p.load(i)
      if (p.containsKey(repository.name)) {
        throw new ErGitManageException("%s already exists.".format(repository.name))
      } else {
        p.setProperty(repository.name, repository.path)
        using[Unit, OutputStream](repoFile.newOutputStream){o =>
          p.store(o, "")
        }
      }
    }
  }


  /**
    * remove the repository form the repo file. Do nothing if the repository cannot be found.
    *
    * @param directory  the ergit managed directory.
    * @param repository the repository to remove.
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  def removeRepository(directory: File, repository: Repository): File = {
    verifyUnderGit(directory)
    val reposFile = getRepoFile(directory)
    val temp = reposFile.parent / "repo.tmp"
    temp.createIfNotExists()
    reposFile.lines filter (p => p != toStoredString(repository)) foreach {
      f => temp.appendLine(f)
    }
    reposFile.overwrite(temp.contentAsString)
    temp.delete()
  }

  def getRepositories(directory: File): Seq[Repository] = {
    val repoFile = getRepoFile(directory)
    val p = new Properties()
    val fileInputStream = repoFile.newInputStream

    using[Seq[Repository], InputStream](fileInputStream) { i =>
      p.load(i)
      p.stringPropertyNames().asScala.map { f =>
        Repository(File(p.getProperty(f)))
      }.toSeq
    }

  }



  /**
    * find repo file recursively.
    *
    * @param directory the ergit managed directory.
    * @throws ErGitRepoFileNotFoundException if no repos file found.
    * @throws ErGitNotInitializedException   if no .ergit found.
    * @return the repo file if it found. Throws ErGitManageException otherwise.
    */
  def getRepoFile(directory: File): File = {
    verifyUnderGit(directory)
    val ergitRoot = getErGitRoot(directory)
    ergitRoot.children.find(p => p.name == repoFileName) match {
      case Some(x) => x
      case _ => throw new ErGitRepoFileNotFoundException("cannot found repos file.")
    }
  }

  /**
    * find the ergit root directory recursively. the returned file's path includes .ergit directory.
    *
    * @param directory the ergit managed directory.
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

  private def toStoredString(repository: Repository): String = {
    "%s=%s".format(repository.name, repository.path)
  }

  /**
    *
    * @param directory the ergit managed directory.
    * @throws ErGitNotInitializedException if no .ergit found.
    */
  private def verifyUnderGit(directory: File): Unit = {
    if (!isUnderErGit(directory)) throw new ErGitNotInitializedException()
  }

}