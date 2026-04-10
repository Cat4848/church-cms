package db;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlDatabase {
  ResultSet execute(String sql, Object[] params) throws SQLException, NamingException;
}