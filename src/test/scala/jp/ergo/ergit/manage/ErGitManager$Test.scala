package jp.ergo.ergit.manage

import java.io.File
import java.nio.charset.StandardCharsets
import jp.ergo.ergit.manage.exception.ErGitManageException
import org.apache.commons.io.FileUtils
import org.scalatest._

import scala.sys.process.Process


class ErGitManager$Test extends FlatSpec with Matchers with BeforeAndAfter {
  private val path = new File("./src/test/resources")

  behavior of "ErGitManager$Test"
  before {
    FileUtils.deleteDirectory(new File(path, ".ergit"))
  }

  after {
    FileUtils.deleteDirectory(new File(path, ".ergit"))
  }
  "init" should "init" in {
    ErGitManager.init(path)
    new File(path, ".ergit").exists() should be(true)
    new File(path, ".ergit/repos").exists() should be(true)
  }

  "init" should "throw ErGitManageException" in {
    ErGitManager.init(path)
    a[ErGitManageException] should be thrownBy ErGitManager.init(path)
  }

  "addRepository" should "add repository" in {
    ErGitManager.init(path)
    ErGitManager.addRepository(path, "repository1")
    ErGitManager.addRepository(path, "repository2")

    val lines = FileUtils.readLines(new File(path, ".ergit/repos"), StandardCharsets.UTF_8)
    lines.get(0) should be("repository1")
    lines.get(1) should be("repository2")
  }

  "isUnderErGit" should "return true" in {
    ErGitManager.init(path)
    val child = new File(path, "child")
    child.mkdir()
    ErGitManager.isUnderErGit(child) should be(true)
    child.delete()
  }

  "isUnderErGit" should "return false" in {
    ErGitManager.isUnderErGit(path) should be(false)
  }

  "getErGitRoot" should "return ErGit root directory" in {
    ErGitManager.init(path)
    val child = new File(path, "child")
    child.mkdir()

    ErGitManager.getErGitRoot(child).getName should be(".ergit")
    child.delete()
  }


  "getErGitRoot" should "throw ErGitManageException" in {
    a[ErGitManageException] should be thrownBy ErGitManager.getErGitRoot(path)
  }


}
