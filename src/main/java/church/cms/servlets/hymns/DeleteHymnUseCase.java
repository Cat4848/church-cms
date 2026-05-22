package church.cms.servlets.hymns;

import church.cms.repositories.HymnRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteHymnUseCase {
  private final HymnRepository hymnRepository;
  private final Logger logger;

  public DeleteHymnUseCase(HymnRepository hymnRepository, Logger logger) {
    this.hymnRepository = hymnRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, IllegalArgumentException,
          SQLException {
    logger.info("start");

    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      throw new IllegalArgumentException("Specify Hymn ID when deleting a Hymn.");
    }

    String[] pathParts = pathInfo.split("/");
    if (pathParts.length < 2) {
      throw new IllegalArgumentException("The Hymn ID in not included in the URL path.");
    }

    Integer hymnId = Integer.parseInt(pathParts[1]);
    boolean exists = hymnRepository.exists(hymnId);
    if (!exists) {
      logger.error("The Hymn with hymnId {} doesn't exist.", hymnId);

      throw new IllegalArgumentException("The Hymn with Hymn ID " + hymnId + " doesn't exist.");
    }

    hymnRepository.delete(hymnId);

    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
