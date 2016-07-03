package jp.ergo.ergit.manage

import java.io.{File => JFile}

import better.files._
import jp.ergo.ergit.manage.exception.ErGitManageException
import org.scalatest._


class ErGitManager$Test extends FlatSpec with Matchers with BeforeAndAfter {
  private val root = File("./src/test/resources")
  private val ergitFile = root / ".ergit"

  behavior of "ErGitManager$Test"

  before {
    if (ergitFile.exists) ergitFile.delete()
  }

  after {
    if (ergitFile.exists) ergitFile.delete()
  }
  "init" should "init" in {
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
    ErGitManager.addRepository(root, "repository1")
    ErGitManager.addRepository(root, "repository2")

    val lines = root / ".ergit" / "repos" lines
    val array = lines.toArray
    array(0) should be("repository1")
    array(1) should be("repository2")
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


}
