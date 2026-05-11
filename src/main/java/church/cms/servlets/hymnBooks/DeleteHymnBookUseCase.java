package church.cms.servlets.hymnBooks;

import church.cms.repositories.HymnBookRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteHymnBookUseCase {
  private final HymnBookRepository hymnBookRepository;
  private final Logger logger;

  public DeleteHymnBookUseCase(HymnBookRepository hymnBookRepository, Logger logger) {
    this.hymnBookRepository = hymnBookRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, IllegalArgumentException,
          SQLException {
    logger.info("start");

    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      throw new IllegalArgumentException("Specify hymn book ID when deleting a hymn book.");
    }

    String[] pathParts = pathInfo.split("/");
    if (pathParts.length < 2) {
      throw new IllegalArgumentException("The hymn book ID in not included in the URL path.");
    }

    Integer hymnBookId = Integer.parseInt(pathParts[1]);
    hymnBookRepository.delete(hymnBookId);

    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
