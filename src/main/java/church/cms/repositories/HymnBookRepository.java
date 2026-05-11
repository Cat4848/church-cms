package church.cms.repositories;

import church.cms.domain.HymnBook;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HymnBookRepository implements Repository<HymnBook> {
  private final DataSource dataSource;
  private final Logger logger;

  public HymnBookRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }

  @Override
  public List<HymnBook> list() throws SQLException {
    logger.info("list hymn books: start");

    String sql = "SELECT * FROM hymn_books;";

    List<HymnBook> hymnBooks = new ArrayList<>();

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer hymnBookId = rs.getInt("hymn_book_id");
          String name = rs.getString("name");
          hymnBooks.add(new HymnBook(hymnBookId, name));
        }
      }
    }

    logger.info("list hymn books: end");

    return hymnBooks;
  }

  @Override
  public HymnBook save(HymnBook hymnBook) throws SQLException {
    logger.info("save hymn book: start: hymnBookId: {}", hymnBook.getHymnBookId());

    String sql;
    List<Object> params = new ArrayList<>(List.of(hymnBook.getName()));

    if (hymnBook.getHymnBookId() == null) {
      sql = "INSERT INTO hymn_books (name) VALUES (?);";
    } else {
      sql = "UPDATE hymn_books SET name = ? WHERE hymn_book_id = ?;";
      params.add(hymnBook.getHymnBookId());
    }
    logger.debug("save hymn book: with SQL query: {}", sql);

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newHymnBookId = rs.getInt(1);
          hymnBook.setHymnBookId(newHymnBookId);
        }
      }

      logger.info("save hymn book: end");

      return hymnBook;
    }
  }

  @Override
  public HymnBook retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve hymn book: start: hymnBookId: {}", id);

    String sql = "SELECT * FROM hymn_books WHERE hymn_book_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer hymnBookId = rs.getInt("hymn_book_id");
          String name = rs.getString("name");

          logger.info("retrieve hymn book: end");

          return new HymnBook(hymnBookId, name);
        }
      }
    }
    throw new InvalidEntityException("HymnBook with ID " + id + " not found");
  }

  @Override
  public void delete(Integer id) throws SQLException {
    logger.info("delete hymn book: start: hymnBookId: {}", id);

    String sql = "DELETE FROM hymn_books WHERE hymn_book_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);
      stm.executeUpdate();
    }

    logger.info("delete hymn book: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists hymn book: start: hymn book: {}", id);

    Integer checkedId = null;
    String sql = "SELECT hymn_book_id FROM hymn_books WHERE hymn_book_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("hymn_book_id");
        }
      }
    }

    logger.info("exists hymn book: end");

    return checkedId != null;
  }

  @Override
  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists author: start");

    String sql = "CREATE TABLE IF NOT EXISTS hymn_books (" +
            "hymn_book_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY(hymn_book_id)," +
            "name VARCHAR(100) NOT NULL);";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
    logger.info("createTableIfNotExists hymn book: end");
  }

  @Override
  public void truncateTable() throws SQLException {
    logger.info("truncate table: start");

    String sql = "TRUNCATE TABLE hymn_books;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.executeUpdate();
    }

    logger.info("truncate table: end");
  }
}