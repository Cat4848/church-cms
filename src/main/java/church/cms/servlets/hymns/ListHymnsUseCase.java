package church.cms.servlets.hymns;

import church.cms.domain.Hymn;
import church.cms.repositories.HymnRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHymnsUseCase {
  private final ObjectMapper objectMapper;
  private final HymnRepository hymnRepository;
  private final Logger logger;

  public ListHymnsUseCase(ObjectMapper objectMapper, HymnRepository hymnRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.hymnRepository = hymnRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException,
                                                                              StreamWriteException {
    logger.info("start");

    String searchLyricsQuery = req.getParameter("searchLyrics");

    logger.debug("get hymns query string {}", searchLyricsQuery);

    List<Hymn> hymns = new ArrayList<>();

    if (searchLyricsQuery == null) {
      hymns = hymnRepository.list();
    } else {
      // MySQL syntax will not be happy if I pass * into FULLTEXT search
      if (!searchLyricsQuery.contains("*")) {
        hymns = hymnRepository.list(searchLyricsQuery);
      }
    }

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(res.getWriter(), hymns);

    logger.info("end");
  }

}
