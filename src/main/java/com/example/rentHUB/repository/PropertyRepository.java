package com.example.rentHUB.repository;
import com.example.rentHUB.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PropertyRepository extends MongoRepository<Property, String> {
    List<Property> findByCategory(String category);
}
