package br.com.delivery.micro.exception.client;

public class ClientNotFoundException extends RuntimeException {
  public ClientNotFoundException() {super("The client with this id was not found!");}
    public ClientNotFoundException(String message) {
        super(message);
    }
}
