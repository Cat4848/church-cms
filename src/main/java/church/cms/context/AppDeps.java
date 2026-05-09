package church.cms.context;

import church.cms.repositories.AuthorRepository;
import church.cms.repositories.UserRepository;
import church.cms.servlets.authors.CreateAuthorUseCase;
import church.cms.servlets.authors.ListAuthorsUseCase;
import church.cms.servlets.authors.UpdateAuthorUseCase;
import church.cms.servlets.login.LoginUseCase;
import church.cms.servlets.users.CreateUserUseCase;
import church.cms.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AppDeps {
  private final UserRepository userRepository;
  private final AuthorRepository authorRepository;
  private final CreateUserUseCase createUserUseCase;
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;
  private final LoginUseCase loginUseCase;
  private final CreateAuthorUseCase createAuthorUseCase;
  private final ListAuthorsUseCase listAuthorsUseCase;
  private final UpdateAuthorUseCase updateAuthorUseCase;

  public AppDeps(DataSource dataSource) throws SQLException {
    this.userRepository = new UserRepository(dataSource, LoggerFactory.getLogger(UserRepository.class));
    this.authorRepository = new AuthorRepository(dataSource, LoggerFactory.getLogger(AuthorRepository.class));
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
    this.createAuthorUseCase = new CreateAuthorUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            CreateAuthorUseCase.class));
    this.listAuthorsUseCase = new ListAuthorsUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            ListAuthorsUseCase.class));
    this.updateAuthorUseCase = new UpdateAuthorUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            UpdateAuthorUseCase.class));
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public AuthorRepository getAuthorRepository() {
    return authorRepository;
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

  public CreateAuthorUseCase getCreateAuthorUseCase() {
    return createAuthorUseCase;
  }

  public ListAuthorsUseCase getListAuthorsUseCase() {
    return listAuthorsUseCase;
  }

  public UpdateAuthorUseCase getUpdateAuthorUseCase() {
    return updateAuthorUseCase;
  }
}
