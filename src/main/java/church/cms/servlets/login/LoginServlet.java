package church.cms.servlets.login;

import church.cms.context.AppDeps;
import church.cms.exceptions.InvalidEntityException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("start");

    // TODO validation on body
    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the users doPost servlet");
      }

      deps.getLoginUseCase().execute(req, res);

      logger.info("end");
    } catch (IOException | InvalidEntityException | SQLException | IllegalStateException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      logger.error("LoginServlet error", e);
    }
  }
}