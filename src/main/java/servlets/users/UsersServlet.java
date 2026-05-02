package servlets.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.AppDeps;
import domain.User;
import exceptions.InvalidEntityException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.ws.rs.core.Response;
import repositories.Repository;

import javax.naming.NamingException;

@WebServlet("/api/users/*")
public class UsersServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

          AppDeps ctx = (AppDeps) getServletContext().getAttribute("appContext");
          Repository<User> userRepository = ctx.getUserRepository();
          User user = userRepository.retrieve(1);

          res.setContentType("application/json");
          res.setStatus(Response.Status.OK.getStatusCode());
          objectMapper.writeValue(res.getOutputStream(), user);
        }
      }

    } catch (SQLException | InvalidEntityException | NamingException e) {
      res.setContentType("text/plain");
      res.setStatus(500);
      res.getWriter().write("we have an error on GET users route" + e.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the users doPost servlet");
      }

      deps.getCreateUserUseCase().execute(req, res);
    } catch (IOException | IllegalStateException | InvalidEntityException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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