package church.cms.servlets.labels;

import church.cms.repositories.LabelRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteLabelUseCase {
  private final LabelRepository labelRepository;
  private final Logger logger;

  public DeleteLabelUseCase(LabelRepository labelRepository, Logger logger) {
    this.labelRepository = labelRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, IllegalArgumentException,
          SQLException {
    logger.info("start");

    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      throw new IllegalArgumentException("Specify Label ID when deleting a Label.");
    }

    String[] pathParts = pathInfo.split("/");
    if (pathParts.length < 2) {
      throw new IllegalArgumentException("The Label ID in not included in the URL path.");
    }

    Integer labelId = Integer.parseInt(pathParts[1]);
    boolean exists = labelRepository.exists(labelId);
    if (!exists) {
      logger.info("The Label with labelId {} doesn't exist.", labelId);

      throw new IllegalArgumentException("The Label with Label ID " + labelId + " doesn't exist.");
    }

    labelRepository.delete(labelId);

    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
