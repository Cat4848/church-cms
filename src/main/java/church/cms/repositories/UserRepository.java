package church.cms.repositories;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserRepository implements Repository<User> {
  private final DataSource dataSource;
  private final Logger logger;

  public UserRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }

  @Override
  public User save(User user) throws SQLException, InvalidEntityException {
    logger.info("save user: start: userId: {}", user.getUserId());

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
    logger.debug("save user: with SQL query: {}", sql);

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

    logger.info("save user: end");

    return user;
  }

  @Override
  public User retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve user: start: userId: {}", id);

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

          logger.info("retrieve user: end");

          return new User(userId, firstName, lastName, email, password, isAdmin, isRetired);
        }
      }
    }
    throw new InvalidEntityException("User with ID " + id + " not found");
  }

  public User retrieveByEmail(String email) throws SQLException, InvalidEntityException {
    logger.info("retrieveByEmail user: start: userEmail {}", email);

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

          logger.info("retrieveByEmail user: end");

          return new User(userId, firstName, lastName, uEmail, password, isAdmin, isRetired);
        }
      }
    }
    throw new InvalidEntityException("User with email " + email + "not found");
  }

  @Override
  public void delete(Integer id) throws SQLException, InvalidEntityException {
    logger.info("delete user: start: userId: {}", id);

    User user = retrieve(id);
    user.setIsRetired(true);
    save(user);

    logger.info("delete user: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists user: start: userId: {}", id);

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

    logger.info("exists user: end");

    return checkedId != null;
  }

  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists user: start");

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
    logger.info("createTableIfNotExists user: end");
  }
}