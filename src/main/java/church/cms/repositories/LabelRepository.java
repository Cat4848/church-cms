package church.cms.repositories;

import church.cms.domain.Label;
import church.cms.exceptions.InvalidEntityException;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabelRepository implements Repository<Label> {
  private final DataSource dataSource;
  private final Logger logger;

  public LabelRepository(DataSource dataSource, Logger logger) {
    this.dataSource = dataSource;
    this.logger = logger;
  }

  @Override
  public List<Label> list() throws SQLException {
    logger.info("list labels: start");

    String sql = "SELECT * FROM labels;";

    List<Label> labels = new ArrayList<>();

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer labelId = rs.getInt("label_id");
          String name = rs.getString("name");
          labels.add(new Label(labelId, name));
        }
      }
    }

    logger.info("list labels: end");

    return labels;
  }

  @Override
  public Label save(Label label) throws SQLException {
    logger.info("save label: start: labelId: {}", label.getLabelId());

    String sql;
    List<Object> params = new ArrayList<>(List.of(label.getName()));

    if (label.getLabelId() == null) {
      sql = "INSERT INTO labels (name) VALUES (?);";
    } else {
      sql = "UPDATE labels SET name = ? WHERE label_id = ?;";
      params.add(label.getLabelId());
    }
    logger.debug("save label: with SQL query: {}", sql);

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int size = params.size();
      for (int i = 0; i < size; i++) {
        stm.setObject(i + 1, params.get(i));
      }
      stm.executeUpdate();

      try (ResultSet rs = stm.getGeneratedKeys()) {
        while (rs.next()) {
          Integer newLabelId = rs.getInt(1);
          label.setLabelId(newLabelId);
        }
      }

      logger.info("save label: end");

      return label;
    }
  }

  @Override
  public Label retrieve(Integer id) throws SQLException, InvalidEntityException {
    logger.info("retrieve label: start: labelId: {}", id);

    String sql = "SELECT * FROM labels WHERE label_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          Integer labelId = rs.getInt("label_id");
          String name = rs.getString("name");

          logger.info("retrieve label: end");

          return new Label(labelId, name);
        }
      }
    }
    throw new InvalidEntityException("Label with ID " + id + " not found");
  }

  @Override
  public void delete(Integer id) throws SQLException {
    logger.info("delete label: start: labelId: {}", id);

    String sql = "DELETE FROM labels WHERE label_id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);
      stm.executeUpdate();
    }

    logger.info("delete label: end");
  }

  @Override
  public boolean exists(Integer id) throws SQLException {
    logger.info("exists label: start: label: {}", id);

    Integer checkedId = null;
    String sql = "SELECT label_id FROM labels WHERE label_id = ?;";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setInt(1, id);

      try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
          checkedId = rs.getInt("label_id");
        }
      }
    }

    logger.info("exists label: end");

    return checkedId != null;
  }

  @Override
  public void createTableIfNotExists() throws SQLException {
    logger.info("createTableIfNotExists labels: start");

    String sql = "CREATE TABLE IF NOT EXISTS labels (" +
            "label_id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY(label_id)," +
            "name VARCHAR(100) NOT NULL);";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.execute();
    }
    logger.info("createTableIfNotExists label: end");
  }

  @Override
  public void truncateTable() throws SQLException {
    logger.info("truncate table: start");

    String sql = "TRUNCATE TABLE labels;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.executeUpdate();
    }

    logger.info("truncate table: end");
  }
}