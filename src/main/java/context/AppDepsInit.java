package context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

@WebListener
public class AppDepsInit implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      DataSource dataSource = (DataSource) new InitialContext().lookup(System.getenv("CHURCH_CMS_DB"));
      AppDeps appDeps = new AppDeps(dataSource);
      servletContextEvent.getServletContext().setAttribute("appDeps", appDeps);
    } catch (NamingException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
