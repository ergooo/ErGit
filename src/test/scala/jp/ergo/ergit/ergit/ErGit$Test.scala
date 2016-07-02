package jp.ergo.ergit.ergit

import java.io.File

import jp.ergo.scala.checkoutkun.ergit.exception.{RemoteRepositoryNotFoundException, NoSuchBranchException}
import org.scalatest._

import scala.sys.process.Process


class ErGit$Test extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  private val path = new File("./src/test/resources")

  // BeforeAndAfterトレイト
  before {
    // git init
    Process(Seq("git", "init"), path) !!

    Process(Seq("touch", "Readme.md"), path) !!

    Process(Seq("git", "add", "."), path) !!

    Process(Seq("git", "commit", "-m", "initial commit"), path) !!


    // create 3  branches
    Process(Seq("git", "branch", "hoge"), path) !!

    Process(Seq("git", "branch", "mage"), path) !!

    Process(Seq("git", "branch", "piyo"), path) !!
  }

  after {
    // remove .git
    Process(Seq("rm", "-rf", ".git/"), path) !!

    Process(Seq("rm", "Readme.md"), path) !!
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
    a [NoSuchBranchException] should be thrownBy ErGit.checkout(path.getAbsolutePath, "huga")
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
    a [RemoteRepositoryNotFoundException] should be thrownBy ErGit.fetch(path.toString)
  }

  "isUnderGit" should "return true if the directory is under git" in {
    ErGit.isUnderGit(path.getAbsolutePath) should be(true)
  }

  "isUnderGit" should "return false if the directory is not under git" in {
    Process(Seq("rm", "-rf", ".git/"), path) !!

    ErGit.isUnderGit(path.getAbsolutePath) should be(false)
  }
}
