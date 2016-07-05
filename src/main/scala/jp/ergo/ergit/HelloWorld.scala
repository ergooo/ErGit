package jp.ergo.ergit

import better.files.File
import jp.ergo.ergit.repository.Repository


object HelloWorld {
  def main(args: Array[String]): Unit = {

    val rootDirectory = File("/Users/wataru_ishigaya/develop/scala/sample_repository")
    val repository = Repository(rootDirectory)
    val a = repository.getCurrentBranch
    println(a)
  }
}
