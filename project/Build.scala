import java.io.File

import sbt._
import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.assemblySettings
import sbtassembly.AssemblyKeys
import sbtassembly.AssemblyPlugin.autoImport._

object Build extends Build {
  val pathToCommandTemplete = "./project/resources/templete/ergit"
  val outDir = "./out/artifacts/ergit/"
  val commandFileName = "ergit"

  val packageErGit = taskKey[Unit]("package ErGit.")

  // Project settings
  lazy val root = (project in file(".")).
    settings(
      packageErGit := {}
      , packageErGit <<= packageErGit.dependsOn(assembly map { (file: File) =>
        println("run packageErGit")
        packaging(file)
      })
    )

  def packaging(jarFile: File): Unit = {
    val commandText = createCommandText(jarFile.name)
    val outCommandFile = new File(outDir, commandFileName)
    IO.write(outCommandFile, commandText, IO.utf8, append = false)
  }

  def createCommandText(jarName: String): String = {
    IO.read(new File(pathToCommandTemplete), IO.utf8).replace("__JAR__", jarName)
  }
}