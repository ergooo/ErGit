package jp.ergo.ergit.ergit

import java.io.File

import jp.ergo.ergit.exception.{NoSuchBranchException, RemoteRepositoryNotFoundException}
import jp.ergo.ergit.ErGit
import jp.ergo.ergit.utils.GitHelper
import org.scalatest._

import scala.sys.process.Process


class ErGit$Test extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  private val path = new File("./src/test/resources")

  // BeforeAndAfterトレイト
  before {
    // git init
    GitHelper.create(path, Seq("hoge", "mage", "piyo"))

  }

  after {
    // remove .git
    GitHelper.destroy(path)
  }

  // BeforeAndAfterAllトレイト
  override def beforeAll(): Unit = {
  }

  override def afterAll(): Unit = {
  }


  "checkout" should "checkout hoge branch" in {

    ErGit.checkout(path.getAbsolutePath, "hoge")
    val branch = ErGit.getCurrentBranch(path.toString)
    branch should be("hoge")
  }

  "checkout" should "throw BranchNotFoundException if the branch does not exist" in {
    a[NoSuchBranchException] should be thrownBy ErGit.checkout(path.getAbsolutePath, "huga")
  }

  "checkoutb" should "checkout fuga branch" in {

    ErGit.checkoutb(path.getAbsolutePath, "huga")
    val branch = ErGit.getCurrentBranch(path.toString)
    branch should be("huga")

  }

  "checkoutb" should "checkout hoge branch" in {

    ErGit.checkoutb(path.getAbsolutePath, "hoge")
    val branch = ErGit.getCurrentBranch(path.toString)
    branch should be("master")

  }

  "hasRemote" should "return false if there is no remote repositories" in {
    ErGit.hasRemote(path.getAbsolutePath) should be(false)
  }

  "fetch" should "throws RemoteRepositoryNotFoundException if there is no remote repositories" in {
    a[RemoteRepositoryNotFoundException] should be thrownBy ErGit.fetch(path.toString)
  }

  "isUnderGit" should "return true if the directory is under git" in {
    ErGit.isUnderGit(path.getAbsolutePath) should be(true)
  }

  "isUnderGit" should "return false if the directory is not under git" in {
    Process(Seq("rm", "-rf", ".git/"), path) !!

    ErGit.isUnderGit(path.getAbsolutePath) should be(false)
  }

  "getStatus" should "return the status as String" in {
    val status = ErGit.getStatus(path.getAbsolutePath)
    status should be("On branch master\nnothing to commit, working directory clean\n")
  }
}
