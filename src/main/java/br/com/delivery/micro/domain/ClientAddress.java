package br.com.delivery.micro.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientAddress {
    private String cep;
    private String number;
    private String complement;
}
