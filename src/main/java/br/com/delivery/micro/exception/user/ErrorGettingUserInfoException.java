package br.com.delivery.micro.exception.user;

public class ErrorGettingUserInfoException extends RuntimeException {
    public ErrorGettingUserInfoException() {super("It was not possible to get user information!");}
    public ErrorGettingUserInfoException(String message) {
        super(message);
    }
}
