package church.cms.servlets.topics;

import church.cms.context.AppDeps;
import church.cms.domain.Topic;
import church.cms.repositories.TopicRepository;
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

@DisplayName("Integration Test suite for all Topic Use Cases")
public class TopicAllUseCasesIntegrationTest {
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
  public static TopicRepository topicRepository;
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

    topicRepository = appDeps.getTopicRepository();
    topicRepository.createTableIfNotExists();

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "topicsServlet", new TopicsServlet());
    context.addServletMappingDecoded("/api/topics/*", "topicsServlet");

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
  public void ifTopicCreatedSuccessfully() throws SQLException {
    topicRepository.truncateTable();

    CreateTopicPayload payload = new CreateTopicPayload("Faith");

    given()
            .body(payload)
            .when()
            .post("/api/topics")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_CREATED)
            .and()
            .body("name", equalTo(payload.name()));
  }

  @Test
  public void ifItListsTopicsSuccessfully() throws SQLException {
    topicRepository.truncateTable();

    Topic topic1 = new Topic("Faith");
    topicRepository.save(topic1);
    Topic topic2 = new Topic("Joy");
    topicRepository.save(topic2);

    get("/api/topics")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("name", hasItems(topic1.getName(), topic2.getName()));
  }

  @Test
  public void ifItUpdatesTopicSuccessfully() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    topicRepository.save(topic);

    String updatedTopicName = "Joy";
    UpdateTopicPayload payload = new UpdateTopicPayload(topic.getTopicId(), updatedTopicName);

    given()
            .body(payload)
            .put("/api/topics")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("topicId", equalTo(topic.getTopicId()), "name", equalTo(updatedTopicName));
  }

  @Test
  public void ifItErrorsWhenUpdatingUnexistingTopic() throws SQLException {
    topicRepository.truncateTable();

    UpdateTopicPayload payload = new UpdateTopicPayload(1, "not valid");

    given()
            .body(payload)
            .put("/api/topics")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  void ifItDeletesTopicSuccessfully() throws SQLException {
    topicRepository.truncateTable();

    Topic topic = new Topic("Faith");
    Topic createdTopic = topicRepository.save(topic);

    given()
            .delete("/api/topics/" + createdTopic.getTopicId())
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK);
  }
}
