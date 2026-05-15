package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.HymnBook;
import church.cms.exceptions.InvalidEntityException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HymnBookRepositoryIntegrationTest {
  public static HymnBookRepository hymnBookRepository;
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

    hymnBookRepository = appDeps.getHymnBookRepository();
    hymnBookRepository.createTableIfNotExists();
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifListsHymnBooks() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook1 = new HymnBook("Christian Faith");
    hymnBookRepository.save(hymnBook1);
    HymnBook hymnBook2 = new HymnBook("Red Hymns Book");
    hymnBookRepository.save(hymnBook2);

    List<HymnBook> hymnBooks = hymnBookRepository.list();

    assertEquals(2, hymnBooks.size());
  }

  @Test
  void ifRetrievesHymnBook() throws SQLException, InvalidEntityException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);

    Integer hymnBookId = createdHymnBook.getHymnBookId();
    assertNotNull(hymnBookId);

    HymnBook retrievedHymnBook = hymnBookRepository.retrieve(hymnBookId);
    assertEquals(retrievedHymnBook.getName(), createdHymnBook.getName());
  }

  @Test
  void ifDeletesHymnBook() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);

    Integer hymnBookId = createdHymnBook.getHymnBookId();
    assertNotNull(hymnBookId);

    hymnBookRepository.delete(hymnBookId);
    assertThrows(InvalidEntityException.class, () -> hymnBookRepository.retrieve(hymnBookId));
  }

  @Test
  void ifThrowsErrorIfHymnBookNotFound() throws SQLException {
    hymnBookRepository.truncateTable();

    assertThrows(InvalidEntityException.class, () -> hymnBookRepository.retrieve(1));
  }

  @Test
  void ifCreatedHymnBook() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);

    Integer hymnBookId = createdHymnBook.getHymnBookId();
    assertNotNull(hymnBookId);
    assertNotNull(createdHymnBook.getName());

    boolean exists = hymnBookRepository.exists(hymnBookId);
    assertTrue(exists);
  }

  @Test
  void ifUpdatedHymnBook() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);

    Integer hymnBookId = createdHymnBook.getHymnBookId();
    String hymnBookName = createdHymnBook.getName();
    assertNotNull(hymnBookId);
    assertNotNull(hymnBookName);

    hymnBookName = "Red Hymn Book";
    createdHymnBook.setName(hymnBookName);
    HymnBook updatedHymnBook = hymnBookRepository.save(createdHymnBook);

    assertEquals(hymnBookName, updatedHymnBook.getName());
  }

  @Test
  void ifReturnsTrueWhenHymnBookExists() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);
    boolean exists = hymnBookRepository.exists(createdHymnBook.getHymnBookId());
    assertTrue(exists);
  }

  @Test
  void ifReturnsFalseWhenHymnBookDoesNotExists() throws SQLException {
    hymnBookRepository.truncateTable();

    boolean exists = hymnBookRepository.exists(1);
    assertFalse(exists);
  }
}
