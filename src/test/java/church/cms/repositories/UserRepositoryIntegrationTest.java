package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test suite for the UserRepository")
public class UserRepositoryIntegrationTest {
  public static Repository<User> userRepository;
  public static MySQLContainer<?> dbContainer = new MySQLContainer<>("mysql:9.0");

  @BeforeAll
  public static void beforeAll() throws SQLException {
    dbContainer.start();

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dbContainer.getJdbcUrl());
    config.setUsername(dbContainer.getUsername());
    config.setPassword(dbContainer.getPassword());
    DataSource dataSource = new HikariDataSource(config);

    AppDeps appDeps = new AppDeps(dataSource);

    userRepository = appDeps.getUserRepository();
    userRepository.createTableIfNotExists();
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifRetrievesUser() throws NamingException, SQLException, InvalidEntityException {
    User newUser = new User("John", "Long", "j.long@gmail.com", "passwd", false);

    User createdUser = userRepository.save(newUser);
    User retrievedUser = userRepository.retrieve(createdUser.getUserId());

    assertNotNull(retrievedUser.getUserId());
    assertEquals(createdUser.getUserId(), retrievedUser.getUserId());
  }

  @Test
  void ifItDeletesUser() throws NamingException, SQLException, InvalidEntityException {
    User newUser = new User("John", "Long", "j.long@gmail.com", "passwd", false);

    User createdUser = userRepository.save(newUser);
    userRepository.delete(createdUser.getUserId());

    User deletedUser = userRepository.retrieve(createdUser.getUserId());
    assertTrue(deletedUser.getIsRetired());
  }

  @Test
  void ifItThrowsErrorWhenUserNotFound() {
    assertThrows(InvalidEntityException.class, () -> userRepository.retrieve(2));
  }

  @Test
  void ifItCreatesUser() throws SQLException {
    User newUser = new User("John", "Long", "j.long@gmail.com", "passwd", false);
    User createdUser = userRepository.save(newUser);
    assertNotNull(createdUser.getUserId());
    assertNotNull(createdUser.getFirstName());

    boolean isUser = userRepository.exists(createdUser.getUserId());
    assertTrue(isUser);
  }

  @Test
  void ifItUpdatesUser() throws SQLException {
    User newUser = new User("John", "Long", "j.long@gmail.com", "passwd", false);
    String newFirstName = "Roger";

    User createdUser = userRepository.save(newUser);
    createdUser.setFirstName(newFirstName);

    User updatedUser = userRepository.save(createdUser);
    assertEquals(newFirstName, updatedUser.getFirstName());
  }

  @Test
  void ifReturnsTrueWhenUserExists() throws SQLException {
    User newUser = new User("John", "Long", "j.long@gmail.com", "passwd", false);
    User createdUser = userRepository.save(newUser);
    boolean isUser = userRepository.exists(createdUser.getUserId());
    assertTrue(isUser);
  }

  @Test
  void ifItReturnsFalseWhenUserDoesNotExist() throws SQLException {
    boolean isUser = userRepository.exists(7);
    assertFalse(isUser);
  }
}