package br.com.delivery.micro.event.dto;

import br.com.delivery.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentEventDto (
        String paymentId,
        String userId,
        String userCpf,
        String saleId,
        Status status
){
}
