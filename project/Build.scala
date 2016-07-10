import java.io.File
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.assemblySettings
import sbtassembly.AssemblyKeys
import sbtassembly.AssemblyPlugin.autoImport._

object Build extends Build {
  val packageErGit = taskKey[Unit]("package ErGit.")

  // Project settings
  lazy val root = (project in file(".")).
    settings(
      packageErGit := {},
      packageErGit <<= packageErGit.dependsOn(assembly map {(file: File) =>
        println("run packageErGit")

        println(file.name)
      })
    )
}