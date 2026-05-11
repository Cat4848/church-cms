package church.cms.context;

import church.cms.repositories.AuthorRepository;
import church.cms.repositories.HymnBookRepository;
import church.cms.repositories.UserRepository;
import church.cms.servlets.authors.CreateAuthorUseCase;
import church.cms.servlets.authors.DeleteAuthorUseCase;
import church.cms.servlets.authors.ListAuthorsUseCase;
import church.cms.servlets.authors.UpdateAuthorUseCase;
import church.cms.servlets.hymnBooks.CreateHymnBookUseCase;
import church.cms.servlets.hymnBooks.DeleteHymnBookUseCase;
import church.cms.servlets.hymnBooks.ListHymnBooksUseCase;
import church.cms.servlets.hymnBooks.UpdateHymnBookUseCase;
import church.cms.servlets.login.LoginUseCase;
import church.cms.servlets.users.CreateUserUseCase;
import church.cms.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AppDeps {
  private final ObjectMapper objectMapper;
  private final PasswordUtil passwordUtil;

  private final UserRepository userRepository;
  private final AuthorRepository authorRepository;
  private final HymnBookRepository hymnBookRepository;

  private final LoginUseCase loginUseCase;


  private final CreateUserUseCase createUserUseCase;

  private final CreateAuthorUseCase createAuthorUseCase;
  private final ListAuthorsUseCase listAuthorsUseCase;
  private final UpdateAuthorUseCase updateAuthorUseCase;
  private final DeleteAuthorUseCase deleteAuthorUseCase;

  private final CreateHymnBookUseCase createHymnBookUseCase;
  private final ListHymnBooksUseCase listHymnBooksUseCase;
  private final UpdateHymnBookUseCase updateHymnBookUseCase;
  private final DeleteHymnBookUseCase deleteHymnBookUseCase;

  public AppDeps(DataSource dataSource) throws SQLException {
    this.objectMapper = new ObjectMapper();
    this.passwordUtil = new PasswordUtil();

    this.userRepository = new UserRepository(dataSource, LoggerFactory.getLogger(UserRepository.class));
    this.authorRepository = new AuthorRepository(dataSource, LoggerFactory.getLogger(AuthorRepository.class));
    this.hymnBookRepository = new HymnBookRepository(dataSource, LoggerFactory.getLogger(HymnBookRepository.class));

    this.loginUseCase = new LoginUseCase(objectMapper,
                                         passwordUtil,
                                         userRepository,
                                         LoggerFactory.getLogger(LoginUseCase.class));

    this.createUserUseCase = new CreateUserUseCase(objectMapper,
                                                   passwordUtil,
                                                   userRepository,
                                                   LoggerFactory.getLogger(CreateUserUseCase.class));
    this.createAuthorUseCase = new CreateAuthorUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            CreateAuthorUseCase.class));
    this.listAuthorsUseCase = new ListAuthorsUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            ListAuthorsUseCase.class));
    this.updateAuthorUseCase = new UpdateAuthorUseCase(objectMapper, authorRepository, LoggerFactory.getLogger(
            UpdateAuthorUseCase.class));
    this.deleteAuthorUseCase = new DeleteAuthorUseCase(authorRepository,
                                                       LoggerFactory.getLogger(DeleteAuthorUseCase.class));

    this.createHymnBookUseCase = new CreateHymnBookUseCase(objectMapper, hymnBookRepository, LoggerFactory.getLogger(
            CreateHymnBookUseCase.class));
    this.listHymnBooksUseCase = new ListHymnBooksUseCase(objectMapper, hymnBookRepository, LoggerFactory.getLogger(
            ListHymnBooksUseCase.class));
    this.updateHymnBookUseCase = new UpdateHymnBookUseCase(objectMapper, hymnBookRepository, LoggerFactory.getLogger(
            UpdateHymnBookUseCase.class));
    this.deleteHymnBookUseCase = new DeleteHymnBookUseCase(hymnBookRepository, LoggerFactory.getLogger(
            DeleteHymnBookUseCase.class));
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

  public DeleteAuthorUseCase getDeleteAuthorUseCase() {
    return deleteAuthorUseCase;
  }

  public HymnBookRepository getHymnBookRepository() {
    return hymnBookRepository;
  }

  public CreateHymnBookUseCase getCreateHymnBookUseCase() {
    return createHymnBookUseCase;
  }

  public ListHymnBooksUseCase getListHymnBooksUseCase() {
    return listHymnBooksUseCase;
  }

  public UpdateHymnBookUseCase getUpdateHymnBookUseCase() {
    return updateHymnBookUseCase;
  }

  public DeleteHymnBookUseCase getDeleteHymnBookUseCase() {
    return deleteHymnBookUseCase;
  }
}
