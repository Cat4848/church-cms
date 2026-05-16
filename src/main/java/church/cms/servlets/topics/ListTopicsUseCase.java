package church.cms.servlets.topics;

import church.cms.domain.Topic;
import church.cms.repositories.TopicRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListTopicsUseCase {
  private final ObjectMapper objectMapper;
  private final TopicRepository topicRepository;
  private final Logger logger;

  public ListTopicsUseCase(ObjectMapper objectMapper, TopicRepository topicRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.topicRepository = topicRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException,
          StreamWriteException {
    logger.info("start");

    List<Topic> topics = topicRepository.list();

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(res.getWriter(), topics);

    logger.info("end");
  }

}
