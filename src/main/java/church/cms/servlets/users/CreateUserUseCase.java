package church.cms.servlets.users;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import church.cms.repositories.Repository;
import church.cms.utils.PasswordUtil;
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

public class CreateUserUseCase {
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final Repository<User> userRepository;
  private final Logger logger;

  public CreateUserUseCase(
          ObjectMapper objectMapper,
          PasswordUtil passwordUtil,
          Repository<User> userRepository,
          Logger logger) {
    this.objectMapper = objectMapper;
    this.passwordUtil = passwordUtil;
    this.userRepository = userRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws
          IOException,
          InvalidEntityException,
          SQLException {
    logger.info("start");

    CreateUserPayload payload = objectMapper.readValue(req.getReader(), CreateUserPayload.class);

    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = validatorFactory.getValidator();
      Set<ConstraintViolation<CreateUserPayload>> violations = validator.validate(payload);

      if (!violations.isEmpty()) {
        int size = violations.size();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<CreateUserPayload> violation : violations) {
          sb.append(violation.getMessage());
          if (i < size - 1) {
            sb.append(",");
          }
          i++;
        }

        logger.info("payload validation failed with message: {}", sb);
        logger.info("throwing IllegalArgumentException");

        throw new IllegalArgumentException("An error occurred while creating a new user. Error message is: " + sb);
      }

      User user = new User(
              payload.firstName(),
              payload.lastName(),
              payload.email(),
              payload.password(),
              payload.isAdmin()
      );

      user.setPassword(passwordUtil.hashPassword(user.getPassword()));

      User createdUser = userRepository.save(user);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_CREATED);
      objectMapper.writeValue(res.getWriter(), createdUser);

      logger.info("end");
    }
  }
}
