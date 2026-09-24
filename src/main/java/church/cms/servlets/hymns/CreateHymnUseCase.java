package church.cms.servlets.hymns;

import church.cms.domain.Hymn;
import church.cms.repositories.HymnRepository;
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

public class CreateHymnUseCase {
  private final ObjectMapper objectMapper;
  private final HymnRepository hymnRepository;
  private final Logger logger;

  public CreateHymnUseCase(ObjectMapper objectMapper, HymnRepository hymnRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.hymnRepository = hymnRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws
          IOException,
          SQLException,
          IllegalArgumentException,
          StreamReadException,
          StreamWriteException,
          DatabindException {
    logger.info("start");

    CreateHymnPayload payload = objectMapper.readValue(req.getReader(), CreateHymnPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<CreateHymnPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<CreateHymnPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while creating a new Hymn. Error message is: " + sb);
      }

      logger.debug("create hymn payload = {}", payload.toString());

      Hymn hymn = new Hymn(
              payload.authorId(),
              payload.authorExtras(),
              payload.title(),
              payload.lyrics(),
              payload.hymnBookId(),
              payload.numberInHymnBook(),
              payload.topicId(),
              payload.labelId()
      );
      Hymn createdHymn = hymnRepository.save(hymn);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_CREATED);
      objectMapper.writeValue(res.getWriter(), createdHymn);

      logger.info("end");
    }
  }
}
