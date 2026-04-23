package br.com.delivery.micro.event.dto.response;

public record GetUserInfoDto(
        String message, UserDto user
) {
}
