package db.mysql;

import db.SqlDatabase;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlDatabase implements SqlDatabase {
  final String dbContext;

  /**
   * The MySQL database connection with handy utility methods.
   * */
  public MySqlDatabase(String dbContext) {
    this.dbContext = dbContext;
  }

  /**
   * Executes a simple SQL query with no parameters.
   * @param sql String The SQL query string.
   * @return ResultSet The ResultSet iterator from the database.
   * */
  public ResultSet execute(String sql, Object[] params) throws NamingException, SQLException {
    Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement(sql);

    int paramsSize = params.length;
    for (int i = 0; i < paramsSize; i++) {
      statement.setObject(i, params[i]);
    }

    ResultSet result = statement.executeQuery();

    connection.close();
    statement.close();

    return result;
  }

  private Connection getConnection() throws NamingException, SQLException {
    InitialContext context = new InitialContext();
    DataSource dataSource = (DataSource) context.lookup(dbContext);
    return dataSource.getConnection();
  }
}