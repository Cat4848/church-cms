package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.*;
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

public class HymnRepositoryIntegrationTest {
  public static HymnRepository hymnRepository;
  public static AuthorRepository authorRepository;
  public static HymnBookRepository hymnBookRepository;
  public static TopicRepository topicRepository;
  public static LabelRepository labelRepository;

  public static Author createdAuthor;
  public static HymnBook createdHymnBook;
  public static Topic createdTopic;
  public static Label createdLabel;
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

    hymnBookRepository = appDeps.getHymnBookRepository();
    hymnBookRepository.createTableIfNotExists();

    topicRepository = appDeps.getTopicRepository();
    topicRepository.createTableIfNotExists();

    labelRepository = appDeps.getLabelRepository();
    labelRepository.createTableIfNotExists();

    hymnRepository = appDeps.getHymnRepository();
    hymnRepository.createTableIfNotExists();

    Author author = new Author("John Black");
    createdAuthor = authorRepository.save(author);

    HymnBook hymnBook = new HymnBook("Christina Hymns");
    createdHymnBook = hymnBookRepository.save(hymnBook);

    Topic topic = new Topic("Faith");
    createdTopic = topicRepository.save(topic);

    Label label = new Label("Sunday Morning");
    createdLabel = labelRepository.save(label);
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifListsHymns() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn1 = createHymn("Be thou my vision");
    hymnRepository.save(hymn1);

    Hymn hymn2 = createHymn("It is well with my soul");
    hymnRepository.save(hymn2);

    List<Hymn> topics = hymnRepository.list();

    assertEquals(2, topics.size());
  }

  @Test
  void ifRetrievesHymn() throws SQLException, InvalidEntityException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    Integer hymnId = createdHymn.getHymnId();
    assertNotNull(hymnId);

    Hymn retrievedHymn = hymnRepository.retrieve(hymnId);
    assertEquals(retrievedHymn.getTitle(), createdHymn.getTitle());
  }

  @Test
  void ifDeletesHymn() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    Integer hymnId = createdHymn.getHymnId();
    assertNotNull(hymnId);

    hymnRepository.delete(hymnId);
    assertThrows(InvalidEntityException.class, () -> hymnRepository.retrieve(hymnId));
  }

  @Test
  void ifThrowsErrorIfHymnNotFound() throws SQLException {
    hymnRepository.truncateTable();

    assertThrows(InvalidEntityException.class, () -> hymnRepository.retrieve(1));
  }

  @Test
  void ifCreatesHymn() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    Integer hymnId = createdHymn.getHymnId();
    assertNotNull(hymnId);
    assertNotNull(createdHymn.getTitle());

    boolean exists = hymnRepository.exists(hymnId);
    assertTrue(exists);
  }

  @Test
  void ifUpdatesHymn() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    Integer hymnId = createdHymn.getHymnId();
    String hymnTitle = createdHymn.getTitle();
    assertNotNull(hymnId);
    assertNotNull(hymnTitle);

    hymnTitle = "It is well with my soul";
    createdHymn.setTitle(hymnTitle);
    Hymn updatedTopic = hymnRepository.save(createdHymn);

    assertEquals(hymnTitle, updatedTopic.getTitle());
  }

  @Test
  void ifReturnsTrueWhenHymnExists() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);
    boolean exists = hymnRepository.exists(createdHymn.getHymnId());
    assertTrue(exists);
  }

  @Test
  void ifReturnsFalseWhenHymnDoesNotExists() throws SQLException {
    hymnRepository.truncateTable();

    boolean exists = hymnRepository.exists(1);
    assertFalse(exists);
  }

  Hymn createHymn(String title) {
    return new Hymn(
            createdAuthor.getAuthorId(),
            "1800-1900",
            title,
            "Lyrics",
            createdHymnBook.getHymnBookId(),
            10,
            createdTopic.getTopicId(),
            createdLabel.getLabelId()
    );
  }
}
