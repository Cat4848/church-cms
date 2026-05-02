package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
  public PasswordUtil() {
  }

  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(12));
  }

  public boolean checkPassword(String plain, String hashed) {
    return BCrypt.checkpw(plain, hashed);
  }
}
