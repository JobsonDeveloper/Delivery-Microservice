package br.com.delivery.micro.event.producer;

import br.com.delivery.micro.event.dto.DeliveryProducerDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeliveryProducer {
    private final KafkaTemplate<String, DeliveryProducerDto> kafkaTemplate;

    public DeliveryProducer(KafkaTemplate<String, DeliveryProducerDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createEvent(DeliveryProducerDto deliveryInfo) {
        kafkaTemplate.send("delivery", deliveryInfo);
    }
}
