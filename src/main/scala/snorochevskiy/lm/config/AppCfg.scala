package snorochevskiy.lm.config

import com.typesafe.config.ConfigFactory
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.typesafe.*
import zio.config.typesafe.TypesafeConfigProvider.fromTypesafeConfig

case class DbCfg(
  connectionString: String,
  login: String,
  password: String,
)

case class AppCfg(
  db: DbCfg
)

object AppCfg {
  def layer(profile: String): ZLayer[Any, Exception, AppCfg] = ZLayer {
    for {
      profiled  <- ZIO.attemptBlockingIO(ConfigFactory.load(s"application-${profile}.conf"))
      base      <- ZIO.attemptBlockingIO(ConfigFactory.load(s"application.conf"))
      resulting <- ZIO.attemptBlockingIO(profiled.withFallback(base).resolve())
      appCfg    <- fromTypesafeConfig(resulting).load(deriveConfig[AppCfg])
    } yield appCfg
  }
}

