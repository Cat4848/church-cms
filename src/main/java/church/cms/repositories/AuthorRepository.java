package church.cms.repositories;

import church.cms.domain.Author;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository implements Repository<Author> {
  private final DataSource dataSource;
  private final Logger logger;

  public AuthorRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }


  @Override
  public List<Author> list(){
    return new ArrayList<>();
  }

  @Override
  public Author save(Author author) throws SQLException {
    logger.info("save author: start: authorId: {}", author.getAuthorId());

    String sql;
    List<Object> params = new ArrayList<>(List.of(author.getName()));

    if (author.getAuthorId() == null) {
      sql = "INSERT INTO authors (name) VALUES (?);";
    } else {
      sql = "UPDATE authors SET name = ? WHERE author_id = ?;";
      params.add(author.getAuthorId());
    }
    logger.debug("save author: with SQL query: {}", sql);

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newAuthorId = rs.getInt(1);
          author.setAuthorId(newAuthorId);
        }
      }

      logger.info("save author: end");

      return author;
    }
  }

  @Override
  public Author retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve author: start: authorId: {}", id);

    String sql = "SELECT * FROM authors WHERE author_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer authorId = rs.getInt("author_id");
          String name = rs.getString("name");

          logger.info("retrieve author: end");

          return new Author(authorId, name);
        }
      }
    }
    throw new InvalidEntityException("Author with ID " + id + " not found");
  }

  @Override
  public void delete(Integer id) throws SQLException {
    logger.info("delete author: start: authorId: {}", id);

    String sql = "DELETE FROM authors WHERE author_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);
      stm.executeUpdate();
    }

    logger.info("delete author: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists author: start: authorId: {}", id);

    Integer checkedId = null;
    String sql = "SELECT author_id FROM authors WHERE author_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("author_id");
        }
      }
    }

    logger.info("exists author: end");

    return checkedId != null;
  }

  @Override
  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists author: start");

    String sql = "CREATE TABLE IF NOT EXISTS authors (" +
            "author_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY(author_id)," +
            "name VARCHAR(100) NOT NULL);";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
    logger.info("createTableIfNotExists author: end");
  }

}