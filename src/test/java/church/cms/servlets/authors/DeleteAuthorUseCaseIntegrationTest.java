package church.cms.servlets.authors;

import church.cms.context.AppDeps;
import church.cms.domain.Author;
import church.cms.repositories.AuthorRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@DisplayName("Integration Test suite for Delete Author Use Case")
public class DeleteAuthorUseCaseIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static Tomcat tomcat;
  public static int port;
  public static Author createdAuthor;

  @BeforeAll
  public static void beforeAll() throws SQLException, LifecycleException {
    mySQLContainer.start();

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(mySQLContainer.getJdbcUrl());
    config.setUsername(mySQLContainer.getUsername());
    config.setPassword(mySQLContainer.getPassword());
    DataSource dataSource = new HikariDataSource(config);

    AppDeps appDeps = new AppDeps(dataSource);

    AuthorRepository authorRepository = appDeps.getAuthorRepository();
    authorRepository.createTableIfNotExists();

    Author newAuthor = new Author("James Bond");
    createdAuthor = authorRepository.save(newAuthor);

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "authorsServlet", new AuthorsServlet());
    context.addServletMappingDecoded("/api/authors/*", "authorsServlet");

    tomcat.start();
    port = tomcat.getConnector().getLocalPort();
  }

  @AfterAll
  public static void afterAll() throws LifecycleException {
    mySQLContainer.stop();
    tomcat.stop();
  }

  @BeforeEach
  public void setUp() {
    baseURI = "http://localhost:" + port;
  }

  @Test
  public void ifAuthorDeletedSuccessfully() {
    given()
            .when()
            .delete("/api/authors/" + createdAuthor.getAuthorId())
            .then()
            .assertThat()
            .statusCode(200);
  }
}
