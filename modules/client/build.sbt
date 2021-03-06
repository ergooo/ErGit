name := "ErGitClient"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  // ScalaTest
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.github.pathikrit" %% "better-files" % "2.16.0",
  "com.github.scopt" %% "scopt" % "3.5.0"
)

parallelExecution in Test := false
