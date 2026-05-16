package church.cms.repositories;

import church.cms.domain.Topic;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicRepository implements Repository<Topic> {
  private final DataSource dataSource;
  private final Logger logger;

  public TopicRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }

  @Override
  public List<Topic> list() throws SQLException {
    logger.info("list topics: start");

    String sql = "SELECT * FROM topics;";

    List<Topic> topics = new ArrayList<>();

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer topicId = rs.getInt("topic_id");
          String name = rs.getString("name");
          topics.add(new Topic(topicId, name));
        }
      }
    }

    logger.info("list topics: end");

    return topics;
  }

  @Override
  public Topic save(Topic topic) throws SQLException {
    logger.info("save topic: start: topicId: {}", topic.getTopicId());

    String sql;
    List<Object> params = new ArrayList<>(List.of(topic.getName()));

    if (topic.getTopicId() == null) {
      sql = "INSERT INTO topics (name) VALUES (?);";
    } else {
      sql = "UPDATE topics SET name = ? WHERE topic_id = ?;";
      params.add(topic.getTopicId());
    }
    logger.debug("save topic: with SQL query: {}", sql);

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newTopicId = rs.getInt(1);
          topic.setTopicId(newTopicId);
        }
      }

      logger.info("save topic: end");

      return topic;
    }
  }

  @Override
  public Topic retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve topic: start: topicId: {}", id);

    String sql = "SELECT * FROM topics WHERE topic_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer topicId = rs.getInt("topic_id");
          String name = rs.getString("name");

          logger.info("retrieve topic: end");

          return new Topic(topicId, name);
        }
      }
    }
    throw new InvalidEntityException("Topic with ID " + id + " not found");
  }

  @Override
  public void delete(Integer id) throws SQLException {
    logger.info("delete topic: start: topicId: {}", id);

    String sql = "DELETE FROM topics WHERE topic_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);
      stm.executeUpdate();
    }

    logger.info("delete topic: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists topic: start: topic: {}", id);

    Integer checkedId = null;
    String sql = "SELECT topic_id FROM topics WHERE topic_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("topic_id");
        }
      }
    }

    logger.info("exists topic: end");

    return checkedId != null;
  }

  @Override
  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists topics: start");

    String sql = "CREATE TABLE IF NOT EXISTS topics (" +
            "topic_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY(topic_id)," +
            "name VARCHAR(100) NOT NULL);";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
    logger.info("createTableIfNotExists topic: end");
  }

  @Override
  public void truncateTable() throws SQLException {
    logger.info("truncate table: start");

    String sql = "TRUNCATE TABLE topics;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.executeUpdate();
    }

    logger.info("truncate table: end");
  }
}