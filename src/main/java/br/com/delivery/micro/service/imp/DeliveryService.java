package br.com.delivery.micro.service.imp;

import br.com.delivery.micro.domain.Delivered;
import br.com.delivery.micro.domain.Deliveries;
import br.com.delivery.micro.domain.Status;
import br.com.delivery.micro.event.dto.DeliveryProducerDto;
import br.com.delivery.micro.event.producer.DeliveryProducer;
import br.com.delivery.micro.exception.DeliveryNotFoundException;
import br.com.delivery.micro.repository.IDeliveredRepository;
import br.com.delivery.micro.repository.IDeliveriesRepository;
import br.com.delivery.micro.service.IDeliveryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryService implements IDeliveryService {
    private final IDeliveriesRepository iDeliveriesRepository;
    private final IDeliveredRepository iDeliveredRepository;
    private final DeliveryProducer deliveryProducer;

    public DeliveryService(
            IDeliveriesRepository iDeliveriesRepository,
            IDeliveredRepository iDeliveredRepository, DeliveryProducer deliveryProducer
    ) {
        this.iDeliveriesRepository = iDeliveriesRepository;
        this.iDeliveredRepository = iDeliveredRepository;
        this.deliveryProducer = deliveryProducer;
    }

    @Override
    public Delivered markAsDelivered(String deliveryId) {
        Deliveries delivery = iDeliveriesRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        iDeliveriesRepository.deleteById(deliveryId);

        Delivered delivered = Delivered.builder()
                .saleId(delivery.getSaleId())
                .paymentId(delivery.getPaymentId())
                .client(delivery.getClient())
                .deliveryForecast(delivery.getDeliveryForecast())
                .status(Status.DELIVERED)
                .created_at(delivery.getCreated_at())
                .updated_at(delivery.getUpdated_at())
                .delivered_at(LocalDateTime.now())
                .build();

        Delivered deliveredInfo = iDeliveredRepository.save(delivered);

        deliveryProducer.createEvent(new DeliveryProducerDto(
                deliveredInfo.getId(),
                delivered.getSaleId(),
                delivered.getClient().getId(),
                delivered.getStatus()
        ));

        return deliveredInfo;
    }
}
