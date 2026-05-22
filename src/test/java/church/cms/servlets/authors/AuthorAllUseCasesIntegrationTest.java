package church.cms.servlets.authors;

import church.cms.context.AppDeps;
import church.cms.domain.Author;
import church.cms.repositories.AuthorRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Integration Test suite for all Author Use Case")
public class AuthorAllUseCasesIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static AuthorRepository authorRepository;
  public static Tomcat tomcat;
  public static int port;

  @BeforeAll
  public static void beforeAll() throws SQLException, LifecycleException {
    mySQLContainer.start();

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(mySQLContainer.getJdbcUrl());
    config.setUsername(mySQLContainer.getUsername());
    config.setPassword(mySQLContainer.getPassword());
    DataSource dataSource = new HikariDataSource(config);

    AppDeps appDeps = new AppDeps(dataSource);

    authorRepository = appDeps.getAuthorRepository();
    authorRepository.createTableIfNotExists();

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
  public void ifAuthorCreatedSuccessfully() throws SQLException {
    authorRepository.truncateTable();

    CreateAuthorPayload payload = new CreateAuthorPayload("John Newton");

    String name = given()
            .body(payload)
            .when()
            .post("/api/authors")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_CREATED)
            .extract()
            .path("name");
    assertEquals(payload.name(), name);
  }

  @Test
  public void ifItListsAuthorsSuccessfully() throws SQLException {
    authorRepository.truncateTable();

    Author author1 = new Author("William Knox");
    authorRepository.save(author1);
    Author author2 = new Author("James Green");
    authorRepository.save(author2);

    get("/api/authors")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("name", hasItems(author1.getName(), author2.getName()));
  }

  @Test
  public void ifItUpdatesAuthorSuccessfully() throws SQLException {
    authorRepository.truncateTable();

    Author author = new Author("William Knox");
    authorRepository.save(author);

    String updatedAuthorName = "Grace and Hope";
    UpdateAuthorPayload payload = new UpdateAuthorPayload(author.getAuthorId(), updatedAuthorName);

    given()
            .body(payload)
            .put("/api/authors")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("authorId", equalTo(author.getAuthorId()), "name", equalTo(updatedAuthorName));
  }

  @Test void ifErrorsWhenUpdatingUnexistingAuthor() throws SQLException{
    authorRepository.truncateTable();

    UpdateAuthorPayload payload = new UpdateAuthorPayload(1, "John Newman");

    given()
            .body(payload)
            .put("/api/authors")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  void ifItDeletesAuthorSuccessfully() throws SQLException {
    authorRepository.truncateTable();

    Author author = new Author("John Newton");
    Author createdAuthor = authorRepository.save(author);

    given()
            .delete("/api/authors/" + createdAuthor.getAuthorId())
            .then()
            .assertThat()
            .statusCode(200);
  }
}
