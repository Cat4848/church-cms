package church.cms.servlets.login;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import church.cms.repositories.UserRepository;
import church.cms.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.UUID;

public class LoginUseCase {
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final UserRepository userRepository;
  private final Logger logger;

  public LoginUseCase(
          ObjectMapper objectMapper,
          PasswordUtil passwordUtil,
          UserRepository userRepository,
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

    HttpSession session = req.getSession(false);
    // in case the user is already logged in
    if (session != null) {
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    LoginUserPayload loginAttempt = objectMapper.readValue(req.getReader(), LoginUserPayload.class);

    logger.info("login attempt payload {}", loginAttempt);

    if (loginAttempt.email() == null || loginAttempt.password() == null) {
      logger.info("login validation failed; throwing IllegalArgumentException");

      throw new IllegalArgumentException("User email or password missing");
    }

    User existingUser = userRepository.retrieveByEmail(loginAttempt.email());

    if (passwordUtil.checkPassword(loginAttempt.password(), existingUser.getPassword())) {
      logger.info("correct password: logging in");

      HttpSession newSession = req.getSession(true);
      newSession.setAttribute("userId", existingUser.getUserId());

      // set the CSRF token
      String token = UUID.randomUUID().toString();
      newSession.setAttribute("csrfToken", token);

      res.setHeader("X-CSRF-TOKEN", token);

      LoginSuccessResponsePayload resPayload = new LoginSuccessResponsePayload(
              existingUser.getFirstName(),
              existingUser.getLastName(),
              existingUser.getEmail(),
              existingUser.getIsAdmin()
      );
      objectMapper.writeValue(res.getWriter(), resPayload);

      res.setContentType("application/json");
      res.setStatus(HttpServletResponse.SC_OK);

      logger.info("end");
    } else {
      throw new InvalidParameterException("Incorrect password");
    }
  }
}
