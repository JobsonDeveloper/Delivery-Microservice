package br.com.delivery.micro.event.dto;

import br.com.delivery.micro.domain.Status;

public record DeliveryProducerDto(
        String id,
        String saleId,
        String clientId,
        Status status
) {
}
