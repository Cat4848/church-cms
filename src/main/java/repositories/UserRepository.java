package repositories;

import db.SqlDatabase;
import domain.User;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements Repository<User> {
  SqlDatabase db;

  public UserRepository(SqlDatabase db) {
    this.db = db;
  }

  @Override
  public void save(User user) {
    if (user.getUserId() == null) {
      // INSERT
    } else {
      // UPDATE
    }
  }

  @Override
  public User retrieve(String id) {
    return null;
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public boolean exists(Long id) throws SQLException, NamingException {
    String sql = "SELECT user_id FROM users WHERE user_id = ?;";
    Long[] params = new Long[]{id};
    try {
      ResultSet resultSet = db.execute(sql, params);
      if (resultSet.next()) {
        resultSet.getLong("user_id");
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      throw new SQLException(e.getMessage());
    } catch (NamingException e) {
      throw new NamingException(e.getMessage());
    }
  }
}