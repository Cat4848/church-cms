package church.cms.servlets.labels;

import church.cms.domain.Label;
import church.cms.repositories.LabelRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListLabelsUseCase {
  private final ObjectMapper objectMapper;
  private final LabelRepository labelRepository;
  private final Logger logger;

  public ListLabelsUseCase(ObjectMapper objectMapper, LabelRepository labelRepository, Logger logger) {
    this.objectMapper = objectMapper;
    this.labelRepository = labelRepository;
    this.logger = logger;
  }

  public void execute(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException,
          StreamWriteException {
    logger.info("start");

    List<Label> labels = labelRepository.list();

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(res.getWriter(), labels);

    logger.info("end");
  }

}
