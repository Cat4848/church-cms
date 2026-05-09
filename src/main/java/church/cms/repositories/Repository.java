package church.cms.repositories;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;

import javax.naming.NamingException;
import java.sql.SQLException;

public interface Repository<T> {
  T save(T entity) throws SQLException;

  T retrieve(Integer id) throws SQLException, InvalidEntityException;

  void delete(Integer id) throws SQLException, InvalidEntityException;

  boolean exists(Integer id) throws SQLException;

  void createTableIfNotExists() throws SQLException;
}