package br.com.delivery.micro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "delivered")
public class Delivered {
    @Id
    private String id;

    @Indexed(unique = true)
    private String saleId;

    @Indexed(unique = true)
    private String paymentId;

    private ClientInfo client;
    private LocalDate deliveryForecast;
    private Status status;

    @CreatedBy
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @CreatedBy
    private LocalDateTime delivered_at;
}
