package church.cms.servlets.hymnBooks;

import church.cms.context.AppDeps;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/hymn-books/*")
public class HymnBooksServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(HymnBooksServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("POST create hymn book endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymn books doPost servlet");
      }

      deps.getCreateHymnBookUseCase().execute(req, res);

      logger.info("POST create hymn book endpoint: end");
    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("POST create hymn book endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST create hymn book endpoint: internal server error", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET hymn books endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymn books doGet servlet");
      }

      deps.getListHymnBooksUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST list hymn books endpoint: internal server error", e);
    }

    logger.info("GET hymn books endpoint: end");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("PUT update hymn book: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymn books doGet servlet");
      }

      deps.getUpdateHymnBookUseCase().execute(req, res);

    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update hymn book endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage() + "my message");

      logger.error("PUT update hymn book endpoint: internal server error", e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("DELETE delete hymn book: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymn books doGet servlet");
      }

      deps.getDeleteHymnBookUseCase().execute(req, res);

    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete hymn book endpoint: bad request error", e);
    } catch(IllegalStateException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete hymn book endpoint: internal server error",e);
    }

    logger.info("DELETE delete hymn book: end");
  }
}
