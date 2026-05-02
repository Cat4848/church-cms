package church.cms.servlets.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import church.cms.repositories.UserRepository;
import church.cms.utils.PasswordUtil;
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

  public LoginUseCase(ObjectMapper objectMapper, PasswordUtil passwordUtil, UserRepository userRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.passwordUtil = passwordUtil;
    this.userRepository = userRepository;
    this.logger = logger;
  }

  // TODO validation on body
  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, InvalidEntityException, SQLException {
    logger.info("start");

    HttpSession session = req.getSession(false);
    // in case the user is already logged in
    if (session != null) {
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    User attemptingUser = objectMapper.readValue(req.getInputStream(), User.class);

    logger.info("attemptingUserId {}", attemptingUser.getUserId());

    if (attemptingUser.getEmail() == null && attemptingUser.getPassword() == null) {
      throw new InvalidEntityException("User email or password missing");
    }

    User existingUser = userRepository.retrieveByEmail(attemptingUser.getEmail());

    if (passwordUtil.checkPassword(attemptingUser.getPassword(), existingUser.getPassword())) {
      logger.info("correct password: logging in");

      HttpSession newSession = req.getSession(true);
      newSession.setAttribute("userId", existingUser.getUserId());

      // set the CSRF token
      String token = UUID.randomUUID().toString();
      newSession.setAttribute("csrfToken", token);

      res.setHeader("X-CSRF-TOKEN", token);
      res.setStatus(HttpServletResponse.SC_OK);

      logger.info("end");
    } else {
      throw new InvalidParameterException("Incorrect password");
    }
  }
}
