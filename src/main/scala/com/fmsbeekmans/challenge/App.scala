package com.fmsbeekmans.challenge

import cats.effect.*
import com.fmsbeekmans.challenge.api.Api
import com.fmsbeekmans.challenge.client.NearEarthObjectClient
import com.fmsbeekmans.challenge.config.Config
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.slf4j.*

case class App(
  config: Config,
  nearEarthObjectClient: NearEarthObjectClient,
  api: Api
):
  val run: Resource[IO, Server] = api.run

object App extends IOApp:

  def fromConfig(config: Config): Resource[IO, App] = {

    for {
      httpClient <-
        EmberClientBuilder
          .default[IO]
          .build
      logger <- Resource.eval(Slf4jFactory.create[IO])
      nearEarthObjectClient =
        NearEarthObjectClient(
          config.nearEarthObjectClientConfig,
          httpClient,
          logger
        )
    } yield App(
      config,
      nearEarthObjectClient,
      Api(config.apiConfig, nearEarthObjectClient)
    )
  }

  override def run(args: List[String]): IO[ExitCode] =
    Resource
      .eval(Config.config.load)
      .flatMap(fromConfig)
      .flatMap(_.run)
      .use(_ => IO.never)
      .as(ExitCode.Error)
