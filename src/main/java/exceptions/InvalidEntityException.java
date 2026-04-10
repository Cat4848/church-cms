package exceptions;

public class InvalidEntityException extends Exception {
  String message;

  public InvalidEntityException(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
