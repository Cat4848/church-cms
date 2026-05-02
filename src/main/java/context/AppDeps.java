package context;

import com.fasterxml.jackson.databind.ObjectMapper;
import repositories.UserRepository;
import servlets.login.LoginUseCase;
import servlets.users.CreateUserUseCase;
import utils.PasswordUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AppDeps {
  private final UserRepository userRepository;
  private final CreateUserUseCase createUserUseCase;
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final LoginUseCase loginUseCase;

  public AppDeps(DataSource dataSource) throws SQLException {
    this.userRepository = new UserRepository(dataSource);
    this.objectMapper = new ObjectMapper();
    this.passwordUtil = new PasswordUtil();
    this.createUserUseCase = new CreateUserUseCase(objectMapper, passwordUtil, userRepository);
    this.loginUseCase = new LoginUseCase(objectMapper, passwordUtil, userRepository);
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public CreateUserUseCase getCreateUserUseCase() {
    return createUserUseCase;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public PasswordUtil getPasswordUtil() {
    return passwordUtil;
  }

  public LoginUseCase getLoginUseCase() {
    return loginUseCase;
  }
}
