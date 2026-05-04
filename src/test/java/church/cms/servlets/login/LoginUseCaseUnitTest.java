package church.cms.servlets.login;

import church.cms.servlets.login.LoginUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import church.cms.repositories.UserRepository;
import church.cms.utils.PasswordUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@DisplayName("Unit test suite for the LoginUseCase")
public class LoginUseCaseUnitTest {

  @Test
  public void ifTheUserIsLoggedInSuccessfully() throws IOException, InvalidEntityException, SQLException {
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);

    HttpSession session = mock(HttpSession.class);
    when(req.getSession(true)).thenReturn(session);

    User attemptingUser = new User("John", "Lawrence", "j.lawrence@gmail.com", "pass123", true);
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.readValue(req.getInputStream(), User.class)).thenReturn(attemptingUser);

    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.retrieveByEmail(attemptingUser.getEmail())).thenReturn(attemptingUser);

    PasswordUtil passwordUtil = mock(PasswordUtil.class);
    when(passwordUtil.checkPassword(attemptingUser.getPassword(), attemptingUser.getPassword())).thenReturn(true);

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
