package church.cms.db.mysql;

import church.cms.db.SqlDatabase;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MySqlDatabase implements SqlDatabase {
  private final DataSource dataSource;
  private Connection connection;

  /**
   * The MySQL database connection with handy utility methods.
   */
  public MySqlDatabase(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Executes a {@code SELECT} SQL query with parameters.
   * The {@code executeQuery()} method on the {@code PreparedStatement}
   * only executes {@code SELECT} statements that return a {@code ResultSet}.
   * To execute an {@code INSERT}, {@code UPDATE} or {@code DELETE} SQL statement,
   * please use the {@code executeUpdate()} method provided by the {@code SqlDatabase} interface.
   *
   * @param sql String The SQL query string.
   * @return {@code ResultSet} The iterator from the database.
   */
  public ResultSet executeQuery(String sql, List<Object> params) throws SQLException {
    PreparedStatement statement = prepareStatement(sql, params);
    return statement.executeQuery();
  }

  /**
   * Executes an {@code INSERT}, {@code UPDATE} or {@code DELETE} SQL statement with parameters.
   * The {@code executeUpdate()} method on the {@code PreparedStatement}
   * only executes DSL (Data Manipulation Statements) like {@code INSERT}, {@code UPDATE} or {@code DELETE}
   * which do not return a {@code ResultSet}.
   * To execute a {@code SELECT} SQL statement,
   * please use the {@code executeSelect()} method provided by the {@code SqlDatabase} interface.
   *
   * @param sql String The SQL query string.
   * @return {@code ResultSet} The iterator from the database that contains the generated IDs.
   *
   */
  public ResultSet executeUpdate(String sql, List<Object> params) throws SQLException {
    try(Connection conn = dataSource.getConnection();
    PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
      int paramsSize = params.size();
      for (int i = 0; i < paramsSize; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      return stm.getGeneratedKeys();
    }
  }

  private PreparedStatement prepareStatement(String sql, List<Object> params) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int paramsSize = params.size();
      for (int i = 0; i < paramsSize; i++) {
        statement.setObject(i + 1, params.get(i));
      }
      return statement;
    }
  }

  public void closeConnection() throws SQLException {
    if (connection != null) connection.close();
  }
}