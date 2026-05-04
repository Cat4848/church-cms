package church.cms.servlets.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import church.cms.repositories.Repository;
import church.cms.utils.PasswordUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class CreateUserUseCase {
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final Repository<User> userRepository;
  private final Logger logger;

  public CreateUserUseCase(ObjectMapper objectMapper, PasswordUtil passwordUtil, Repository<User> userRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.passwordUtil = passwordUtil;
    this.userRepository = userRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, InvalidEntityException, SQLException {
    logger.info("start");

    // TODO validate user object
    User user = objectMapper.readValue(req.getInputStream(), User.class);
    user.setPassword(passwordUtil.hashPassword(user.getPassword()));

    User createdUser = userRepository.save(user);

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_CREATED);
    objectMapper.writeValue(res.getWriter(), createdUser);

    logger.info("end");
  }
}
