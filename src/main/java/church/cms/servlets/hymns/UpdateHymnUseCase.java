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

public class UpdateHymnUseCase {
  private final ObjectMapper objectMapper;
  private final HymnRepository hymnRepository;
  private final Logger logger;

  public UpdateHymnUseCase(ObjectMapper objectMapper, HymnRepository hymnRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.hymnRepository = hymnRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException,
          SQLException,
          IllegalArgumentException,
          StreamReadException,
          StreamWriteException,
          DatabindException {
    logger.info("start");

    UpdateHymnPayload payload = objectMapper.readValue(req.getReader(), UpdateHymnPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<UpdateHymnPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<UpdateHymnPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while updating a Hymn. Error message is: " + sb);
      }

      Integer hymnId = payload.hymnId();
      boolean exists = hymnRepository.exists(hymnId);
      if(!exists) {
        logger.error("The Hymn with hymnId {} doesn't exist.", hymnId);

        throw new IllegalArgumentException("Cannot update Hymn because the Hymn with Hymn ID " + hymnId + " doesn't exist.");
      }

      Hymn hymn = new Hymn(
              payload.hymnId(),
              payload.authorId(),
              payload.authorExtras(),
              payload.title(),
              payload.lyrics(),
              payload.hymnBookId(),
              payload.numberInHymnBook(),
              payload.topicId(),
              payload.labelId()
      );
      Hymn updatedHymn = hymnRepository.save(hymn);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(res.getWriter(), updatedHymn);

      logger.info("end");
    }

    logger.info("end");
  }

}
