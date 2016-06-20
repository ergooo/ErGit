package jp.ergo.scala.checkoutkun

import java.io.File

import jp.ergo.scala.checkoutkun.repository.Repository



object HelloWorld {
  def main(args: Array[String]): Unit = {

    val rootDirectory = new File("/Users/wataru_ishigaya/develop/scala/sample_repository")
    val repository = Repository(rootDirectory)
    val a = repository.getCurrentBranch
    println(a)
  }
}
