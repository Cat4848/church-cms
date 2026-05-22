package church.cms.servlets.hymnBooks;

import church.cms.domain.HymnBook;
import church.cms.repositories.HymnBookRepository;
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

public class UpdateHymnBookUseCase {
  private final ObjectMapper objectMapper;
  private final HymnBookRepository hymnBookRepository;
  private final Logger logger;

  public UpdateHymnBookUseCase(ObjectMapper objectMapper, HymnBookRepository hymnBookRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.hymnBookRepository = hymnBookRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException,
                                                                              SQLException,
                                                                              IllegalArgumentException,
                                                                              StreamReadException,
                                                                              StreamWriteException,
                                                                              DatabindException {
    logger.info("start");

    UpdateHymnBookPayload payload = objectMapper.readValue(req.getReader(), UpdateHymnBookPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<UpdateHymnBookPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<UpdateHymnBookPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while updating a hymn book. Error message is: " + sb);
      }

      Integer hymnBookId = payload.hymnBookId();
      boolean exists = hymnBookRepository.exists(hymnBookId);
      if (!exists) {
        logger.error("The Hymn Book with hymnBookId {} doesn't exist.", hymnBookId);

        throw new IllegalArgumentException("Cannot update Hymn Book because the Hymn Book with Hymn Book ID " + hymnBookId + " doesn't exist.");
      }

      HymnBook hymnBook = new HymnBook(hymnBookId, payload.name());
      HymnBook updatedHymnBook = hymnBookRepository.save(hymnBook);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(res.getWriter(), updatedHymnBook);

      logger.info("end");
    }

    logger.info("end");
  }

}
