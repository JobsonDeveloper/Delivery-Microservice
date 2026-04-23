package br.com.delivery.micro.exception.user;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {super("The user with this id was not found!");}
    public UserNotFoundException(String message) {
        super(message);
    }
}
