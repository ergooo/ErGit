name := "ErGit"

version := "1.0"

scalaVersion := "2.11.8"

parallelExecution in Test := false

lazy val client = (project in file("modules/client"))
  .dependsOn(core)

lazy val core = project in file("modules/core")
