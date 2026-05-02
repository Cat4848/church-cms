package repositories;

import domain.User;
import exceptions.InvalidEntityException;

import javax.naming.NamingException;
import java.sql.SQLException;

public interface Repository<T> {
  User save(T entity) throws SQLException, InvalidEntityException;

  T retrieve(Integer id) throws NamingException, SQLException, InvalidEntityException;

  void delete(Integer id) throws NamingException, SQLException, InvalidEntityException;

  boolean exists(Integer id) throws NamingException, SQLException;

  void createTableIfNotExists() throws SQLException;
}