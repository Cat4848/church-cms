package church.cms.servlets.login;

import church.cms.servlets.login.LoginServlet;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import church.cms.context.AppDeps;
import church.cms.domain.User;
import church.cms.exceptions.InvalidEntityException;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import church.cms.repositories.UserRepository;
import church.cms.utils.PasswordUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Integration test suite for the LoginUseCase")
public class LoginUseCaseIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static Tomcat tomcat;
  public static AppDeps deps;
  public static int port;

  @BeforeAll
  public static void beforeAll() throws SQLException, LifecycleException {
    mySQLContainer.start();

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(mySQLContainer.getJdbcUrl());
    config.setUsername(mySQLContainer.getUsername());
    config.setPassword(mySQLContainer.getPassword());
    DataSource dataSource = new HikariDataSource(config);

    deps = new AppDeps(dataSource);

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", deps);

    Tomcat.addServlet(context, "loginServlet", new LoginServlet());
    context.addServletMappingDecoded("/auth/login", "loginServlet");

    tomcat.start();
    port = tomcat.getConnector().getLocalPort();
  }

  @AfterAll
  public static void afterAll() throws LifecycleException {
    tomcat.stop();
    mySQLContainer.stop();
  }

  @BeforeEach
  public void setUp() {
    baseURI = "http://localhost:" + port;
  }

  @Test
  public void ifTheUserIsLoggedInSuccessfully() throws SQLException, InvalidEntityException {
    User seedUser = new User("Head", "Lawrence", "j.lawrence@gmail.com", "pass12345", false);
    LoginUserPayload loginPayload = new LoginUserPayload("j.lawrence@gmail.com", "pass12345");

    UserRepository userRepository = deps.getUserRepository();
    userRepository.createTableIfNotExists();

    PasswordUtil passwordUtil = deps.getPasswordUtil();
    seedUser.setPassword(passwordUtil.hashPassword(seedUser.getPassword()));

    userRepository.save(seedUser);

    given()
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .assertThat()
            .statusCode(200)
            .header("X-CSRF-TOKEN", not(emptyString()));
  }
}
