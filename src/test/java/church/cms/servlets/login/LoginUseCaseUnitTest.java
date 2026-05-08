package church.cms.servlets.login;

import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import church.cms.repositories.UserRepository;
import church.cms.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@DisplayName("Unit test suite for the LoginUseCase")
public class LoginUseCaseUnitTest {

  @Test
  public void ifTheUserIsLoggedInSuccessfully() throws IOException, InvalidEntityException, SQLException {
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);

    HttpSession session = mock(HttpSession.class);
    when(req.getSession(true)).thenReturn(session);

    User existingUser = new User(1, "John", "Lawrence", "j.lawrence@gmail.com", "pass12345", true);
    LoginUserPayload loginPayload = new LoginUserPayload("j.lawrence@gmail.com", "pass12345");

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.readValue(req.getReader(), LoginUserPayload.class)).thenReturn(loginPayload);

    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.retrieveByEmail(existingUser.getEmail())).thenReturn(existingUser);

    PasswordUtil passwordUtil = mock(PasswordUtil.class);
    when(passwordUtil.checkPassword(existingUser.getPassword(), loginPayload.password())).thenReturn(true);

    Logger logger = mock(Logger.class);

    LoginUseCase loginUseCase = new LoginUseCase(objectMapper, passwordUtil, userRepository, logger);
    loginUseCase.execute(req, res);

    verify(res, times(1)).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void ifResponseNotModifiedIsReturnedIfUserHasSessionAlready()
          throws IOException, InvalidEntityException, SQLException {
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);

    HttpSession session = mock(HttpSession.class);
    when(req.getSession(false)).thenReturn(session);

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    PasswordUtil passwordUtil = mock(PasswordUtil.class);
    UserRepository userRepository = mock(UserRepository.class);
    Logger logger = mock(Logger.class);

    LoginUseCase loginUseCase = new LoginUseCase(objectMapper, passwordUtil, userRepository, logger);
    loginUseCase.execute(req, res);

    verify(res, times(1)).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
  }
}
