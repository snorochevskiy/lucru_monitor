package snorochevskiy.lm.persist

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor

import java.io.File
import javax.sql.DataSource

object LiquibaseInit {
  def initLiquibase(ds: DataSource) = {
    val connection = ds.getConnection
    val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))
    val liquibase = new Liquibase("dbchangelog.xml", new DirectoryResourceAccessor(new File("/home/stas/dev/proj/lucru_monitor/db/struct/")), database)
    liquibase.update()
  }
}
