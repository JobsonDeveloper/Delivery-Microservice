package br.com.delivery.micro.dto.response;

import br.com.delivery.micro.domain.Delivered;

public record ReturnDeliveryDto(
        String message,
        Delivered delivery
) {
}
