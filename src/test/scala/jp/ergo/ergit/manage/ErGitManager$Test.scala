package jp.ergo.ergit.manage

import java.io.{File => JFile}

import better.files._
import jp.ergo.ergit.manage.exception.ErGitManageException
import jp.ergo.ergit.repository.{Branch, Repository}
import jp.ergo.ergit.utils.GitHelper
import org.scalatest._


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
    root / ".ergit" / "repos" exists() should be(true)
  }

  "init" should "throw ErGitManageException" in {
    ErGitManager.init(root)
    a[ErGitManageException] should be thrownBy ErGitManager.init(root)
  }

  "addRepository" should "add repository" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))

    val lines = root / ".ergit" / "repos" lines
    val array = lines.toSeq
    array(0) should be("""repository1="path/to/repository1"""")
    array(1) should be("""repository2="path/to/repository2"""")
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

    ErGitManager.removeRepository(root, Repository(pathToRepository1))
    val lines = root / ".ergit" / "repos" lines
    val array = lines.toSeq
    array(0) should be("""repository2="path/to/repository2"""")
  }


  "removeRepository" should "do nothing if no repository to remove found" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))
    val linesBefore = root / ".ergit" / "repos" lines
    val arrayBefore = linesBefore.toSeq
    arrayBefore.length should be(2)
    arrayBefore(0) should be("""repository1="path/to/repository1"""")
    arrayBefore(1) should be("""repository2="path/to/repository2"""")

    // the repository3 to remove has not been added. then do nothing.
    ErGitManager.removeRepository(root, Repository(pathToRepository3))

    val linesAfter = root / ".ergit" / "repos" lines
    val arrayAfter = linesAfter.toSeq
    arrayAfter.length should be(2)
    arrayAfter(0) should be("""repository1="path/to/repository1"""")
    arrayAfter(1) should be("""repository2="path/to/repository2"""")
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
