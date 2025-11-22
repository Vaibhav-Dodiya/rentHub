package com.example.rentHub.repository;
import com.example.rentHub.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PropertyRepository extends MongoRepository<Property, String> {
    List<Property> findByCategory(String category);
}
