package church.cms.db.mysql;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionSingleton {
  private final static DataSource dataSource;

  static {
    try {
      InitialContext context = new InitialContext();
      dataSource = (DataSource) context.lookup(System.getenv("CHURCH_CMS_DB"));
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
  }

  private MySqlConnectionSingleton() {
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}