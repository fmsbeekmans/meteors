package com.fmsbeekmans.challenge.config

import cats.syntax.either.*
import ciris.*
import org.http4s.Uri

case class NearEarthObjectClientConfig(
  baseUri: Uri,
  apiKey: Secret[String]
)

object NearEarthObjectClientConfig:
  given ConfigDecoder[String, Uri] =
    ConfigDecoder[String]
      .mapEither { case (_, uri) =>
        Uri
          .fromString(uri)
          .leftMap { error => ConfigError(error.message) }
      }

  val config =
    for {
      baseUri <-
        env("NEAR_EARTH_OBJECTS_BASE_URI")
          .default("https://api.nasa.gov/neo/rest/v1")
          .as[Uri]
      apiKey <-
        env("NEAR_EARTH_OBJECTS_API_KEY")
          .default("DEMO_KEY")
    } yield NearEarthObjectClientConfig(
      baseUri,
      Secret(apiKey)
    )
