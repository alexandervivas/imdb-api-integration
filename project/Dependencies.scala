import sbt.*

object Versions {

  val enumeratum = "1.9.0"
  val finagle = "24.2.0"
  val circe = "0.14.14"
  val logback = "1.5.18"
  val scalaLogging = "3.9.5"
  val caffeine = "3.2.2"
  val scalatest = "3.2.19"
  val guice = "7.0.0"
}

object Dependencies {

  val finagle: Seq[ModuleID] = Seq(
    "com.twitter" %% "finagle-http" % Versions.finagle
  )

  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % Versions.circe,
    "io.circe" %% "circe-generic" % Versions.circe,
    "io.circe" %% "circe-parser" % Versions.circe
  )

  val logging: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  )

  val caching: Seq[ModuleID] = Seq(
    "com.github.ben-manes.caffeine" % "caffeine" % Versions.caffeine
  )

  val testing: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test
  )

  val enumeratum: Seq[ModuleID] = Seq(
    "com.beachape" %% "enumeratum" % Versions.enumeratum,
    "com.beachape" %% "enumeratum-circe" % Versions.enumeratum
  )

  val guice: Seq[ModuleID] = Seq(
    "com.google.inject" % "guice" % Versions.guice
  )

  val all: Seq[ModuleID] = finagle ++ circe ++ logging ++ caching ++ testing ++ enumeratum ++ guice
}
