package church.cms.servlets.labels;

import church.cms.context.AppDeps;
import church.cms.domain.Label;
import church.cms.repositories.LabelRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@DisplayName("Integration Test suite for all Label Use Cases")
public class LabelAllUseCasesIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static LabelRepository labelRepository;
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

    labelRepository = appDeps.getLabelRepository();
    labelRepository.createTableIfNotExists();

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "labelsServlet", new LabelsServlet());
    context.addServletMappingDecoded("/api/labels/*", "labelsServlet");

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
  public void ifLabelCreatedSuccessfully() throws SQLException {
    labelRepository.truncateTable();

    CreateLabelPayload payload = new CreateLabelPayload("Morning Service");

    given()
            .body(payload)
            .when()
            .post("/api/labels")
            .then()
            .assertThat()
            .statusCode(201)
            .and()
            .body("name", equalTo(payload.name()));
  }

  @Test
  public void ifItListsLabelsSuccessfully() throws SQLException {
    labelRepository.truncateTable();

    Label label1 = new Label("Morning Service");
    labelRepository.save(label1);
    Label label2 = new Label("Evening Service");
    labelRepository.save(label2);

    get("/api/labels")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("name", hasItems(label1.getName(), label2.getName()));
  }

  @Test
  public void ifItUpdatesLabelSuccessfully() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    labelRepository.save(label);

    String updatedTopicName = "Evening Service";
    UpdateLabelPayload payload = new UpdateLabelPayload(label.getLabelId(), updatedTopicName);

    given()
            .body(payload)
            .put("/api/labels")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("labelId", equalTo(label.getLabelId()), "name", equalTo(updatedTopicName));
  }

  @Test
  void ifItDeletesTopicSuccessfully() throws SQLException {
    labelRepository.truncateTable();

    Label label = new Label("Morning Service");
    Label createdTopic = labelRepository.save(label);

    given()
            .delete("/api/labels/" + createdTopic.getLabelId())
            .then()
            .assertThat()
            .statusCode(200);
  }
}
