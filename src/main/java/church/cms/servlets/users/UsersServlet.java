package church.cms.servlets.users;

import church.cms.context.AppDeps;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import church.cms.repositories.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/users/*")
public class UsersServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(UsersServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET users endpoint: start");
    // get all users
    // get user by id = /users/123
    // edit one user = /users/

    Integer userIdT = (Integer) req.getAttribute("userId");
    try {
      String pathInfo = req.getPathInfo();
      if (pathInfo == null || pathInfo.equals("/")) {
        // get all users
      } else {
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length > 1) {
          String userId = pathParts[1];
          // handle the NumberFormatException in case the user passes abc instead on 123
          Long userIdLong = Long.valueOf(userId);
          ObjectMapper objectMapper = new ObjectMapper();

          AppDeps ctx = (AppDeps) getServletContext().getAttribute("appDeps");
          Repository<User> userRepository = ctx.getUserRepository();
          User user = userRepository.retrieve(19);

          res.setContentType("application/json");
          res.setStatus(HttpServletResponse.SC_OK);
          objectMapper.writeValue(res.getWriter(), user);
        }
      }

      logger.info("GET users: end");
    } catch (SQLException | InvalidEntityException e) {
      res.setContentType("text/plain");
      res.setStatus(500);
      res.getWriter().write("we have an error on GET users route" + e.getMessage());

      logger.error("GET users: error", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    logger.info("POST users endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the users doPost servlet");
      }

      deps.getCreateUserUseCase().execute(req, res);

      logger.info("POST users endpoint: end");
    } catch (IOException | IllegalStateException | InvalidEntityException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      logger.error("POST users: error", e);
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // update one user = /users/123/edit
    res.setContentType("text/plain");
    res.getWriter().write("PUT user endpoint");
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // delete (retire) user = /users/123
    res.setContentType("text/plain");
    res.getWriter().write("DELETE user endpoint");
  }
}