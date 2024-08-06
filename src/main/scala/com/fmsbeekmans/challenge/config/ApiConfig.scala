package com.fmsbeekmans.challenge.config

import cats.effect.IO
import ciris.*
import com.comcast.ip4s.*

case class ApiConfig(
  port: Port,
  host: Ipv4Address
)

object ApiConfig:

  given ConfigDecoder[String, Port] =
    ConfigDecoder[String]
      .mapOption[Port]("Port")(Port.fromString)

  given ConfigDecoder[String, Ipv4Address] =
    ConfigDecoder[String]
      .mapOption[Ipv4Address]("IPv4")(Ipv4Address.fromString)

  val config: ConfigValue[IO, ApiConfig] =
    for {
      port <- env("PORT").as[Port].default(port"8080")
      host <- env("HOST").as[Ipv4Address].default(ipv4"0.0.0.0")
    } yield ApiConfig(
      port,
      host
    )
