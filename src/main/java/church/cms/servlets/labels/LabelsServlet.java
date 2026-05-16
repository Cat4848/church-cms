package church.cms.servlets.labels;

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

@WebServlet("/api/labels/*")
public class LabelsServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(LabelsServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("POST create label endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the labels doPost servlet");
      }

      deps.getCreateLabelUseCase().execute(req, res);

      logger.info("POST create label endpoint: end");
    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("POST create label endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("POST create label endpoint: internal server error", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("GET labels endpoint: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the labels doGet servlet");
      }

      deps.getListLabelsUseCase().execute(req, res);

    } catch (IOException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("GET list labels endpoint: internal server error", e);
    }

    logger.info("GET labels endpoint: end");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("PUT update label: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the labels doGet servlet");
      }

      deps.getUpdateLabelUseCase().execute(req, res);

    } catch (IllegalArgumentException | DatabindException | StreamReadException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update label endpoint: bad request error", e);
    } catch (SQLException | IllegalStateException | StreamWriteException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("PUT update label endpoint: internal server error", e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
    logger.info("DELETE delete label: start");

    try {
      AppDeps deps = (AppDeps) getServletContext().getAttribute("appDeps");
      if (deps == null) {
        throw new IllegalStateException("The appDeps dependency doesn't exist in the labels doGet servlet");
      }

      deps.getDeleteLabelUseCase().execute(req, res);

    } catch (IllegalArgumentException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete label endpoint: bad request error", e);
    } catch (IllegalStateException | SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(e.getMessage());

      logger.error("DELETE delete label endpoint: internal server error", e);
    }

    logger.info("DELETE delete label: end");
  }
}
