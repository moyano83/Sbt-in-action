name := "preowned-kittens"

organization := "com.preowned.kittens"

version := "1.0"

libraryDependencies ++= c(
  "org.specs2" % "specs2_2.10" % "1.14" % "test",
  "junit" % "junit" % "4.12" % "test"
)

val gitCommit = taskKey[String]("Definition to store a hashed string")
gitCommit := Process("cd .. && git rev-parse HEAD").lines.head

lazy val common = Project("common", file("common")).settings(libraryDependencies++=(
  "org.specs2" % "specs2_2.10" % "1.14" % "test",
  "junit" % "junit" % "4.12" % "test"
))