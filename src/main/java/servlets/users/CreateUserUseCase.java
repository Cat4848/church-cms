package servlets.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import exceptions.InvalidEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.Repository;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;

public class CreateUserUseCase {
  ObjectMapper objectMapper;
  PasswordUtil passwordUtil;
  Repository<User> userRepository;

  public CreateUserUseCase(ObjectMapper objectMapper, PasswordUtil passwordUtil, Repository<User> userRepository) {
    this.objectMapper = objectMapper;
    this.passwordUtil = passwordUtil;
    this.userRepository = userRepository;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException, InvalidEntityException, SQLException {
    // TODO validate user object
    User user = objectMapper.readValue(req.getInputStream(), User.class);
    user.setPassword(passwordUtil.hashPassword(user.getPassword()));

    User createdUser = userRepository.save(user);

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_CREATED);
    objectMapper.writeValue(res.getOutputStream(), createdUser);
  }
}
