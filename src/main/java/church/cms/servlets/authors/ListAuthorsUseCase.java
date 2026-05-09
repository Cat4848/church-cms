package church.cms.servlets.authors;

import church.cms.domain.Author;
import church.cms.repositories.AuthorRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListAuthorsUseCase {
  private final ObjectMapper objectMapper;
  private final AuthorRepository authorRepository;
  private final Logger logger;

  public ListAuthorsUseCase(ObjectMapper objectMapper, AuthorRepository authorRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.authorRepository = authorRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException,
          StreamWriteException {
    logger.info("start");

    List<Author> authors = authorRepository.list();

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(res.getWriter(), authors);

    logger.info("end");
  }

}
