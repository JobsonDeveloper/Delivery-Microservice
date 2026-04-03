package br.com.delivery.micro.event.dto.response;

public record GetClientInfoDto(
        String message, ClientDto client
) {
}
