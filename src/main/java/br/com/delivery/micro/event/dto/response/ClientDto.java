package br.com.delivery.micro.event.dto.response;

import br.com.delivery.micro.domain.ClientAddress;

public record ClientDto(
        String id,
        String firstName,
        String lastName,
        String cpf,
        ClientAddress address
) {
}
