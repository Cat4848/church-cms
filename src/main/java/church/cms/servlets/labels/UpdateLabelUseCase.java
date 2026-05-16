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

public class UpdateLabelUseCase {
  private final ObjectMapper objectMapper;
  private final LabelRepository labelRepository;
  private final Logger logger;

  public UpdateLabelUseCase(ObjectMapper objectMapper, LabelRepository labelRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.labelRepository = labelRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException,
          SQLException,
          IllegalArgumentException,
          StreamReadException,
          StreamWriteException,
          DatabindException {
    logger.info("start");

    UpdateLabelPayload payload = objectMapper.readValue(req.getReader(), UpdateLabelPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<UpdateLabelPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<UpdateLabelPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while updating a Label. Error message is: " + sb);
      }

      Label label = new Label(payload.labelId(), payload.name());
      Label updatedLabel = labelRepository.save(label);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(res.getWriter(), updatedLabel);

      logger.info("end");
    }

    logger.info("end");
  }

}
