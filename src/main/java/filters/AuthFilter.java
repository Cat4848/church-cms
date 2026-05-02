package filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.HttpMethod;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter extends HttpFilter {
  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    HttpSession session = req.getSession(false);
    if (session == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String token = (String) session.getAttribute("csrfToken");
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    if (!req.getMethod().equals(HttpMethod.GET)) {
      // check the CSRF token
      String userToken = req.getHeader("X-CSRF-TOKEN");
      if (userToken == null) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      if (!token.equals(userToken)) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    req.setAttribute("userId", userId);
    chain.doFilter(req, res);
  }
}
