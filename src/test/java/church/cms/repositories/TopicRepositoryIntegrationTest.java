package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.Topic;
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

public class TopicRepositoryIntegrationTest {
  public static TopicRepository topicRepository;
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

    topicRepository = appDeps.getTopicRepository();
    topicRepository.createTableIfNotExists();
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifListsTopics() throws SQLException {
    topicRepository.truncateTable();

    Topic topic1 = new Topic("Faith");
    topicRepository.save(topic1);
    Topic topic2 = new Topic("Joy");
    topicRepository.save(topic2);

    List<Topic> topics = topicRepository.list();

    assertEquals(2, topics.size());
  }

  @Test
  void ifRetrievesTopic() throws SQLException, InvalidEntityException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);

    Integer topicId = createdTopic.getTopicId();
    assertNotNull(topicId);

    Topic retrievedTopic = topicRepository.retrieve(topicId);
    assertEquals(retrievedTopic.getName(), createdTopic.getName());
  }

  @Test
  void ifDeletesTopic() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);

    Integer topicId = createdTopic.getTopicId();
    assertNotNull(topicId);

    topicRepository.delete(topicId);
    assertThrows(InvalidEntityException.class, () -> topicRepository.retrieve(topicId));
  }

  @Test
  void ifThrowsErrorIfTopicNotFound() throws SQLException {
    topicRepository.truncateTable();

    assertThrows(InvalidEntityException.class, () -> topicRepository.retrieve(1));
  }

  @Test
  void ifCreatedTopic() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);

    Integer topicId = createdTopic.getTopicId();
    assertNotNull(topicId);
    assertNotNull(createdTopic.getName());

    boolean exists = topicRepository.exists(topicId);
    assertTrue(exists);
  }

  @Test
  void ifUpdatedTopic() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);

    Integer topicId = createdTopic.getTopicId();
    String hymnBookName = createdTopic.getName();
    assertNotNull(topicId);
    assertNotNull(hymnBookName);

    hymnBookName = "Joy";
    createdTopic.setName(hymnBookName);
    Topic updatedTopic = topicRepository.save(createdTopic);

    assertEquals(hymnBookName, updatedTopic.getName());
  }

  @Test
  void ifReturnsTrueWhenTopicExists() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);
    boolean exists = topicRepository.exists(createdTopic.getTopicId());
    assertTrue(exists);
  }

  @Test
  void ifReturnsFalseWhenTopicDoesNotExists() throws SQLException {
    topicRepository.truncateTable();

    boolean exists = topicRepository.exists(1);
    assertFalse(exists);
  }
}
