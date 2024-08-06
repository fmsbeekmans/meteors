package com.fmsbeekmans.challenge.client

import cats.effect.*
import cats.syntax.applicativeError.*
import cats.syntax.monadError.*
import cats.syntax.order.*
import com.fmsbeekmans.challenge.config.NearEarthObjectClientConfig
import com.fmsbeekmans.challenge.model.DateRangeInclusive
import fs2.{io as _, *}
import io.circe.*
import org.http4s.Method.GET
import org.http4s.client.Client
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.{QueryParamEncoder, Request}
import org.typelevel.log4cats.Logger

import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class NearEarthObjectClient(
  config: NearEarthObjectClientConfig,
  client: Client[IO],
  logger: Logger[IO]
):
  given QueryParamEncoder[LocalDate] =
    QueryParamEncoder.localDate(DateTimeFormatter.ISO_LOCAL_DATE)

  def list: IO[Json] = {
    val request =
      Request[IO](
        GET,
        (config.baseUri / "neo" / "browse")
          .withQueryParam("api_key", config.apiKey.value)
      )

    client
      .expect[BrowseResponse](request)
      .map(_.near_earth_objects)
      .map(Json.arr(_*))
      .onError(e => logger.error(e)(e.getMessage))
  }

  def get(id: String): IO[Option[Json]] = {
    val request =
      Request[IO](
        GET,
        (config.baseUri / "neo" / id)
          .withQueryParam("api_key", config.apiKey.value)
      )

    client
      .expectOption[Json](request)
      .onError(e => logger.error(e)(e.getMessage))
  }


  def range(dateRange: DateRangeInclusive): Stream[IO, Json] =
    dateRange
      .chunk
      .evalMap { chunk =>
        val request =
          Request[IO](
            GET,
            (config.baseUri / "feed")
              .withQueryParam("start_date", chunk.from)
              .withQueryParam("end_date", chunk.to)
              .withQueryParam("api_key", config.apiKey.value)
          )

        client
          .expect[FeedResponse](request)
          .onError(e => logger.error(e)(e.getMessage))
      }
      .flatMap(body =>
        Stream.emits(body.near_earth_objects.values.toList.flatten)
      )
