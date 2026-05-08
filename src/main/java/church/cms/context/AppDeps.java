package church.cms.context;

import church.cms.repositories.UserRepository;
import church.cms.servlets.login.LoginUseCase;
import church.cms.servlets.users.CreateUserUseCase;
import church.cms.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AppDeps {
  private final UserRepository userRepository;
  private final CreateUserUseCase createUserUseCase;
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final LoginUseCase loginUseCase;

  public AppDeps(DataSource dataSource) throws SQLException {
    this.userRepository = new UserRepository(dataSource, LoggerFactory.getLogger(UserRepository.class));
    this.objectMapper = new ObjectMapper();
    this.passwordUtil = new PasswordUtil();
    this.createUserUseCase = new CreateUserUseCase(objectMapper,
                                                   passwordUtil,
                                                   userRepository,
                                                   LoggerFactory.getLogger(CreateUserUseCase.class));
    this.loginUseCase = new LoginUseCase(objectMapper,
                                         passwordUtil,
                                         userRepository,
                                         LoggerFactory.getLogger(LoginUseCase.class));
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
