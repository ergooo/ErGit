package jp.ergo.ergit.repository

import java.io.File

import org.scalatest._

import scala.sys.process.Process

class RepositoryTest extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

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

  "Repository" should "create the instance from a File" in {
    val sut = Repository(path)
    sut should not be null
    sut.path should be(path.getAbsolutePath)
  }

  "Repository" should "throw an RepositoryNotFoundException if the directory is not under git " in {
    Process(Seq("rm", "-rf", ".git/"), path) !!

    an[RepositoryNotFoundException] should be thrownBy Repository(path)
  }

  "existsInLocal" should "return true is the branch exists" in {
    val sut = Repository(path)
    sut.existsInLocal(Branch("hoge")) should be(true)
    sut.existsInLocal(Branch("mage")) should be(true)
    sut.existsInLocal(Branch("piyo")) should be(true)
  }

  "existsInLocal" should "return false is the branch does not exist" in {
    val sut = Repository(path)
    sut.existsInLocal(Branch("huga")) should be(false)
  }

  "checkout" should "checkout the specified branch" in {
    val sut = Repository(path)
    sut.checkout(Branch("master"))
    sut.getCurrentBranch.name should be("master")

  }

}
