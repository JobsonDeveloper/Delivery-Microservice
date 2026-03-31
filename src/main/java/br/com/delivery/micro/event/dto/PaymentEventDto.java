package br.com.delivery.micro.event.dto;

import br.com.delivery.micro.domain.Status;

public record PaymentEventDto (
        String saleId,
        String paymentId,
        Status status
){
}
