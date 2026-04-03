package br.com.delivery.micro.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException() {super("Delivery not found!");}
    public DeliveryNotFoundException(String message) {
        super(message);
    }
}
