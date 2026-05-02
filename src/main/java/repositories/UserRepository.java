package repositories;

import domain.User;
import exceptions.InvalidEntityException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {
  DataSource dataSource;

  public UserRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public User save(User user) throws SQLException, InvalidEntityException {
    String sql;
    List<Object> params;

    if (user.getUserId() == null) {
      sql = "INSERT INTO users (first_name, last_name, email, password, is_admin, is_retired) " +
              "VALUES (?,?,?,?,?,?);";
      params = List.of(
              user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getPassword(),
              user.getIsAdmin(),
              user.getIsRetired()
      );
    } else {
      User existingUser = retrieve(user.getUserId());
      sql = "UPDATE users SET " +
              "first_name = ?, " +
              "last_name = ?, " +
              "email = ?, " +
              "password = ?, " +
              "is_admin = ?, " +
              "is_retired = ? " +
              "WHERE user_id = ?;";
      params = List.of(
              user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              existingUser.getPassword(),
              user.getIsAdmin(),
              user.getIsRetired(),
              user.getUserId()
      );
    }

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      // if we used the INSERT branch we will have a created id
      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newUserId = rs.getInt(1);
          user.setUserId(newUserId);
        }
      }
    }
    user.setPassword(null);
    return user;
  }

  @Override
  public User retrieve(Integer id) throws SQLException, InvalidEntityException {
    String sql = "SELECT * FROM users WHERE user_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer userId = rs.getInt("user_id");
          String firstName = rs.getString("first_name");
          String lastName = rs.getString("last_name");
          String email = rs.getString("email");
          String password = rs.getString("password");
          Boolean isAdmin = rs.getBoolean("is_admin");
          Boolean isRetired = rs.getBoolean("is_retired");
          return new User(userId, firstName, lastName, email, password, isAdmin, isRetired);
        }
      }
    }
    throw new InvalidEntityException("User with ID " + id + "not found");
  }

  public User retrieveByEmail(String email) throws SQLException, InvalidEntityException {
    String sql = "SELECT * FROM users WHERE email = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setString(1, email);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer userId = rs.getInt("user_id");
          String firstName = rs.getString("first_name");
          String lastName = rs.getString("last_name");
          String uEmail = rs.getString("email");
          String password = rs.getString("password");
          Boolean isAdmin = rs.getBoolean("is_admin");
          Boolean isRetired = rs.getBoolean("is_retired");
          return new User(userId, firstName, lastName, uEmail, password, isAdmin, isRetired);
        }
      }
    }
    throw new InvalidEntityException("User with email " + email + "not found");
  }

  @Override
  public void delete(Integer id) throws SQLException, InvalidEntityException {
    User user = retrieve(id);
    user.setIsRetired(true);
    save(user);
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    Integer checkedId = null;
    String sql = "SELECT user_id FROM users WHERE user_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("user_id");
        }
      }
    }
    return checkedId != null;
  }

  public void createTableIfNotExists() throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY(user_id)," +
            "first_name VARCHAR(100) NOT NULL," +
            "last_name VARCHAR(100) NOT NULL," +
            "email VARCHAR(200) NOT NULL," +
            "password VARCHAR(255) NOT NULL," +
            "is_admin BOOLEAN NOT NULL," +
            "is_retired BOOLEAN NOT NULL" +
            ");";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
  }
}