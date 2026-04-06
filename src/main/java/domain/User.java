package domain;

import java.util.Optional;

public class User {
  private Long userId;
  final String firstName;
  final String lastName;
  final String email;
  final String password;
  final boolean isAdmin;

  public User(Long userId, String firstName, String lastName, String email, String password, boolean isAdmin) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }
  public User(String firstName, String lastName, String email, String password, boolean isAdmin) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  public Long getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }
}