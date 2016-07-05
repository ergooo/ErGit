package jp.ergo.ergit.repository


import better.files.File
import jp.ergo.ergit.utils.GitHelper
import org.scalatest._

import scala.sys.process.Process

class RepositoryTest extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  private val path = File("./src/test/resources")

  // BeforeAndAfterトレイト
  before {
    // git init
    GitHelper.create(path.toJava, Seq("hoge", "mage", "piyo"))

  }

  after {
    // remove .git
    GitHelper.destroy(path.toJava)
  }


  // BeforeAndAfterAllトレイト
  override def beforeAll(): Unit = {
  }

  override def afterAll(): Unit = {
  }

  "Repository" should "create the instance from a File" in {
    val sut = Repository(path)
    sut should not be null
    sut.path should be(path.pathAsString)
  }

  "Repository" should "throw an RepositoryNotFoundException if the directory is not under git " in {
    Process(Seq("rm", "-rf", ".git/"), path.toJava) !!

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

  "getStatus" should "return the status as String" in {
    val sut = Repository(path)
    val status = sut.getStatus
    status should be("On branch master\nnothing to commit, working directory clean\n")
  }

}
