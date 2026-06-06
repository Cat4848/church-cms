package church.cms.servlets.session;

import church.cms.context.AppDeps;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import church.cms.servlets.login.LoginSuccessResponsePayload;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/session/*")
public class SessionServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(SessionServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the users doPost servlet");
      }

      HttpSession session = req.getSession(false);
      if (session == null) {
        throw new IllegalStateException("The request has no active session.");
        // TODO add logging
      }

      Integer userId = (Integer) session.getAttribute("userId");
      if(userId == null) {
        throw new IllegalStateException("The userId not found in the session.");
      }

      User user = deps.getUserRepository().retrieve(userId);

      LoginSuccessResponsePayload userStatePayload = new LoginSuccessResponsePayload(
              user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getIsAdmin()
      );

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);
      deps.getObjectMapper().writeValue(res.getWriter(), userStatePayload);

      logger.info("end");
    } catch (IllegalStateException e) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    } catch (IOException | InvalidEntityException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      logger.error("LoginServlet error", e);
    }
  }
}