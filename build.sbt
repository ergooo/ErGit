name := "ErGit"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  // ScalaTest
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  // https://mvnrepository.com/artifact/org.apache.commons/commons-io
  "commons-io" % "commons-io" % "2.5",
  "com.github.pathikrit" %% "better-files" % "2.16.0"
)
