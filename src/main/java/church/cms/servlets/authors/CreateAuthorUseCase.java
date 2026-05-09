package church.cms.servlets.authors;

import church.cms.domain.Author;
import church.cms.repositories.AuthorRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
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

public class CreateAuthorUseCase {
  private final ObjectMapper objectMapper;
  private final AuthorRepository authorRepository;
  private final Logger logger;

  public CreateAuthorUseCase(ObjectMapper objectMapper, AuthorRepository authorRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.authorRepository = authorRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws
          IOException,
          SQLException,
          IllegalArgumentException,
          StreamReadException,
          DatabindException {
    logger.info("start");

    CreateAuthorPayload payload = objectMapper.readValue(req.getReader(), CreateAuthorPayload.class);

    try (ValidatorFactory vf = Validation.buildDefaultValidatorFactory()) {
      Validator validator = vf.getValidator();
      Set<ConstraintViolation<CreateAuthorPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<CreateAuthorPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while creating a new author. Error message is: " + sb);
      }

      Author author = new Author(payload.name());
      Author createdAuthor = authorRepository.save(author);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_CREATED);
      objectMapper.writeValue(res.getWriter(), createdAuthor);

      logger.info("end");
    }
  }

}
