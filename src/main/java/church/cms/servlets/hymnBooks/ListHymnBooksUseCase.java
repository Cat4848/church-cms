package church.cms.servlets.hymnBooks;

import church.cms.domain.HymnBook;
import church.cms.repositories.HymnBookRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListHymnBooksUseCase {
  private final ObjectMapper objectMapper;
  private final HymnBookRepository hymnBookRepository;
  private final Logger logger;

  public ListHymnBooksUseCase(ObjectMapper objectMapper, HymnBookRepository hymnBookRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.hymnBookRepository = hymnBookRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException,
          StreamWriteException {
    logger.info("start");

    List<HymnBook> hymnBooks = hymnBookRepository.list();

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(res.getWriter(), hymnBooks);

    logger.info("end");
  }

}
