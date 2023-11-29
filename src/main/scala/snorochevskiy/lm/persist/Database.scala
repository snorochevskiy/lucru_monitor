package snorochevskiy.lm.persist

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import snorochevskiy.lm.config.{AppCfg, DbCfg}
import zio.*

import java.io.IOException


case class DBConfig(connectionString: String)

trait Database {
  import java.sql.*
  import javax.sql.*
  val ds: DataSource

  private def acquireConnection(): IO[IOException, Connection] =
    ZIO.attemptBlockingIO(ds.getConnection)

  private def releaseConnection(conn: => Connection): UIO[Unit] =
    ZIO.succeedBlocking(conn.close())

  def connectionScope(): ZIO[Scope, IOException, Connection] =
    ZIO.acquireRelease(acquireConnection())(releaseConnection(_))
}

case class DatabaseLive(ds: HikariDataSource) extends Database

object DatabaseLive {
  val layer: ZLayer[AppCfg, Nothing, DatabaseLive] =
    ZLayer {
      ZIO.service[AppCfg].map(appCfg => DatabaseLive(getConnectionPool(appCfg.db)))
    }

  def getConnectionPool(dbCfg: DbCfg): HikariDataSource =
    val DbCfg(connectionString, login, password) = dbCfg
    val config = new HikariConfig()
    config.setJdbcUrl(connectionString)
    config.setUsername(login)
    config.setPassword(password)
    new HikariDataSource(config)

}
