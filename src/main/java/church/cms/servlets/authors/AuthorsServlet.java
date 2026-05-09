package church.cms.servlets.authors;

import church.cms.context.AppDeps;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/authors/*")
public class AuthorsServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(AuthorsServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("POST create author endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the authors doPost servlet");
      }

      deps.getCreateAuthorUseCase().execute(req, res);

      logger.info("POST create author endpoint: end");
    } catch (IllegalArgumentException | DatabindException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("POST create author endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST create author endpoint: internal server error", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET authors endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in he authors doGet servlet");
      }

      deps.getListAuthorsUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST list authors endpoint: internal server error", e);
    }

    logger.info("GET authors endpoint: end");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("PUT update author: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in he authors doGet servlet");
      }

      deps.getUpdateAuthorUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update author endpoint: internal server error", e);
    }
    logger.info("PUT update author: end");
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("DELETE delete author: start");
    logger.info("DELETE delete author: end");
  }
}
