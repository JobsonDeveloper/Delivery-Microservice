package br.com.delivery.micro.event.dto;

import br.com.delivery.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SaleEventDto(
        String id,
        Status status
){
}
