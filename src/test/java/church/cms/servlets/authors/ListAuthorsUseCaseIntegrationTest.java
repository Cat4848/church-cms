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

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasItems;

@DisplayName("Integration Test suite for Create Author Use Case")
public class ListAuthorsUseCaseIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static Tomcat tomcat;
  public static int port;
  public static Author author1 = new Author("Josh Black");
  public static Author author2 = new Author("Josh White");

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
    authorRepository.save(author1);
    authorRepository.save(author2);

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "authorsServlet", new AuthorsServlet());
    context.addServletMappingDecoded("/api/authors", "authorsServlet");

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
  public void ifAuthorCreatedSuccessfully() {
    get("/api/authors")
            .then()
            .assertThat()
            .statusCode(200)
            .body("name", hasItems(author1.getName(), author2.getName()));
  }
}
