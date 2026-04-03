package br.com.delivery.micro.controller;

import br.com.delivery.micro.domain.Delivered;
import br.com.delivery.micro.dto.request.MarkDeliveryAsCompletedDto;
import br.com.delivery.micro.dto.response.ReturnDeliveryDto;
import br.com.delivery.micro.service.IDeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Delivery", description = "delivery operations")
public class DeliveryController {
    private final IDeliveryService iDeliveryService;

    public DeliveryController(IDeliveryService iDeliveryService) {
        this.iDeliveryService = iDeliveryService;
    }

    @PostMapping("/api/delivery/completed")
    @Operation(
            summary = "Mark delivery as completed",
            description = "Mark delivery as completed and store it",
            tags = {"Delivery"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Delivery mark as completed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReturnDeliveryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Incompatible data!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"error\": \"Validation failed\", \"errors\": \"[...]\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Delivery not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"NOT_FOUND\", \"message\": \"Delivery not found!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ReturnDeliveryDto> deliveryCompleted(@Valid @RequestBody MarkDeliveryAsCompletedDto dto) {
        String deliveryId = dto.id();
        Delivered delivered = iDeliveryService.markAsDelivered(deliveryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ReturnDeliveryDto(
                "Delivery mark as completed!",
                delivered
        ));
    }
}
