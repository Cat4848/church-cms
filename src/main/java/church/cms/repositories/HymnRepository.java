package church.cms.repositories;

import church.cms.domain.Hymn;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HymnRepository implements Repository<Hymn> {
  private final DataSource dataSource;
  private final Logger logger;

  public HymnRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }

  // TODO implement get with pagination
  @Override
  public List<Hymn> list() throws SQLException {
    logger.info("list hymns: start");

    String sql = "SELECT * FROM hymns;";

    List<Hymn> hymns = new ArrayList<>();

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer hymnId = rs.getInt("hymn_id");
          Integer authorId = rs.getInt("author_id");
          String authorExtras = rs.getString("author_extras");
          String title = rs.getString("title");
          String lyrics = rs.getString("lyrics");
          Integer hymnBookId = rs.getInt("hymn_book_id");
          Integer numberInHymnBook = rs.getInt("number_in_hymn_book");
          Integer topicId = rs.getInt("topic_id");
          Integer labelId = rs.getInt("label_id");

          Hymn hymn = new Hymn(hymnId,
                               authorId,
                               authorExtras,
                               title,
                               lyrics,
                               hymnBookId,
                               numberInHymnBook,
                               topicId,
                               labelId);
          hymns.add(hymn);
        }
      }
    }

    logger.info("list hymns: end");

    return hymns;
  }

  @Override
  public Hymn save(Hymn hymn) throws SQLException {
    logger.info("save hymn: start: hymnId: {}", hymn.getHymnId());

    String sql;
    List<Object> params = new ArrayList<>(List.of(
            hymn.getAuthorId(),
            hymn.getAuthorExtras(),
            hymn.getTitle(),
            hymn.getLyrics(),
            hymn.getHymnBookId(),
            hymn.getNumberInHymnBook(),
            hymn.getTopicId(),
            hymn.getLabelId()
    ));

    if (hymn.getHymnId() == null) {
      sql = "INSERT INTO hymns (" +
              "author_id," +
              "author_extras," +
              "title," +
              "lyrics," +
              "hymn_book_id," +
              "number_in_hymn_book," +
              "topic_id," +
              "label_id" +
              ") VALUES (?,?,?,?,?,?,?,?);";
    } else {
      sql = "UPDATE hymns SET " +
              "author_id = ?," +
              "author_extras = ?," +
              "title = ?," +
              "lyrics = ?," +
              "hymn_book_id = ?," +
              "number_in_hymn_book = ?," +
              "topic_id = ?," +
              "label_id = ? " +
              "WHERE hymn_id = ?;";
      params.add(hymn.getHymnId());
    }
    logger.debug("save hymn: with SQL query: {}", sql);

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newHymnId = rs.getInt(1);
          hymn.setHymnId(newHymnId);
        }
      }

      logger.info("save hymn: end");

      return hymn;
    }
  }

  @Override
  public Hymn retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve hymn: start: hymnId: {}", id);

    String sql = "SELECT * FROM hymns WHERE hymn_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer hymnId = rs.getInt("hymn_id");
          Integer authorId = rs.getInt("author_id");
          String authorExtras = rs.getString("author_extras");
          String title = rs.getString("title");
          String lyrics = rs.getString("lyrics");
          Integer hymnBookId = rs.getInt("hymn_book_id");
          Integer numberInHymnBook = rs.getInt("number_in_hymn_book");
          Integer topicId = rs.getInt("topic_id");
          Integer labelId = rs.getInt("label_id");

          logger.info("retrieve hymn: end");

          return new Hymn(
                  hymnId,
                  authorId,
                  authorExtras,
                  title,
                  lyrics,
                  hymnBookId,
                  numberInHymnBook,
                  topicId,
                  labelId
          );
        }
      }
    }
    throw new InvalidEntityException("Hymn with ID " + id + " not found");
  }

  @Override
  public void delete(Integer id) throws SQLException {
    logger.info("delete hymn: start: hymnId: {}", id);

    String sql = "DELETE FROM hymns WHERE hymn_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);
      stm.executeUpdate();
    }

    logger.info("delete hymn: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists hymn: start: hymn: {}", id);

    Integer checkedId = null;
    String sql = "SELECT hymn_id FROM hymns WHERE hymn_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("hymn_id");
        }
      }
    }

    logger.info("exists hymn: end");

    return checkedId != null;
  }

  @Override
  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists hymns: start");

    String sql = "CREATE TABLE IF NOT EXISTS hymns (" +
                 "hymn_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                 "author_id INT UNSIGNED NOT NULL," +
                 "author_extras VARCHAR (200)," +
                 "title VARCHAR(100) NOT NULL," +
                 "lyrics TEXT NOT NULL," +
                 "hymn_book_id INT UNSIGNED," +
                 "number_in_hymn_book INT UNSIGNED," +
                 "topic_id INT UNSIGNED," +
                 "label_id INT UNSIGNED," +
                 "PRIMARY KEY (hymn_id)," +
                 "FOREIGN KEY(author_id) REFERENCES authors (author_id)," +
                 "FOREIGN KEY(hymn_book_id) REFERENCES hymn_books (hymn_book_id)," +
                 "FOREIGN KEY(topic_id) REFERENCES topics (topic_id)," +
                 "FOREIGN KEY(label_id) REFERENCES labels (label_id)," +
                 "FULLTEXT INDEX ft_lyrics (lyrics));";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
    logger.info("createTableIfNotExists hymn: end");
  }

  @Override
  public void truncateTable() throws SQLException {
    logger.info("truncate table: start");

    String sql = "TRUNCATE TABLE hymns;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.executeUpdate();
    }

    logger.info("truncate table: end");
  }
}