package repositories;

import javax.naming.NamingException;
import java.sql.SQLException;

public interface Repository<T> {
  void save(T entity);
  T retrieve(String id);
  void delete(String id);
  boolean exists(Long id) throws SQLException, NamingException;
}