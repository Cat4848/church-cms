package church.cms.servlets.hymns;

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

@WebServlet("/api/hymns/*")
public class HymnsServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(HymnsServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("POST create hymn endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymns doPost servlet");
      }

      deps.getCreateHymnUseCase().execute(req, res);

      logger.info("POST create hymn endpoint: end");
    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("POST create hymn endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST create hymn endpoint: internal server error", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET hymns endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymns doGet servlet");
      }

      deps.getListHymnsUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("GET list hymns endpoint: internal server error", e);
    }

    logger.info("GET hymns endpoint: end");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("PUT update hymn: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymns doGet servlet");
      }

      deps.getUpdateHymnUseCase().execute(req, res);

    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update hymn endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update hymn endpoint: internal server error", e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("DELETE delete hymn: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the hymns doGet servlet");
      }

      deps.getDeleteHymnUseCase().execute(req, res);

    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete hymn endpoint: bad request error", e);
    } catch (IllegalStateException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete hymn endpoint: internal server error", e);
    }

    logger.info("DELETE delete hymn: end");
  }
}
