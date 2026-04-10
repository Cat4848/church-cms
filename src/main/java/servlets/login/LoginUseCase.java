package servlets.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import exceptions.InvalidEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.UserRepository;
import utils.PasswordUtil;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;

public class LoginUseCase {
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final UserRepository userRepository;

  public LoginUseCase(ObjectMapper objectMapper, PasswordUtil passwordUtil, UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.passwordUtil = passwordUtil;
    this.userRepository = userRepository;
  }

  // TODO validation on body
  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, InvalidEntityException, SQLException {
    HttpSession session = req.getSession(false);
    // in case the user already in logged in
    if (session != null) {
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    User attemptingUser = objectMapper.readValue(req.getInputStream(), User.class);

    if (attemptingUser.getEmail() == null && attemptingUser.getPassword() == null) {
      throw new InvalidEntityException("User email or password missing");
    }

    User existingUser = userRepository.retrieveByEmail(attemptingUser.getEmail());

    if (passwordUtil.checkPassword(attemptingUser.getPassword(), existingUser.getPassword())) {
      HttpSession newSession = req.getSession(true);
      newSession.setAttribute("userId", existingUser.getUserId());
      res.setStatus(HttpServletResponse.SC_OK);
    } else {
      throw new InvalidParameterException("Incorrect password");
    }
  }
}
