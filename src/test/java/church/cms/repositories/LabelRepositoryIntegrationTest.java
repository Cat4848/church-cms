package church.cms.repositories;

import church.cms.context.AppDeps;
import church.cms.domain.Label;
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

public class LabelRepositoryIntegrationTest {
  public static LabelRepository labelRepository;
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

    labelRepository = appDeps.getLabelRepository();
    labelRepository.createTableIfNotExists();
  }

  @AfterAll
  public static void afterAll() {
    dbContainer.stop();
  }

  @Test
  void ifListsLabels() throws SQLException {
    labelRepository.truncateTable();

    Label label1 = new Label("Morning Service");
    labelRepository.save(label1);
    Label label2 = new Label("Evening Service");
    labelRepository.save(label2);

    List<Label> topics = labelRepository.list();

    assertEquals(2, topics.size());
  }

  @Test
  void ifRetrievesLabel() throws SQLException, InvalidEntityException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdLabel = labelRepository.save(label);

    Integer labelId = createdLabel.getLabelId();
    assertNotNull(labelId);

    Label retrievedLabel = labelRepository.retrieve(labelId);
    assertEquals(retrievedLabel.getName(), createdLabel.getName());
  }

  @Test
  void ifDeletesLabel() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdLabel = labelRepository.save(label);

    Integer labelId = createdLabel.getLabelId();
    assertNotNull(labelId);

    labelRepository.delete(labelId);
    assertThrows(InvalidEntityException.class, () -> labelRepository.retrieve(labelId));
  }

  @Test
  void ifThrowsErrorIfLabelNotFound() throws SQLException {
    labelRepository.truncateTable();

    assertThrows(InvalidEntityException.class, () -> labelRepository.retrieve(1));
  }

  @Test
  void ifCreatedLabel() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdLabel = labelRepository.save(label);

    Integer labelId = createdLabel.getLabelId();
    assertNotNull(labelId);
    assertNotNull(createdLabel.getName());

    boolean exists = labelRepository.exists(labelId);
    assertTrue(exists);
  }

  @Test
  void ifUpdatedLabel() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdLabel = labelRepository.save(label);

    Integer labelId = createdLabel.getLabelId();
    String labelName = createdLabel.getName();
    assertNotNull(labelId);
    assertNotNull(labelName);

    labelName = "Evening Service";
    createdLabel.setName(labelName);
    Label updatedTopic = labelRepository.save(createdLabel);

    assertEquals(labelName, updatedTopic.getName());
  }

  @Test
  void ifReturnsTrueWhenLabelExists() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdLabel = labelRepository.save(label);
    boolean exists = labelRepository.exists(createdLabel.getLabelId());
    assertTrue(exists);
  }

  @Test
  void ifReturnsFalseWhenLabelDoesNotExists() throws SQLException {
    labelRepository.truncateTable();

    boolean exists = labelRepository.exists(1);
    assertFalse(exists);
  }
}
