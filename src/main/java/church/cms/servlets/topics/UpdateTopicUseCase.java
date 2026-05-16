package church.cms.servlets.topics;

import church.cms.domain.Topic;
import church.cms.repositories.TopicRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class UpdateTopicUseCase {
  private final ObjectMapper objectMapper;
  private final TopicRepository topicRepository;
  private final Logger logger;

  public UpdateTopicUseCase(ObjectMapper objectMapper, TopicRepository topicRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.topicRepository = topicRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException,
          SQLException,
          IllegalArgumentException,
          StreamReadException,
          StreamWriteException,
          DatabindException {
    logger.info("start");

    UpdateTopicPayload payload = objectMapper.readValue(req.getReader(), UpdateTopicPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<UpdateTopicPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<UpdateTopicPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while updating a Topic. Error message is: " + sb);
      }

      Topic topic = new Topic(payload.topicId(), payload.name());
      Topic updatedTopic = topicRepository.save(topic);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(res.getWriter(), updatedTopic);

      logger.info("end");
    }

    logger.info("end");
  }

}
