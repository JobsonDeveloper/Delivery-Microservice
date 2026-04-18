package br.com.delivery.micro.dto.swagger;

public record DefaultErrorResponseDto(
        String status,
        String message
) {
}
