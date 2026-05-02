package servlets.login;

import context.AppDeps;
import exceptions.InvalidEntityException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // TODO validation on body
    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the users doPost servlet");
      }

      deps.getLoginUseCase().execute(req, res);
    } catch (IOException | InvalidEntityException | SQLException | IllegalStateException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
}