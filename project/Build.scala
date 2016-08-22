import java.io.File

import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._

object Build extends Build {
  val pathToCommandTemplete = "./project/resources/templete/ergit"
  val artifactDirectory = "./out/artifacts/"
  val commandFileName = "ergit"

  val packagingDirectoryName: Def.Initialize[String] = Def.setting {
    "%s-%s".format(Keys.name.value, Keys.version.value)
  }

  val packageErGit = taskKey[Unit]("package ErGit.")

  // Project settings
  lazy val root = (project in file("modules/client")).
    settings(
      packageErGit := {
        val jarFile = assembly.value

        val pathToPackagingDirectory = artifactDirectory + packagingDirectoryName.value
        val commandText = IO.read(new File(pathToCommandTemplete), IO.utf8).replace("__JAR__", jarFile.name)
        val outCommandFile = new File(pathToPackagingDirectory, commandFileName)
        outCommandFile.setExecutable(true)
        IO.write(outCommandFile, commandText, IO.utf8, append = false)

        IO.copyFile(jarFile, new File(pathToPackagingDirectory, jarFile.name))
      }
    )
    .dependsOn(core % "compile->compile;test->test")

  lazy val core = project in file("modules/core")

  mainClass in assembly := Some("jp.ergo.ergit.client.app.ErGitClient")

}