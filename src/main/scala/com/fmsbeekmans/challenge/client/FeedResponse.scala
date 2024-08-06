package com.fmsbeekmans.challenge.client

import io.circe.{ Decoder, Json, KeyDecoder }
import io.circe.generic.semiauto

import java.time.LocalDate
import scala.util.Try

case class FeedResponse(
  near_earth_objects: Map[LocalDate, List[Json]]
)

object FeedResponse:
  given KeyDecoder[LocalDate] =
    KeyDecoder.instance { key => Try(LocalDate.parse(key)).toOption }

  given Decoder[FeedResponse] = semiauto.deriveDecoder
