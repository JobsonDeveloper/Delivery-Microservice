package br.com.delivery.micro.event.consumer;

import br.com.delivery.micro.domain.Status;
import br.com.delivery.micro.event.dto.PaymentEventDto;
import br.com.delivery.micro.repository.IDeliveriesRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeliveryConsumer {
    private final IDeliveriesRepository iDeliveriesRepository;

    public DeliveryConsumer(IDeliveriesRepository iDeliveriesRepository) {
        this.iDeliveriesRepository = iDeliveriesRepository;
    }

    @KafkaListener(topics = "payment",
            groupId = "${spring.kafka.consumer.group-id}")
    private void storeInDelivery(PaymentEventDto paymentEventDto) {
        Status status = paymentEventDto.status();
        String saleId = paymentEventDto.saleId();
        String paymentId = paymentEventDto.paymentId();
    }
}
