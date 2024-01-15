package snorochevskiy.lm.repo

import snorochevskiy.lm.persist.{Company, Database}
import zio.*

class CompanyRepo(db: Database) {
  def listCompanies() =
    db.connectionScope().map { (conn: java.sql.Connection) =>
      val statement = conn.createStatement()
      val rs = statement.executeQuery(
        """
          |SELECT id, title, linkedin_id, website
          |FROM companies
          |""".stripMargin)
      var result: List[Company] = Nil
      while (rs.next()) {
        val entity = Company(
          id = Some(rs.getLong("id")),
          name = rs.getString("title"),
          linkedinId = Option(rs.getString("linkedin_id")),
          website = Option(rs.getString("website")),
        )
        result ::= entity
      }
      result
    }
}

object CompanyRepo {
  val layer =
    ZLayer {
      ZIO.service[Database].map(db => new CompanyRepo(db))
    }

  val ListCompaniesSql =
    """
      |SELECT id, title
      |FROM companies, linkedin_id, website
      |""".stripMargin
}
