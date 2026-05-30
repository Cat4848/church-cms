package church.cms.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

@WebFilter("/*")
public class CorsFilter extends HttpFilter {
  private final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException,
                                                                                                  ServletException {
    String[] allowedOrigins = new String[]{"http://localhost:5173"};

    logger.info("Setting the following allowed origins {}", Arrays.toString(allowedOrigins));

    for (String allowedOrigin : allowedOrigins) {
      res.setHeader("Access-Control-Allow-Origin", allowedOrigin);
    }
    res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    res.setHeader("Access-Control-Expose-Headers", "X-CSRF-TOKEN");

    chain.doFilter(req, res);
  }
}