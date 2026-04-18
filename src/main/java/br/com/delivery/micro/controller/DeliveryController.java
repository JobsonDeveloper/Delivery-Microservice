package br.com.delivery.micro.controller;

import br.com.delivery.micro.domain.Delivered;
import br.com.delivery.micro.dto.request.MarkDeliveryAsCompletedDto;
import br.com.delivery.micro.dto.response.ReturnDeliveryDto;
import br.com.delivery.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.delivery.micro.dto.swagger.validation.fields.FieldsErrorDto;
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
            summary = "Finalize a delivery",
            description = "Mark a delivery as completed",
            tags = {"Delivery"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Delivery completed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReturnDeliveryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inconsistent request fields",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Delivery not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReturnDeliveryDto> deliveryCompleted(@Valid @RequestBody MarkDeliveryAsCompletedDto dto) {
        String deliveryId = dto.id();
        Delivered delivered = iDeliveryService.markAsDelivered(deliveryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ReturnDeliveryDto(
                "Delivery completed successfully!",
                delivered
        ));
    }
}
