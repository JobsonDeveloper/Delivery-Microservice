package br.com.delivery.micro.repository;

import br.com.delivery.micro.domain.Deliveries;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeliveriesRepository extends MongoRepository<Deliveries, String> {
}
