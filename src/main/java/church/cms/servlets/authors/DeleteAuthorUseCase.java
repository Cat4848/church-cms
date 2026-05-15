package church.cms.servlets.authors;

import church.cms.repositories.AuthorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteAuthorUseCase {
  private final AuthorRepository authorRepository;
  private final Logger logger;

  public DeleteAuthorUseCase(AuthorRepository authorRepository, Logger logger) {
    this.authorRepository = authorRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, IllegalArgumentException,
          SQLException {
    logger.info("start");

    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      throw new IllegalArgumentException("Specify author ID when deleting an author.");
    }

    String[] pathParts = pathInfo.split("/");
    if (pathParts.length < 2) {
      throw new IllegalArgumentException("The author ID in not included in the URL path.");
    }

    Integer authorId = Integer.parseInt(pathParts[1]);

    boolean exists = authorRepository.exists(authorId);
    if (!exists) {
      logger.info("The author with authorId {} doesn't exist.", authorId);

      throw new IllegalArgumentException("The author with author ID " + authorId + " doesn't exist.");
    }

    authorRepository.delete(authorId);

    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
