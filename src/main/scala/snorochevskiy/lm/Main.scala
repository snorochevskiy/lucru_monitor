package snorochevskiy.lm

import snorochevskiy.lm.config.AppCfg
import snorochevskiy.lm.persist.{Database, DatabaseLive, LiquibaseInit}
import snorochevskiy.lm.service.CompanyRepo
import snorochevskiy.lm.web.LucruRouter
import zio.*
import zio.http.*

import scala.util.Try

object LucruApp extends ZIOAppDefault {

  override val run =
    val program = for {
      db <- ZIO.service[Database]
      _  <- ZIO.fromTry(Try(LiquibaseInit.initLiquibase(db.ds)))
      _  <- Server.serve(LucruRouter.app)
    } yield ()
    program
      .provide(
        zio.Scope.default,
        Server.default,
        AppCfg.layer("snoro"),
        CompanyRepo.layer,
        DatabaseLive.layer
      )
}

