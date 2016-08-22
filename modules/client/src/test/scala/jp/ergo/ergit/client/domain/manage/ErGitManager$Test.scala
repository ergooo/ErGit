package jp.ergo.ergit.client.domain.manage

import java.io.{File => JFile, InputStream}
import java.util.Properties

import better.files._
import jp.ergo.ergit.client.domain.manage.exception.ErGitManageException
import jp.ergo.ergit.core.domain.Repository
import jp.ergo.ergit.core.utils.GitHelper
import org.scalatest._
import jp.ergo.ergit.client.infrastructure.utils.Using.using

class ErGitManager$Test extends FlatSpec with Matchers with BeforeAndAfter {
  private val root = File("./src/test/resources")
  private val ergitFile = root / ".ergit"

  private val pathToRepository1 = root / "repository1"
  private val pathToRepository2 = root / "repository2"
  private val pathToRepository3 = root / "repository3"

  behavior of "ErGitManager$Test"


  // BeforeAndAfterトレイト
  before {
    if (ergitFile.exists) ergitFile.delete()

    if (pathToRepository1.exists) pathToRepository1.delete()
    if (pathToRepository2.exists) pathToRepository2.delete()
    if (pathToRepository3.exists) pathToRepository3.delete()

    pathToRepository1.createIfNotExists(asDirectory = true)
    pathToRepository2.createIfNotExists(asDirectory = true)
    pathToRepository3.createIfNotExists(asDirectory = true)

    GitHelper.create(pathToRepository1.toJava, Seq("branch1"))
    GitHelper.create(pathToRepository2.toJava, Seq("branch1"))
    GitHelper.create(pathToRepository3.toJava, Seq("branch1"))
  }

  after {
    if (ergitFile.exists) ergitFile.delete()

    if (pathToRepository1.exists) pathToRepository1.delete()
    if (pathToRepository2.exists) pathToRepository2.delete()
    if (pathToRepository3.exists) pathToRepository3.delete()
  }

  "init" should "create .egit directory and repos file" in {
    ErGitManager.init(root)
    root / ".ergit" exists() should be(true)
    root / ".ergit" / ErGitManager.repoFileName exists() should be(true)
  }

  "init" should "throw ErGitManageException" in {
    ErGitManager.init(root)
    a[ErGitManageException] should be thrownBy ErGitManager.init(root)
  }

  "addRepository" should "add repository" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))

    val repoFile = root / ".ergit" / ErGitManager.repoFileName
    using[Unit, InputStream](repoFile.newInputStream) { i =>
      val p = new Properties()
      p.load(i)
      p.getProperty("repository1") should be(pathToRepository1.pathAsString)
      p.getProperty("repository2") should be(pathToRepository2.pathAsString)
    }
  }


  "addRepository" should "throws ErGitManageException" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    a[ErGitManageException] should be thrownBy ErGitManager.addRepository(root, Repository(pathToRepository1))

  }

  "removeRepository" should "remove the repository" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))

    ErGitManager.removeRepository(root, "repository1")
    val repoFile = root / ".ergit" / ErGitManager.repoFileName
    using[Unit, InputStream](repoFile.newInputStream) { i =>
      val p = new Properties()
      p.load(i)
      p.getProperty("repository1") should be(null)
      p.getProperty("repository2") should be(pathToRepository2.pathAsString)
    }
  }


  "removeRepository" should "do nothing if no repository to remove found" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))

    val repoFile = root / ".ergit" / ErGitManager.repoFileName
    using[Unit, InputStream](repoFile.newInputStream) { i =>
      val p = new Properties()
      p.load(i)
      p.getProperty("repository1") should be(pathToRepository1.pathAsString)
      p.getProperty("repository2") should be(pathToRepository2.pathAsString)
    }

    // the repository3 to remove has not been added. then do nothing.
    ErGitManager.removeRepository(root, "repository3")

    using[Unit, InputStream](repoFile.newInputStream) { i =>
      val p = new Properties()
      p.load(i)
      p.getProperty("repository1") should be(pathToRepository1.pathAsString)
      p.getProperty("repository2") should be(pathToRepository2.pathAsString)
    }
  }

  "isUnderErGit" should "return true" in {
    ErGitManager.init(root)
    val child = root / "child"
    child.createDirectory()
    ErGitManager.isUnderErGit(child) should be(true)
    child.delete()
  }

  "isUnderErGit" should "return false" in {
    ErGitManager.isUnderErGit(root) should be(false)
  }

  "getErGitRoot" should "return ErGit root directory" in {
    ErGitManager.init(root)
    val child = root / "child"
    child.createDirectory()

    ErGitManager.getErGitRoot(child).name should be(".ergit")
    child.delete()
  }

  "getErGitRoot" should "throw ErGitManageException" in {
    a[ErGitManageException] should be thrownBy ErGitManager.getErGitRoot(root)
  }

  "getRepositories" should "returns" in {

    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))
    val repositories = ErGitManager.getRepositories(root)

    repositories(0).name should be("repository1")
    repositories(1).name should be("repository2")
  }

}
