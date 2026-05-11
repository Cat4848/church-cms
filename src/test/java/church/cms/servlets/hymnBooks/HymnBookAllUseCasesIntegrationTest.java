package church.cms.servlets.hymnBooks;

import church.cms.context.AppDeps;
import church.cms.domain.HymnBook;
import church.cms.repositories.HymnBookRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

@DisplayName("Integration Test suite for all Hymn Book Use Cases")
public class HymnBookAllUseCasesIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static HymnBookRepository hymnBookRepository;
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

    hymnBookRepository = appDeps.getHymnBookRepository();
    hymnBookRepository.createTableIfNotExists();

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "hymnBooksServlet", new HymnBooksServlet());
    context.addServletMappingDecoded("/api/hymn-books/*", "hymnBooksServlet");

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
  public void ifHymnBookCreatedSuccessfully() throws SQLException {
    hymnBookRepository.truncateTable();

    CreateHymnBookPayload payload = new CreateHymnBookPayload("Christian Hymns");

    String name = given()
            .body(payload)
            .when()
            .post("/api/hymn-books")
            .then().statusCode(201)
            .extract()
            .path("name");
    assertEquals(payload.name(), name);
  }

  @Test
  public void ifItListsHymnBooksSuccessfully() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook1 = new HymnBook("Christian Faith");
    hymnBookRepository.save(hymnBook1);
    HymnBook hymnBook2 = new HymnBook("Red Book");
    hymnBookRepository.save(hymnBook2);

    get("/api/hymn-books")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("name", hasItems(hymnBook1.getName(), hymnBook2.getName()));
  }

  @Test
  public void ifItUpdatesHymnBookSuccessfully() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    hymnBookRepository.save(hymnBook);

    String updatedHymnBookName = "Grace and Hope";
    UpdateHymnBookPayload payload = new UpdateHymnBookPayload(hymnBook.getHymnBookId(), updatedHymnBookName);

    given()
            .body(payload)
            .put("/api/hymn-books")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("hymnBookId", equalTo(hymnBook.getHymnBookId()), "name", equalTo(updatedHymnBookName));
  }

  @Test
  void ifItDeletesHymnBookSuccessfully() throws SQLException {
    hymnBookRepository.truncateTable();

    HymnBook hymnBook = new HymnBook("Christian Faith");
    HymnBook createdHymnBook = hymnBookRepository.save(hymnBook);

    given()
            .delete("/api/hymn-books/" + createdHymnBook.getHymnBookId())
            .then()
            .assertThat()
            .statusCode(200);
  }
}
