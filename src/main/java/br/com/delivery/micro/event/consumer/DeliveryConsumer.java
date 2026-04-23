package br.com.delivery.micro.event.consumer;

import br.com.delivery.micro.domain.*;
import br.com.delivery.micro.event.dto.PaymentEventDto;
import br.com.delivery.micro.event.dto.SaleEventDto;
import br.com.delivery.micro.event.dto.response.UserDto;
import br.com.delivery.micro.exception.DeliveryNotFoundException;
import br.com.delivery.micro.exception.user.UserNotFoundException;
import br.com.delivery.micro.exception.user.ErrorGettingUserInfoException;
import br.com.delivery.micro.repository.ICanceledRepository;
import br.com.delivery.micro.repository.IDeliveriesRepository;
import br.com.delivery.micro.service.IUserDelivery;
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
    private final IUserDelivery iUserDelivery;
    private final ICanceledRepository iCanceledRepository;

    public DeliveryConsumer(
            IDeliveriesRepository iDeliveriesRepository,
            IUserDelivery iUserDelivery,
            ICanceledRepository iCanceledRepository
    ) {
        this.iDeliveriesRepository = iDeliveriesRepository;
        this.iUserDelivery = iUserDelivery;
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
        String userId = paymentEventDto.userId();
        LocalDate date = LocalDate.now();
        int randomRange = ThreadLocalRandom.current().nextInt(0, 21);

        if (!status.equals(Status.PAID)) return;

        UserDto userResponse = null;

        try {
            userResponse = iUserDelivery.getUserInfo(userId).user();
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException();
        } catch (FeignClientException e) {
            throw new ErrorGettingUserInfoException();
        }

        UserAddress address = UserAddress.builder()
                .cep(userResponse.address().getCep())
                .number(userResponse.address().getNumber())
                .complement(userResponse.address().getComplement())
                .build();

        UserInfo user = UserInfo.builder()
                .id(userResponse.id())
                .firstName(userResponse.firstName())
                .lastName(userResponse.lastName())
                .cpf(userResponse.cpf())
                .address(address)
                .build();

        LocalDate deliveryForecast = date.plusDays(randomRange);

        Deliveries deliveries = Deliveries.builder()
                .saleId(saleId)
                .paymentId(paymentId)
                .user(user)
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
                .user(delivery.getUser())
                .deliveryForecast(delivery.getDeliveryForecast())
                .status(Status.CANCELED)
                .created_at(LocalDateTime.now())
                .build();

        iCanceledRepository.save(canceledDelivery);
        iDeliveriesRepository.deleteById(delivery.getId());
    }
}
