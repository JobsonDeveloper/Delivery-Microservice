package br.com.delivery.micro.repository;

import br.com.delivery.micro.domain.Delivered;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeliveredRepository extends MongoRepository<Delivered, String> {
}
