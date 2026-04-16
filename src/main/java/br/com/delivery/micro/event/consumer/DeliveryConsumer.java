package br.com.delivery.micro.event.consumer;

import br.com.delivery.micro.domain.*;
import br.com.delivery.micro.event.dto.PaymentEventDto;
import br.com.delivery.micro.event.dto.SaleEventDto;
import br.com.delivery.micro.event.dto.response.ClientDto;
import br.com.delivery.micro.exception.DeliveryNotFoundException;
import br.com.delivery.micro.exception.client.ClientNotFoundException;
import br.com.delivery.micro.exception.client.ErrorGettingClientInfoException;
import br.com.delivery.micro.repository.ICanceledRepository;
import br.com.delivery.micro.repository.IDeliveriesRepository;
import br.com.delivery.micro.service.IClientDelivery;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DeliveryConsumer {
    private final IDeliveriesRepository iDeliveriesRepository;
    private final IClientDelivery iClientDelivery;
    private final ICanceledRepository iCanceledRepository;

    public DeliveryConsumer(
            IDeliveriesRepository iDeliveriesRepository,
            IClientDelivery iClientDelivery,
            ICanceledRepository iCanceledRepository
    ) {
        this.iDeliveriesRepository = iDeliveriesRepository;
        this.iClientDelivery = iClientDelivery;
        this.iCanceledRepository = iCanceledRepository;
    }

    @KafkaListener(
            topics = "payment",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentKafkaListenerFactory"
    )
    private void storeInDelivery(PaymentEventDto paymentEventDto) {
        Status status = paymentEventDto.status();
        String saleId = paymentEventDto.saleId();
        String paymentId = paymentEventDto.paymentId();
        String clientId = paymentEventDto.clientId();
        LocalDate date = LocalDate.now();
        int randomRange = ThreadLocalRandom.current().nextInt(0, 21);

        if (!status.equals(Status.PAID)) return;

        ClientDto clientResponse = null;

        try {
            clientResponse = iClientDelivery.getClientInfo(clientId).client();
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException();
        } catch (FeignClientException e) {
            throw new ErrorGettingClientInfoException();
        }

        ClientAddress address = ClientAddress.builder()
                .cep(clientResponse.address().getCep())
                .number(clientResponse.address().getNumber())
                .complement(clientResponse.address().getComplement())
                .build();

        ClientInfo client = ClientInfo.builder()
                .id(clientResponse.id())
                .firstName(clientResponse.firstName())
                .lastName(clientResponse.lastName())
                .cpf(clientResponse.cpf())
                .address(address)
                .build();

        LocalDate deliveryForecast = date.plusDays(randomRange);

        Deliveries deliveries = Deliveries.builder()
                .saleId(saleId)
                .paymentId(paymentId)
                .client(client)
                .deliveryForecast(deliveryForecast)
                .status(Status.SHIPPED)
                .created_at(LocalDateTime.now())
                .build();

        iDeliveriesRepository.save(deliveries);
    }

    @KafkaListener(
            topics = "sale",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "saleKafkaListenerFactory"
    )
    private void saleCanceledConsumer(SaleEventDto saleEventDto) {
        Status status = saleEventDto.status();
        String saleId = saleEventDto.id();

        if (!status.equals(Status.CANCELED)) return;

        Deliveries delivery = iDeliveriesRepository.findBySaleId(saleId).orElseThrow(DeliveryNotFoundException::new);

        Canceled canceledDelivery = Canceled.builder()
                .saleId(delivery.getId())
                .paymentId(delivery.getPaymentId())
                .client(delivery.getClient())
                .deliveryForecast(delivery.getDeliveryForecast())
                .status(Status.CANCELED)
                .created_at(LocalDateTime.now())
                .build();

        iCanceledRepository.save(canceledDelivery);
        iDeliveriesRepository.deleteById(delivery.getId());
    }
}
