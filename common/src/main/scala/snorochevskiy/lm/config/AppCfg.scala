package snorochevskiy.lm.config

import com.typesafe.config.ConfigFactory
import zio.config.magnolia.deriveConfig
import zio.{ZIO, ZLayer, config}
import zio.config.typesafe.TypesafeConfigProvider.fromTypesafeConfig
import zio.config.{magnolia, typesafe}

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

