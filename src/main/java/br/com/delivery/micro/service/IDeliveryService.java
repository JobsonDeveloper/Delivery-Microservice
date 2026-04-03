package br.com.delivery.micro.service;

import br.com.delivery.micro.domain.Delivered;

public interface IDeliveryService {
    public Delivered markAsDelivered(String deliveryId);
}
