package church.cms.domain;

public class User {
  private Integer userId;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private Boolean isAdmin;
  private Boolean isRetired = false;

  public User(Integer userId, String firstName, String lastName, String email, String password, Boolean isAdmin) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  public User(Integer userId, String firstName, String lastName, String email, String password, Boolean isAdmin, Boolean isRetired) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
    this.isRetired = isRetired;
  }

  public User(String firstName, String lastName, String email, String password, Boolean isAdmin) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  public User(){}

  public Integer getUserId() {
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

  public boolean getIsRetired() {
    return isRetired;
  }

  public void setUserId(Integer id) {
    this.userId = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public void setIsRetired(boolean isRetired) {
    this.isRetired = isRetired;
  }
}