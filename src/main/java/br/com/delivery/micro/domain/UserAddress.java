package br.com.delivery.micro.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAddress {
    private String cep;
    private String number;
    private String complement;
}
