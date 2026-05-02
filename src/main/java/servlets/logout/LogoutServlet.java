package servlets.logout;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    res.setStatus(HttpServletResponse.SC_OK);
  }
}
