name := "mongoDBAssignments"

version := "1.0"

scalaVersion := "2.11.8"

parallelExecution in Test := false

// you may also want to add the typesafe repository
resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.11.11",
  "org.slf4j" % "slf4j-log4j12" % "1.7.21",
  "org.scalatest" % "scalatest_2.11" % "3.0.0-M15",
  "org.scala-lang.modules" % "scala-async_2.10" % "0.9.2"
)