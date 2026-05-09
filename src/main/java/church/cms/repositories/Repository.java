package church.cms.repositories;

import church.cms.exceptions.InvalidEntityException;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
  List<T> list() throws SQLException;

  T save(T entity) throws SQLException;

  T retrieve(Integer id) throws SQLException, InvalidEntityException;

  void delete(Integer id) throws SQLException, InvalidEntityException;

  boolean exists(Integer id) throws SQLException;

  void createTableIfNotExists() throws SQLException;
}