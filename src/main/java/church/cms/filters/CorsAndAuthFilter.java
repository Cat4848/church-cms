package church.cms.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class CorsAndAuthFilter extends HttpFilter {
  private final Logger logger = LoggerFactory.getLogger(CorsAndAuthFilter.class);

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    logger.info("start");

    // deal with CORS related stuff
    res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
    res.setHeader("Access-Control-Allow-Headers", "X-CSRF-TOKEN, Content-Type");
    res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    res.setHeader("Access-Control-Expose-Headers", "X-CSRF-TOKEN");
    res.setHeader("Access-Control-Allow-Credentials", "true");

    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
      res.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    // deal with Authentication related stuff
    if (req.getRequestURI().startsWith("/church-cms/api/")) {
      HttpSession session = req.getSession(false);

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

      req.setAttribute("userId", userId);

    }
    logger.info("end");

    chain.doFilter(req, res);
  }
}
