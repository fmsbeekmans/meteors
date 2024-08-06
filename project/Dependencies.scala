import sbt.Keys.*
import sbt.{ Def, * }

object Dependencies extends AutoPlugin {
  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        Seq(
          "logback-classic"
        ).map("ch.qos.logback" % _ % "1.5.6"),
        Seq(
          "circe-core",
          "circe-generic",
          "circe-parser",
          "circe-literal"
        ).map("io.circe" %% _ % "0.14.9"),
        Seq(
          "ciris",
          "ciris-enumeratum"
        ).map("is.cir" %% _ % "3.6.0"),
        Seq(
          "http4s-circe",
          "http4s-ember-client",
          "http4s-ember-server",
          "http4s-dsl"
        ).map("org.http4s" %% _ % "0.23.27"),
        Seq(
          "log4cats-core",
          "log4cats-slf4j",
        ).map("org.typelevel" %% _ % "2.7.0")
      ).flatten,
      libraryDependencies ++= Seq(
        Seq(
          "munit-cats-effect"
        ).map("org.typelevel" %% _ % "2.0.0-M4" % Test)
      ).flatten
    )
}
