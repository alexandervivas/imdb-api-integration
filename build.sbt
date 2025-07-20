ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "imdb-api-integration",
    version := "0.1.0",
    organization := "com.freddy",
    libraryDependencies ++= Seq(
      // Finagle HTTP client/server
      "com.twitter" %% "finagle-http" % "23.11.0",
      // JSON parsing with Circe
      "io.circe" %% "circe-core" % "0.14.6",
      "io.circe" %% "circe-generic" % "0.14.6",
      "io.circe" %% "circe-parser" % "0.14.6",
      // Logging
      "ch.qos.logback" % "logback-classic" % "1.4.14",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      // Caching (optional)
      "com.github.ben-manes.caffeine" % "caffeine" % "3.1.8",
      // Testing
      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    )
  )
