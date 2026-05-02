package church.cms.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

@WebListener
public class AppDepsInit implements ServletContextListener {
  private final Logger logger = LoggerFactory.getLogger(AppDepsInit.class);

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      logger.info("start");

      DataSource dataSource = (DataSource) new InitialContext().lookup(System.getenv("CHURCH_CMS_DB"));
      AppDeps appDeps = new AppDeps(dataSource);
      servletContextEvent.getServletContext().setAttribute("appDeps", appDeps);

      logger.info("end");
    } catch (NamingException | SQLException e) {
      logger.error("initialising app dependencies: error", e);
      throw new RuntimeException(e);
    }
  }
}
