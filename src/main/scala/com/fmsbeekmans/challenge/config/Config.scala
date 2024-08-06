package com.fmsbeekmans.challenge.config

import cats.effect.IO
import ciris.*
import com.comcast.ip4s.*

case class Config(
  apiConfig: ApiConfig,
  nearEarthObjectClientConfig: NearEarthObjectClientConfig
)

object Config:

  val config: ConfigValue[IO, Config] =
    for {
      apiConfig <- ApiConfig.config
      nearEarthObjectClientConfig <- NearEarthObjectClientConfig.config
    } yield Config(
      apiConfig,
      nearEarthObjectClientConfig
    )
