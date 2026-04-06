package repositories;

import db.SqlDatabase;
import db.mysql.MySqlDatabase;
import domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.naming.NamingException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@DisplayName("Test suite for the UserRepository")
public class UserRepositoryTest {
  SqlDatabase db;
  String sql;
  Long[] params;
  Repository<User> userRepository;

  public UserRepositoryTest() {
    this.db = mock(MySqlDatabase.class);
    this.sql = "SELECT user_id FROM users WHERE user_id = ?;";
    this.params = new Long[]{2L};
    this.userRepository = new UserRepository(db);
  }

  @Test
  void ifReturnsTrueWhenUserExists() throws NamingException, SQLException {
    try {
      ResultSet resultSet = mock(ResultSet.class);
      when(resultSet.next()).thenReturn(true, false);
      when(resultSet.getLong("user_id")).thenReturn(params[0]);
      when(db.execute(sql, params)).thenReturn(resultSet);

      boolean isUser = userRepository.exists(params[0]);
      assertTrue(isUser);

      verify(db).execute(sql, params);
    } catch (NamingException e) {
      throw new NamingException(e.getMessage());
    } catch (SQLException e) {
      throw new SQLException(e.getMessage());
    }
  }

  @Test
  void ifItReturnsFalseWhenUserDoesNotExist() throws SQLException, NamingException {
    try {
      ResultSet resultSet = mock(ResultSet.class);
      when(resultSet.next()).thenReturn(false);
      when(db.execute(sql, params)).thenReturn(resultSet);

      boolean isUser = userRepository.exists(params[0]);
      assertFalse(isUser);

      verify(db).execute(sql, params);
    } catch (SQLException e) {
      throw new SQLException(e.getMessage());
    } catch (NamingException e) {
      throw new NamingException(e.getMessage());
    }
  }
}