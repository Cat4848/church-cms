package church.cms.servlets.logout;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    logger.info("start");

    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    res.setStatus(HttpServletResponse.SC_OK);

    logger.info("end");
  }
}
