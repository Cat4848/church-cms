package church.cms.servlets.topics;

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

@WebServlet("/api/topics/*")
public class TopicsServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(TopicsServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("POST create topic endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the topics doPost servlet");
      }

      deps.getCreateTopicUseCase().execute(req, res);

      logger.info("POST create topic endpoint: end");
    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("POST create topic endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST create topic endpoint: internal server error", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET topics endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the topics doGet servlet");
      }

      deps.getListTopicsUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("GET list topics endpoint: internal server error", e);
    }

    logger.info("GET topics endpoint: end");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("PUT update topic: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the topics doGet servlet");
      }

      deps.getUpdateTopicUseCase().execute(req, res);

    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update topic endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update topic endpoint: internal server error", e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("DELETE delete topic: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the topics doGet servlet");
      }

      deps.getDeleteTopicUseCase().execute(req, res);

    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete topic endpoint: bad request error", e);
    } catch (IllegalStateException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete topic endpoint: internal server error", e);
    }

    logger.info("DELETE delete topic: end");
  }
}
