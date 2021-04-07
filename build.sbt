name := "taskmanager-scala"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq("org.scalaz" %% "scalaz-core" % "7.3.3",
  // For logging:
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  // Test depedencies:
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "org.mockito" %% "mockito-scala-cats" % "1.16.37" % "test"
)