package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.Author;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration Test suite for the AuthorRepository")
public class AuthorRepositoryIntegrationTest {
  public static Repository<Author> authorRepository;
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

    authorRepository = appDeps.getAuthorRepository();
    authorRepository.createTableIfNotExists();
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifListsAuthors() throws SQLException {
    authorRepository.truncateTable();

    Author newAuthor1 = new Author("John Newton");
    Author newAuthor2 = new Author("Brian Green");
    authorRepository.save(newAuthor1);
    authorRepository.save(newAuthor2);

    List<Author> authors = authorRepository.list();

    assertEquals(2, authors.size());
  }

  @Test
  void ifRetrievesAuthor() throws SQLException, InvalidEntityException {
    Author newAuthor = new Author("John Newton");
    Author createdAuthor = authorRepository.save(newAuthor);

    Author retrievedAuthor = authorRepository.retrieve(createdAuthor.getAuthorId());

    assertNotNull(retrievedAuthor.getAuthorId());
    assertEquals(createdAuthor.getAuthorId(), retrievedAuthor.getAuthorId());
  }

  @Test
  void ifItDeletesAuthor() throws SQLException, InvalidEntityException {
    Author newAuthor = new Author("John Newton");
    Author createdAuthor = authorRepository.save(newAuthor);

    authorRepository.delete(createdAuthor.getAuthorId());
    assertThrows(InvalidEntityException.class, () -> authorRepository.retrieve(createdAuthor.getAuthorId()));
  }

  @Test
  void ifItThrowsErrorWhenAuthorNotFound() {
    assertThrows(InvalidEntityException.class, () -> authorRepository.retrieve(12));
  }

  @Test
  void ifCreatesAuthor() throws SQLException {
    Author newAuthor = new Author("William");
    Author createdUser = authorRepository.save(newAuthor);
    assertNotNull(createdUser.getAuthorId());
    assertNotNull(createdUser.getName());

    boolean isAuthor = authorRepository.exists(createdUser.getAuthorId());
    assertTrue(isAuthor);
  }

  @Test
  void ifItUpdatesAuthor() throws SQLException {
    Author newAuthor = new Author("William Spencer");
    String newFirstName = "Roger White";

    Author createdAuthor = authorRepository.save(newAuthor);
    createdAuthor.setName(newFirstName);

    Author updatedAuthor = authorRepository.save(createdAuthor);
    assertEquals(newFirstName, updatedAuthor.getName());
  }

  @Test
  void ifReturnsTrueWhenAuthorExists() throws SQLException {
    Author newAuthor = new Author("John Newton");
    Author createdAuthor = authorRepository.save(newAuthor);
    boolean isAuthor = authorRepository.exists(createdAuthor.getAuthorId());
    assertTrue(isAuthor);
  }

  @Test
  void ifItReturnsFalseWhenAuthorDoesNotExist() throws SQLException {
    boolean isAuthor = authorRepository.exists(11);
    assertFalse(isAuthor);
  }
}
