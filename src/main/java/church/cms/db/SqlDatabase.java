package church.cms.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface SqlDatabase {
  ResultSet executeQuery(String sql, List<Object> params) throws SQLException;

  ResultSet executeUpdate(String sql, List<Object> params) throws SQLException;

  void closeConnection() throws SQLException;
}