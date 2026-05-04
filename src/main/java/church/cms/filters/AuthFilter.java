package church.cms.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter extends HttpFilter {
  private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    HttpSession session = req.getSession(false);

    logger.info("start");

    if (session == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // extra check, a bit redundant because if the session is null it can't have attributes
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // extra check, a bit redundant because if the session is null it can't have attributes
    String token = (String) session.getAttribute("csrfToken");
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String httpMethod = req.getMethod();

    logger.info("userId {}", userId);
    logger.info("http method: {}", httpMethod);

    if (!httpMethod.equals(HttpMethod.GET)) {
      // check the CSRF token
      String userToken = req.getHeader("X-CSRF-TOKEN");
      if (userToken == null) {
        logger.info("CSRF token not found in headers");

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      if (!token.equals(userToken)) {
        logger.info("CSRF token not valid");

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    req.setAttribute("userId", userId);

    logger.info("end");

    chain.doFilter(req, res);
  }
}
