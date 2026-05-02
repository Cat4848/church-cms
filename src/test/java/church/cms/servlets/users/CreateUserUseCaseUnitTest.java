package church.cms.servlets.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import church.cms.repositories.Repository;
import church.cms.utils.PasswordUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


@DisplayName("Test suite for the CreateUserUseCaseTest")
public class CreateUserUseCaseUnitTest {
  @Test
  void ifUserIsCreatedSuccessfully() throws IOException, SQLException, InvalidEntityException {
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);

    User user = new User("John", "Lawrence", "j.lawrence@gmail.com", "pass123", true);
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.readValue(req.getInputStream(), User.class)).thenReturn(user);

    PasswordUtil passwordUtil = mock(PasswordUtil.class);
    when(passwordUtil.hashPassword(user.getPassword())).thenReturn("hashed-password");

    Repository<User> userRepository = mock(Repository.class);

    Logger logger = mock(Logger.class);

    CreateUserUseCase createUserUseCase = new CreateUserUseCase(objectMapper, passwordUtil, userRepository, logger);
    createUserUseCase.execute(req, res);

    verify(userRepository, times(1)).save(user);
  }

}