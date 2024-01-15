package snorochevskiy.lm.util


import java.sql.ResultSet

//class ResultSetIterator(rs: ResultSet) extends Iterator[ResultSet] {
//
//}

object ResultSetMapper {

  def as[A <: Product](rs: ResultSet, a: A): A = {

    rs.getMetaData.getColumnCount
    ???
  }
}
