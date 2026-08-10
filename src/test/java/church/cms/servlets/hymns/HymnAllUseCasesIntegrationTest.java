package church.cms.servlets.hymns;

import church.cms.context.AppDeps;
import church.cms.domain.*;
import church.cms.repositories.*;
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
import static org.hamcrest.Matchers.*;

@DisplayName("Integration Test suite for all Hymns Use Cases")
public class HymnAllUseCasesIntegrationTest {
  public static HymnRepository hymnRepository;
  public static AuthorRepository authorRepository;
  public static HymnBookRepository hymnBookRepository;
  public static TopicRepository topicRepository;
  public static LabelRepository labelRepository;

  public static Author createdAuthor;
  public static HymnBook createdHymnBook;
  public static Topic createdTopic;
  public static Label createdLabel;
  public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.0");
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

    hymnBookRepository = appDeps.getHymnBookRepository();
    hymnBookRepository.createTableIfNotExists();

    topicRepository = appDeps.getTopicRepository();
    topicRepository.createTableIfNotExists();

    labelRepository = appDeps.getLabelRepository();
    labelRepository.createTableIfNotExists();

    hymnRepository = appDeps.getHymnRepository();
    hymnRepository.createTableIfNotExists();

    Author author = new Author("John Black");
    createdAuthor = authorRepository.save(author);

    HymnBook hymnBook = new HymnBook("Christina Hymns");
    createdHymnBook = hymnBookRepository.save(hymnBook);

    Topic topic = new Topic("Faith");
    createdTopic = topicRepository.save(topic);

    Label label = new Label("Sunday Morning");
    createdLabel = labelRepository.save(label);

    tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", null);
    context.getServletContext().setAttribute("appDeps", appDeps);

    Tomcat.addServlet(context, "hymnsServlet", new HymnsServlet());
    context.addServletMappingDecoded("/api/hymns/*", "hymnsServlet");

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
  public void ifCreatesHymnSuccessfully() throws SQLException {
    hymnRepository.truncateTable();

    CreateHymnPayload payload = new CreateHymnPayload(
            createdAuthor.getAuthorId(),
            "1800-1900",
            "Be Thou my vision",
            "Lyrics",
            createdHymnBook.getHymnBookId(),
            10,
            createdTopic.getTopicId(),
            createdLabel.getLabelId()
    );

    given()
            .body(payload)
            .when()
            .post("/api/hymns")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_CREATED)
            .and()
            .body("title", equalTo(payload.title()), "hymnId", notNullValue());
  }

  @Test
  public void ifItListsHymnsSuccessfully() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn1 = createHymn("Be Thou my vision");
    hymnRepository.save(hymn1);
    Hymn hymn2 = createHymn("It is well with my soul");
    hymnRepository.save(hymn2);

    get("/api/hymns")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("title", hasItems(hymn1.getTitle(), hymn2.getTitle()));
  }

  @Test
  public void ifItSearchesHymnsByLyricsSuccessfully() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = new Hymn(
            createdAuthor.getAuthorId(),
            "1800-1900",
            "Be Thou my vision",
            "Verse 1\n" +
                    "Be Thou my Vision, O Lord of my heart;\n" +
                    "Naught be all else to me, save that Thou art\n" +
                    "Thou my best Thought, by day or by night,\n" +
                    "Waking or sleeping, Thy presence my light.\n" +
                    "\n" +
                    "Verse 2\n" +
                    "Be Thou my Wisdom, and Thou my true Word;\n" +
                    "I ever with Thee and Thou with me, Lord;\n" +
                    "Thou my great Father, I Thy true son;\n" +
                    "Thou in me dwelling, and I with Thee one.\n" +
                    "\n" +
                    "Verse 3\n" +
                    "Be Thou my battle Shield, Sword for the fight;\n" +
                    "Be Thou my Dignity, Thou my Delight;\n" +
                    "Thou my soul's Shelter, Thou my high Tower:\n" +
                    "Raise Thou me heavenward, O Power of my power.\n" +
                    "\n" +
                    "Verse 4\n" +
                    "Riches I heed not, nor man's empty praise,\n" +
                    "Thou mine Inheritance, now and always:\n" +
                    "Thou and Thou only, first in my heart,\n" +
                    "High King of Heaven, my Treasure Thou art.\n" +
                    "\n" +
                    "Verse 5\n" +
                    "High King of Heaven, my victory won,\n" +
                    "May I reach Heaven's joys, O bright Heaven's Sun!\n" +
                    "Heart of my own heart, whatever befall,\n" +
                    "Still be my Vision, O Ruler of all.",
            createdHymnBook.getHymnBookId(),
            10,
            createdTopic.getTopicId(),
            createdLabel.getLabelId()
    );
    hymnRepository.save(hymn);

    String searchLyrics = "I ever with Thee and Thou with me, Lord;\n" +
            "Thou my great Father, I Thy true son;";
    get("/api/hymns?searchLyrics=" + searchLyrics)
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("title", hasItems(hymn.getTitle()));
  }

  @Test
  public void ifItUpdatesHymnSuccessfully() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    String updatedHymnTitle = "It is well with my soul";
    UpdateHymnPayload payload = new UpdateHymnPayload(
            createdHymn.getHymnId(),
            createdHymn.getAuthorId(),
            createdHymn.getAuthorExtras(),
            updatedHymnTitle,
            createdHymn.getLyrics(),
            createdHymn.getHymnBookId(),
            createdHymn.getNumberInHymnBook(),
            createdHymn.getTopicId(),
            createdHymn.getLabelId()
    );

    given()
            .body(payload)
            .put("/api/hymns")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK)
            .and()
            .body("hymnId", equalTo(hymn.getHymnId()), "title", equalTo(updatedHymnTitle));
  }

  @Test
  void ifItErrorsWhenUpdatingUnexistingHymn() throws SQLException {
    hymnRepository.truncateTable();

    UpdateHymnPayload payload = new UpdateHymnPayload(
            1,
            1,
            "not valid",
            "not valid",
            "not valid",
            1,
            1,
            1,
            1
    );

    given()
            .body(payload)
            .put("/api/hymns")
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  void ifItDeletesHymnSuccessfully() throws SQLException {
    hymnRepository.truncateTable();

    Hymn hymn = createHymn("Be Thou my vision");
    Hymn createdHymn = hymnRepository.save(hymn);

    given()
            .delete("/api/hymns/" + createdHymn.getTopicId())
            .then()
            .assertThat()
            .statusCode(HttpServletResponse.SC_OK);
  }

  Hymn createHymn(String title) {
    return new Hymn(
            createdAuthor.getAuthorId(),
            "1800-1900",
            title,
            "Lyrics",
            createdHymnBook.getHymnBookId(),
            10,
            createdTopic.getTopicId(),
            createdLabel.getLabelId()
    );
  }
}
