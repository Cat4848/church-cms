package church.cms.servlets.labels;

import church.cms.domain.Label;
import church.cms.repositories.LabelRepository;
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

public class CreateLabelUseCase {
  private final ObjectMapper objectMapper;
  private final LabelRepository labelRepository;
  private final Logger logger;

  public CreateLabelUseCase(ObjectMapper objectMapper, LabelRepository labelRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.labelRepository = labelRepository;
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

    CreateLabelPayload payload = objectMapper.readValue(req.getReader(), CreateLabelPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<CreateLabelPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<CreateLabelPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while creating a new Label. Error message is: " + sb);
      }

      Label label = new Label(payload.name());
      Label createdLabel = labelRepository.save(label);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_CREATED);
      objectMapper.writeValue(res.getWriter(), createdLabel);

      logger.info("end");
    }
  }
}
