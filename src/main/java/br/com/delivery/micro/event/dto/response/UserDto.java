package br.com.delivery.micro.event.dto.response;

import br.com.delivery.micro.domain.UserAddress;

public record UserDto(
        String id,
        String firstName,
        String lastName,
        String cpf,
        UserAddress address
) {
}
