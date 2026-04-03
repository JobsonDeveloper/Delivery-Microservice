package br.com.delivery.micro.exception;

public class ErrorCreatingDeliveryException extends RuntimeException {
    public ErrorCreatingDeliveryException() {
        super("It was not possible to save delivery data!");
    }
    public ErrorCreatingDeliveryException(String message) {
        super(message);
    }
}
