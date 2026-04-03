package br.com.delivery.micro.event.dto;

import br.com.delivery.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentEventDto (
        String paymentId,
        String clientId,
        String clientCpf,
        String saleId,
        Status status
){
}
