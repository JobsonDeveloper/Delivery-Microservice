package br.com.delivery.micro.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientInfo {
    private String id;
    private String firstName;
    private String lastName;
    private String cpf;
    private ClientAddress address;
}
