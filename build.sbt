name := "taskmanager-scala"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.3.3",
  // For logging:
  "org.slf4j" % "slf4j-api" % "1.7.30" % Compile,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.13.3" % Runtime,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  // Test dependencies:
  "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  "org.mockito" %% "mockito-scala-cats" % "1.16.37" % Test,
  "org.scalacheck" %% "scalacheck" % "1.15.3" % Test, // for property based testing
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.7.0" % Test, // to integrate scala-check with scala-test
)

scalacOptions ++= Seq("-deprecation")