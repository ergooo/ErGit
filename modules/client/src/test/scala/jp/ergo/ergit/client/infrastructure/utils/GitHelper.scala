package jp.ergo.ergit.client.infrastructure.utils

import java.io.File

import scala.sys.process.Process

object GitHelper {
  def create(path: File, branches: Seq[String]): Unit = {
    // git init
    Process(Seq("git", "init"), path) !!

    Process(Seq("touch", "Readme.md"), path) !!

    Process(Seq("git", "add", "."), path) !!

    Process(Seq("git", "commit", "-m", "initial commit"), path) !!

    // create branches
    branches.foreach(b => Process(Seq("git", "branch", b), path) !!)

  }

  def destroy(path: File): Unit ={
    // remove .git
    Process(Seq("rm", "-rf", ".git/"), path) !!

    Process(Seq("rm", "-f", "Readme.md"), path) !!
  }
}
