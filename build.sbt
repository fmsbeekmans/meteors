val scala3Version = "3.4.2"

val projectVersion = "0.1.0"

lazy val core = project
  .in(file("."))
  .settings(
    name := "Meteor challenge",
    version := projectVersion,
    scalaVersion := scala3Version
  )
  .enablePlugins(Dependencies)

