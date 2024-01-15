package snorochevskiy.lm.crawler

import snorochevskiy.lm.crawler.http.SiradmaCrawler
import zio.http.{Client, ClientSSLConfig, DnsResolver, ZClient}
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.netty.NettyConfig
import zio.http.netty.client.NettyClientDriver

object CrawlerApp extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =

    val sslConfig = ClientSSLConfig.FromTrustStoreResource(
      trustStorePath = "truststore.jks",
      trustStorePassword = "changeit",
    )

    val clientConfig = ZClient.Config.default //.ssl(sslConfig)

    val program = for {
      siradmaCrawler <- ZIO.service[SiradmaCrawler]
      text <- siradmaCrawler.fetchJobs() //fetchJobCategories()
      _ <- zio.Console.printLine(text)
    } yield ()
    program.provide(ZLayer.succeed(clientConfig),
      Client.customized,
      NettyClientDriver.live,
      DnsResolver.default,
      ZLayer.succeed(NettyConfig.default),
      Scope.default,
      SiradmaCrawler.layer,
    )
}
