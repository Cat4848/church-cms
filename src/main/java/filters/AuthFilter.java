package filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

    req.setAttribute("userId", userId);
    chain.doFilter(req, res);
  }
}
