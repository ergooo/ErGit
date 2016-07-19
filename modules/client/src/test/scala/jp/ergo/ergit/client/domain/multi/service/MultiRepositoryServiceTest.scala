package jp.ergo.ergit.client.domain.multi.service

import better.files.File
import jp.ergo.ergit.client.domain.ErGitStatus
import jp.ergo.ergit.client.domain.manage.ErGitManager
import jp.ergo.ergit.client.domain.multi.exception.BranchNotExistException
import jp.ergo.ergit.core.domain.{Branch, Repository}
import jp.ergo.ergit.infrastructure.utils.GitHelper
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class MultiRepositoryServiceTest extends FlatSpec with Matchers with BeforeAndAfter {
  private val root = File("./src/test/resources")
  private val ergitFile = root / ".ergit"

  private val pathToRepository1 = root / "repository1"
  private val pathToRepository2 = root / "repository2"
  private val pathToRepository3 = root / "repository3"

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

  behavior of "MultiRepositoryServiceTest"

  "getStatuses" should "return the statuses" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))
    ErGitManager.addRepository(root, Repository(pathToRepository3))
    val repositories = ErGitManager.getRepositories(root)
    val statuses = MultiRepositoryService.getStatuses(repositories)
    statuses.foreach {
      case ErGitStatus("repository1", ｓ) => ｓ should be("On branch master\nnothing to commit, working directory clean\n")
      case ErGitStatus("repository2", ｓ) => ｓ should be("On branch master\nnothing to commit, working directory clean\n")
      case ErGitStatus("repository3", ｓ) => ｓ should be("On branch master\nnothing to commit, working directory clean\n")
      case _ => fail()
    }
  }

  "checkout" should "checkout the branch" in {
    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))
    ErGitManager.addRepository(root, Repository(pathToRepository3))
    val repositories = ErGitManager.getRepositories(root)
    repositories.foreach(p =>
      p.getCurrentBranch.name should be("master")
    )

    MultiRepositoryService.checkout(repositories, Branch("branch1"))
    repositories.foreach(p =>
      p.getCurrentBranch.name should be("branch1")
    )
  }


  "checkout" should "throw BranchNotExistException" in {
    val pathToRepository4 = root / "repository4"
    if (pathToRepository4.exists) pathToRepository4.delete()
    pathToRepository4.createIfNotExists(asDirectory = true)
    // repository4 does not have the "branch1".
    GitHelper.create(pathToRepository4.toJava, Seq())

    ErGitManager.init(root)
    ErGitManager.addRepository(root, Repository(pathToRepository1))
    ErGitManager.addRepository(root, Repository(pathToRepository2))
    ErGitManager.addRepository(root, Repository(pathToRepository3))
    ErGitManager.addRepository(root, Repository(pathToRepository4))
    val repositories = ErGitManager.getRepositories(root)
    repositories.foreach(p =>
      p.getCurrentBranch.name should be("master")
    )

    a[BranchNotExistException] should be thrownBy MultiRepositoryService.checkout(repositories, Branch("branch1"))

    if (pathToRepository4.exists) pathToRepository4.delete()

  }
}
