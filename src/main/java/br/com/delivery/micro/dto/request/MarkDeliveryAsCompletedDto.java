package br.com.delivery.micro.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MarkDeliveryAsCompletedDto(
        @NotNull(message = "The delivery id is required!") @Size(min = 1, message = "The delivery id must be valid!") String id
) {
}

