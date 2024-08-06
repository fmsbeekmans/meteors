package com.fmsbeekmans.challenge.api

import cats.effect.IO
import munit.CatsEffectSuite
import fs2.Stream
import io.circe.Json
import org.http4s.circe.CirceEntityDecoder.*

class ApiTest extends CatsEffectSuite {
  formatTest("can format empty array", Stream.empty)
  formatTest("can format singleton array", Stream.emit("1"))
  formatTest("can format array with more elements", Stream("1", "2")
  )

  def formatTest(label: String, items: Stream[IO, String]): Unit = {
    test(label) {
      val result =
        for {
          response <- Api.formatAsJsonArray(items)
          body <- response.as[Json]
        } yield ()

      assertIO_(result)
    }
  }

}
