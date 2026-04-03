package br.com.delivery.micro.exception.client;

public class ErrorGettingClientInfoException extends RuntimeException {
    public ErrorGettingClientInfoException() {super("It was not possible to get client information!");}
    public ErrorGettingClientInfoException(String message) {
        super(message);
    }
}
