package church.cms.servlets.topics;

import church.cms.repositories.TopicRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteTopicUseCase {
  private final TopicRepository topicRepository;
  private final Logger logger;

  public DeleteTopicUseCase(TopicRepository topicRepository, Logger logger) {
    this.topicRepository = topicRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, IllegalArgumentException,
          SQLException {
    logger.info("start");

    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      throw new IllegalArgumentException("Specify Topic ID when deleting a Topic.");
    }

    String[] pathParts = pathInfo.split("/");
    if (pathParts.length < 2) {
      throw new IllegalArgumentException("The Topic ID in not included in the URL path.");
    }

    Integer topicId = Integer.parseInt(pathParts[1]);
    boolean exists = topicRepository.exists(topicId);
    if (!exists) {
      logger.info("The Topic with topicId {} doesn't exist.", topicId);

      throw new IllegalArgumentException("The Topic with Topic ID " + topicId + " doesn't exist.");
    }

    topicRepository.delete(topicId);

    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
