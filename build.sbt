name := "taskmanager-scala"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq("org.scalaz" %% "scalaz-core" % "7.3.3",
  // For logging:
  "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.13.0" % Runtime,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  // Test dependencies:
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.mockito" %% "mockito-scala-cats" % "1.16.37" % Test,
  "org.scalacheck" %% "scalacheck" % "1.15.3" % Test // for property based testing
)