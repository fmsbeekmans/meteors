package com.fmsbeekmans.challenge.client

import io.circe.{ Decoder, Json, KeyDecoder }
import io.circe.generic.semiauto

import java.time.LocalDate
import scala.util.Try

case class BrowseResponse(
  near_earth_objects: List[Json]
)

object BrowseResponse:
  given Decoder[BrowseResponse] = semiauto.deriveDecoder
