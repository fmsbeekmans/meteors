package com.fmsbeekmans.challenge.client

import cats.effect.IO
import io.circe.Decoder
import io.circe.parser.*
import munit.CatsEffectSuite

import scala.io.Source

class ResponseTest extends CatsEffectSuite {

  validFormatTest[BrowseResponse]("neo/browse.json")
  validFormatTest[FeedResponse]("neo/feed.json")

  def validFormatTest[T: Decoder](fileName: String): Unit =
    test(fileName) {
      val result = for {
        content <- IO.delay(
            Source
              .fromResource(fileName)
              .getLines()
              .mkString("")
          )

        decoded <- IO.fromEither(decode[T](content))
      } yield decoded
      
      assertIO_(result.void)
    }
}
