package church.cms.repositories;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;

import javax.naming.NamingException;
import java.sql.SQLException;

public interface Repository<T> {
  User save(T entity) throws SQLException, InvalidEntityException;

  T retrieve(Integer id) throws NamingException, SQLException, InvalidEntityException;

  void delete(Integer id) throws NamingException, SQLException, InvalidEntityException;

  boolean exists(Integer id) throws NamingException, SQLException;

  void createTableIfNotExists() throws SQLException;
}