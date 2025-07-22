ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "imdb-api-integration",
    version := "0.1.0",
    organization := "test.imdb",
    libraryDependencies ++= Dependencies.all
  )
