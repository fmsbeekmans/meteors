package com.fmsbeekmans.challenge.api

import cats.effect.{ IO, Resource }
import cats.syntax.applicativeError.*
import com.fmsbeekmans.challenge.api.Api.formatAsJsonArray
import com.fmsbeekmans.challenge.client.NearEarthObjectClient
import com.fmsbeekmans.challenge.config.{ ApiConfig, Config }
import com.fmsbeekmans.challenge.model.DateRangeInclusive
import org.http4s.{ HttpRoutes, MediaType, Response }
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import fs2.{ io as _, * }
import org.http4s.headers.`Content-Type`

import java.time.LocalDate
import scala.util.Try

object LocalDateVar {
  def unapply(str: String): Option[LocalDate] = {
    if (!str.isEmpty)
      Try(LocalDate.parse(str)).toOption
    else
      None
  }
}

case class Api(
  config: ApiConfig,
  nearEarthObjectClient: NearEarthObjectClient
) {

  val service =
    HttpRoutes.of[IO] {
      case GET -> Root / "browse" =>
        import org.http4s.circe.CirceEntityEncoder.*

        nearEarthObjectClient
          .list
          .flatMap(Ok(_))
      case GET -> Root / "details" / id =>
        import org.http4s.circe.CirceEntityEncoder.*

        nearEarthObjectClient
          .get(id)
          .flatMap {
            case None => NotFound()
            case Some(details) => Ok(details)
          }
      case GET -> Root / "search" / LocalDateVar(from) / LocalDateVar(to) =>
        val range = DateRangeInclusive.create(from, to)
        val objects =
          nearEarthObjectClient
            .range(range)
            .map(_.noSpacesSortKeys)
            .intersperse(",")

        formatAsJsonArray(objects)
          .map(
            _.withContentType(`Content-Type`(MediaType.application.json))
          )
    }

  val run: Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(service.orNotFound)
      .build
}

object Api:
  def formatAsJsonArray(items: Stream[IO, String]): IO[Response[IO]] =
    Ok(Stream("[") ++ items ++ Stream("]"))
