package com.example.rentHUB.repository;
import com.example.rentHUB.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PropertyRepository extends MongoRepository<Property, String> {
}
